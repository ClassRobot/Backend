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
import org.dromara.system.domain.vo.SysOssVo;
import org.dromara.system.service.ISysOssService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 示例请求消息处理器
 */
@Service
@RequiredArgsConstructor
public class MessageRequestMessageHandler implements RequestMessageHandler {

    private final IMessageService messageService;
    private final ISysOssService ossService;

    /**
     * 处理send_message请求
     */
    @RequestMessageMapping(value = "send_message", description = "处理接收到的message消息")
    public ResponseMessage sendMessage(RequestMessage request) {
        String userId = (String) request.getParams().get("user_id");
        Object message = request.getParams().get("message");

        List<ChatMessage.MessageData> messageData = JsonUtils.parseArray(JsonUtils.toJsonString(message), ChatMessage.MessageData.class);

        buildFileUrl(messageData);

        MessagesBo build = MessagesBo.builder()
                .receiveId(Long.parseLong(userId))
                .messages(messageData)
                .build();

        messageService.sendPrivateMessage(build, 0L);

        // 构建响应消息
        return ResponseMessage.ok();
    }

    private void buildFileUrl(List<ChatMessage.MessageData> messageData){
//        messageData.forEach(data -> {
//            for (String value : data.getData().keySet()) {
//                if(value.equals("file_id")){
//                    String url = ossService.getById(Long.valueOf(data.getData().get("file_id"))).getUrl();
//                    data.getData().put("file_url", url);
//                }
//            }
//        });

        List<Long> fileId = messageData.stream().map(data ->
                Long.parseLong(data.getData().get("file_id")))
                .toList();

        Map<Long, String> ossMap = ossService.listByIds(fileId).stream().collect(Collectors.toMap(SysOssVo::getOssId, SysOssVo::getUrl));

        messageData.forEach(data -> {
            for (String value : data.getData().keySet()) {
                if("file_id".equals(value)){
                    data.getData().put("file_url", ossMap.get(Long.parseLong(data.getData().get(value))));
                }
            }
        });

    }

}