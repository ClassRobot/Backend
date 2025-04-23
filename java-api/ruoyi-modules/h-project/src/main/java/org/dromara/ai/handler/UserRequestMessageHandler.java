package org.dromara.ai.handler;

import lombok.RequiredArgsConstructor;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.message.domain.MessagesBo;
import org.dromara.message.service.IMessageService;
import org.dromara.onebot.annotation.RequestMessageMapping;
import org.dromara.onebot.entity.message.ChatMessage;
import org.dromara.onebot.entity.request.RequestMessage;
import org.dromara.onebot.entity.request.ResponseMessage;
import org.dromara.onebot.handler.RequestMessageHandler;
import org.dromara.system.service.ISysOssService;
import org.springframework.stereotype.Service;

/**
 * 示例请求消息处理器
 */
@Service
@RequiredArgsConstructor
public class MessageRequestMessageHandler implements RequestMessageHandler {

    private final IMessageService messageService;

    /**
     * 处理send_message请求
     */
    @RequestMessageMapping(value = "send_message", description = "处理接收到的message消息")
    public ResponseMessage sendMessage(RequestMessage request) {
        String userId = (String) request.getParams().get("user_id");
        Object message = request.getParams().get("message");

        MessagesBo build = MessagesBo.builder()
                .receiveId(Long.parseLong(userId))
                .messages(JsonUtils.parseArray(JsonUtils.toJsonString(message), ChatMessage.MessageData.class))
                .build();

        messageService.sendPrivateMessage(build, 0L);

//        WebSocketUtils.sendMessage(Long.parseLong(userId), JsonUtils.toJsonString(build));

        // 构建响应消息
        return ResponseMessage.ok();
    }

}