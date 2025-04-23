package org.dromara.onebot.socket;

import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.onebot.entity.base.EventMessage;
import org.dromara.onebot.entity.base.MetaMessage;
import org.dromara.onebot.entity.base.SelfMessage;
import org.dromara.onebot.entity.base.StatusMessage;
import org.dromara.onebot.entity.message.ChatMessage;
import org.dromara.onebot.entity.request.RequestMessage;
import org.dromara.onebot.entity.request.ResponseMessage;
import org.dromara.onebot.utils.MessageUtils;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class OneBotSocket extends WebSocketClient {

    private OneBotClient client;

    // 存储请求和响应的映射关系
    private final Map<String, CompletableFuture<ResponseMessage>> responseMap = new ConcurrentHashMap<>();

    // 消息处理器
    private final ObjectMapper objectMapper = new ObjectMapper();


    public OneBotSocket(URI serverUri, OneBotClient client) {
        super(serverUri);
        this.client = client;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        OneBotClient.logger.info("OneBotClient {}链接成功 机器人ID：{}",
            client.getConfig().getBaseurl(),
            client.getConfig().getBotId());

        // 发送元数据消息
        sendMetaMessage();

        // 发送状态更新消息
        sendStatusMessage();
    }

    @Override
    public void onMessage(String s) {
        // 将消息转发给OneBotClient进行处理
        handleMessage(s);
    }

    @Override
    public void onMessage(ByteBuffer s) {
        OneBotClient.logger.info("OneBotClient 收到消息：{}", s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        OneBotClient.logger.info("OneBotClient {}链接关闭 ：{} : {}",i, s, b);

//        connect();
    }

    @Override
    public void onError(Exception e) {
        OneBotClient.logger.info("OneBotClient报错 ：{}", e);
    }

    /**
     * 发送元数据消息
     */
    private void sendMetaMessage() {
        MetaMessage message = new MetaMessage();
        message.setId(IdUtil.fastSimpleUUID());
        message.setTime(System.currentTimeMillis() / 1000);
        message.setType("meta");
        message.setDetail_type("connect");
        message.setSub_type("");

        MetaMessage.VersionInfo versionInfo = new MetaMessage.VersionInfo();
        versionInfo.setImpl(client.getConfig().getPlatform());
        versionInfo.setVersion(client.getConfig().getVersion());
        versionInfo.setOnebot_version(client.getConfig().getOnebotVersion());
        message.setVersion(versionInfo);

        String jsonMessage = JsonUtils.toJsonString(message);
        OneBotClient.logger.info("发送元数据消息：{}", jsonMessage);
        send(jsonMessage);
    }

    /**
     * 发送状态更新消息
     */
    private void sendStatusMessage() {
        StatusMessage message = new StatusMessage();
        message.setId(IdUtil.fastSimpleUUID());
        message.setTime(System.currentTimeMillis() / 1000);
        message.setType("meta");
        message.setDetail_type("status_update");
        message.setSub_type("");

        StatusMessage.StatusInfo statusInfo = new StatusMessage.StatusInfo();
        statusInfo.setGood(true);

        StatusMessage.SelfInfo selfInfo = new StatusMessage.SelfInfo();
        selfInfo.setPlatform(client.getConfig().getPlatform());
        selfInfo.setUser_id(client.getConfig().getUserId());

        StatusMessage.BotInfo botInfo = new StatusMessage.BotInfo();
        botInfo.setSelf(selfInfo);
        botInfo.setOnline(true);

        statusInfo.setBots(Collections.singletonList(botInfo));
        message.setStatus(statusInfo);

        String jsonMessage = JsonUtils.toJsonString(message);
        OneBotClient.logger.info("发送状态更新消息：{}", jsonMessage);
        send(jsonMessage);
    }


    /**
     * 处理接收到的消息
     *
     * @param message 接收到的消息字符串
     */
    public void handleMessage(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            String type = jsonNode.has("type") ? jsonNode.get("type").asText() : "";

            if ("meta".equals(type)) {
                // 处理元数据消息
                MetaMessage metaMessage = JsonUtils.parseObject(message, MetaMessage.class);
                handleMetaMessage(metaMessage);
            } else if ("message".equals(type)) {
                // 处理事件消息
                EventMessage eventMessage = JsonUtils.parseObject(message, EventMessage.class);
                handleEventMessage(eventMessage);
            } else if (jsonNode.has("echo")) {
                // 处理请求消息
                RequestMessage requestMessage = JsonUtils.parseObject(message, RequestMessage.class);
                requestMessage.setJson(message);
                handleRequestMessage(requestMessage);
            }else {
//                OneBotClient.logger.info("OneBotClient 未知消息：{}", message);
            }
        } catch (JsonProcessingException e) {
            OneBotClient.logger.error("解析消息失败: {}", e.getMessage());
        }
    }

    /**
     * 处理元数据消息
     *
     * @param message 元数据消息
     */
    private void handleMetaMessage(MetaMessage message) {
        OneBotClient.logger.info("收到元数据消息: {}", JsonUtils.toJsonString(message));
    }

    /**
     * 处理事件消息
     *
     * @param message 事件消息
     */
    private void handleEventMessage(EventMessage message) {
        OneBotClient.logger.info("收到事件消息: {}", JsonUtils.toJsonString(message));
    }

    /**
     * 处理请求消息
     *
     * @param message 响应消息
     */
    private void handleRequestMessage(RequestMessage message) {
        if (message.getJson().length() > 200000) {
            OneBotClient.logger.info("收到请求消息，消息太大: {}", message.getAction());
        }else {
            OneBotClient.logger.info("收到请求消息: {}", message.getJson());
        }

        // 使用RequestMessageHandlerManager处理请求消息
        ResponseMessage responseMessage;
        try {
            if (client.getRequestMessageHandlerManager() != null) {
                responseMessage = client.getRequestMessageHandlerManager().handleRequest(message);
            } else {
                responseMessage = ResponseMessage.builder()
                    .status("failed")
                    .retcode(404)
                    .echo(message.getEcho())
                    .build();
            }
        } catch (Exception e) {
            OneBotClient.logger.error("处理请求消息失败: {}", e.getMessage(), e);
            responseMessage = ResponseMessage.builder()
                    .status("failed")
                    .retcode(500)
                    .message("Internal error: " + e.getMessage())
                    .echo(message.getEcho())
                    .build();
        }
        
        String jsonString = JsonUtils.toJsonString(responseMessage);
        send(jsonString);
        OneBotClient.logger.info("发送响应消息: {}", jsonString);
    }

    /**
     * 发送消息
     *
     * @param detail_type 消息名称
     * @param msg         参数
     */
    public ChatMessage sendMessage(String userId, String detail_type, Object msg) {

        ChatMessage message = MessageUtils.createChatMessage(userId, detail_type);

        sendMessage(message, msg);

        return message;
    }

    /**
     * 发送消息
     *
     * @param message 消息
     * @param msg         参数
     */
    public ChatMessage sendMessage(ChatMessage message, Object msg) {

        message.setSelf(SelfMessage.builder()
                .platform(client.getConfig().getPlatform())
                .user_id(client.getConfig().getUserId())
                .build());

        message.setMessage(msg);
        message.setAlt_message(MessageUtils.getAltMessageByData(msg));

        sendMessage(message);

        return message;
    }

    /**
     * 发送消息
     *
     * @param message 消息
     */
    public void sendMessage(ChatMessage message) {
        message.setSelf(SelfMessage.builder()
                .platform(client.getConfig().getPlatform())
                .user_id(client.getConfig().getUserId())
                .build());
        String jsonMessage = JsonUtils.toJsonString(message);
        OneBotClient.logger.info("发送消息: {}", jsonMessage);
        send(jsonMessage);
    }

    /**
     * 发送事件消息
     *
     * @param detailType 详细类型
     * @param subType    子类型
     * @param data       数据
     */
    public void sendEvent(String detailType, String subType, Object data) {
        EventMessage message = new EventMessage();
        message.setId(IdUtil.fastSimpleUUID());
        message.setTime(System.currentTimeMillis() / 1000);
        message.setType("event");
        message.setDetail_type(detailType);
        message.setSub_type(subType);
        message.setData(data);

        String jsonMessage = JsonUtils.toJsonString(message);
        OneBotClient.logger.info("发送事件消息: {}", jsonMessage);
        send(jsonMessage);
    }
}
