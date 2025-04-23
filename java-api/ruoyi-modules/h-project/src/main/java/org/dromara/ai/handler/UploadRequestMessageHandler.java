package org.dromara.ai.handler;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.common.websocket.utils.WebSocketUtils;
import org.dromara.message.domain.MessagesBo;
import org.dromara.message.service.IMessageService;
import org.dromara.onebot.annotation.RequestMessageMapping;
import org.dromara.onebot.entity.message.ChatMessage;
import org.dromara.onebot.entity.request.RequestMessage;
import org.dromara.onebot.entity.request.ResponseMessage;
import org.dromara.onebot.entity.request.params.UploadRequestParams;
import org.dromara.onebot.handler.RequestMessageHandler;
import org.dromara.system.domain.vo.SysOssVo;
import org.dromara.system.service.ISysOssService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * 示例请求消息处理器
 */
@Service
@RequiredArgsConstructor
public class ExampleRequestMessageHandler implements RequestMessageHandler {


    private final IMessageService messageService;
    private final ISysOssService ossService;

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

    /**
     * 处理upload_file请求
     */
    @RequestMessageMapping(value = "upload_file", description = "处理接收到的message消息")
    public ResponseMessage upload_file(RequestMessage request) {
        HashMap<String, Object> data = new HashMap<>();

        try {
            JsonNode jsonNode = JsonUtils.getObjectMapper().readTree(request.getJson());
            UploadRequestParams params = JsonUtils.parseObject(jsonNode.get("params").asText(), UploadRequestParams.class);

            if ("data".equals(params.getType())) {
                // 解码base64数据
                String base64Data = params.getData();
                // 移除可能存在的base64前缀（如data:image/jpeg;base64,）
                if (base64Data.contains(",")) {
                    base64Data = base64Data.split(",")[1];
                }
                
                // 将base64转换为字节数组
                byte[] fileBytes = java.util.Base64.getDecoder().decode(base64Data);
                
                // 创建临时文件
                String fileName = params.getName() != null ? params.getName() : "upload_" + System.currentTimeMillis();
                java.io.File tempFile = java.io.File.createTempFile("upload_", "_" + fileName);
                try (java.io.FileOutputStream fos = new java.io.FileOutputStream(tempFile)) {
                    fos.write(fileBytes);
                }
                
                // 使用OSS服务上传文件
                SysOssVo upload = ossService.upload(tempFile);

                // 删除临时文件
                tempFile.delete();

                data.put("file_id", upload.getOssId());

                // 返回上传结果
                return ResponseMessage.ok(data);
            }

        } catch (JacksonException e) {
        } catch (Exception e) {
        }

        // 构建响应消息
        return ResponseMessage.ok(data);
    }

    /**
     * 处理get_user_info请求
     */
    @RequestMessageMapping(value = "get_user_info", description = "获取用户信息")
    public ResponseMessage getUserInfo(RequestMessage request) {

        String userId = (String) request.getParams().get("user_id");

        // 创建用户数据
        ResponseMessage.UserData userData = new ResponseMessage.UserData();
        userData.setUser_id(userId);
        userData.setUser_name("Example User");
        userData.setUser_displayname("Example Display Name");
        userData.setUser_remark("Example Remark");
        
        // 构建响应消息
        return ResponseMessage.ok(userData);
    }
    
    /**
     * 处理get_version请求
     */
    @RequestMessageMapping(value = "get_version", description = "获取版本信息", priority = 0)
    public ResponseMessage getVersion(RequestMessage message) {
        // 创建版本数据
        ResponseMessage.UserData versionData = new ResponseMessage.UserData();
        versionData.setUser_id("1.0.0"); // 使用UserData字段存储版本信息
        
        // 构建响应消息
        return ResponseMessage.ok(versionData);
    }

}