package org.dromara.onebot.socket;


import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dromara.onebot.config.OneBotConfig;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import org.dromara.onebot.entity.message.ChatMessageTypeEnum;
import org.dromara.onebot.handler.MessageHandler;
import org.dromara.onebot.handler.RequestMessageHandlerManager;
import org.dromara.onebot.entity.message.ChatMessage;
import org.dromara.onebot.utils.MessageUtils;

@Getter
public class OneBotClient {

    private OneBotConfig config;
    public static Logger logger;
    private OneBotSocket socket;

    @Getter
    @Setter
    private RequestMessageHandlerManager requestMessageHandlerManager;

    public OneBotClient(String serverUri) {
        this.socket = new OneBotSocket(URI.create(serverUri), this);
        this.socket.connect();
    }

    public static OneBotClient create(OneBotConfig config) {
        logger = LogManager.getLogger("OneBot Client");

        OneBotClient client = new OneBotClient(config.getBaseurl());

        client.config = config;

        return client;
    }

    /**
     * 发送私聊消息
     *
     * @param userId 用户ID
     * @param message 消息内容
     * @return 响应消息的CompletableFuture
     */
    public void sendPrivateMessage(String userId, String message) {
        socket.sendMessage(userId, "private",
                Collections.singletonList(MessageUtils
                        .createTextMessage(message)));
    }

    /**
     * 发送私聊消息
     *
     * @param userId 用户ID
     * @param msg 消息内容
     * @return 响应消息的CompletableFuture
     */
    public ChatMessage sendPrivateMessage(String userId, List<ChatMessage.MessageData> msg) {
        ChatMessage message = MessageUtils.createChatMessage(userId, ChatMessageTypeEnum.PRIVATE.getValue());
        return socket.sendMessage(message, msg);
    }

    /**
     * 发送私聊消息
     *
     * @param userId 用户ID
     * @param msg 消息内容
     * @return 响应消息的CompletableFuture
     */
    public ChatMessage sendPrivateMessage(String id, String userId, List<ChatMessage.MessageData> msg) {
        ChatMessage message = MessageUtils.createChatMessage(userId, ChatMessageTypeEnum.PRIVATE.getValue());
        message.setId(id);
        message.setMessage_id(id);
        return socket.sendMessage(message, msg);
    }

}
