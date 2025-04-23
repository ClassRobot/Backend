package org.dromara.ai.handler;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.onebot.annotation.RequestMessageMapping;
import org.dromara.onebot.entity.request.RequestMessage;
import org.dromara.onebot.entity.request.ResponseMessage;
import org.dromara.onebot.entity.request.params.GetFileRequestParams;
import org.dromara.onebot.entity.request.params.UploadFileRequestParams;
import org.dromara.onebot.handler.RequestMessageHandler;
import org.dromara.system.domain.vo.SysOssVo;
import org.dromara.system.service.ISysOssService;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * 示例请求消息处理器
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UploadRequestMessageHandler implements RequestMessageHandler {


    private final ISysOssService ossService;

    /**
     * 处理upload_file请求
     */
    @RequestMessageMapping(value = "upload_file", description = "单文件文件上传")
    public ResponseMessage upload_file(RequestMessage request) throws Exception {
        HashMap<String, Object> data = new HashMap<>();

        JsonNode jsonNode = JsonUtils.getObjectMapper().readTree(request.getJson());

        jsonNode = jsonNode.get("params");

        UploadFileRequestParams params = UploadFileRequestParams.builder()
                .data(jsonNode.get("data").asText())
                .type(jsonNode.get("type").asText())
                .name(jsonNode.get("name").asText())
                .build();

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
            data.put("url", upload.getUrl());

            // 返回上传结果
            return ResponseMessage.ok(data);
        }

        // 构建响应消息
        return ResponseMessage.ok(data);
    }


    /**
     * 处理get_file请求
     */
    @RequestMessageMapping(value = "get_file", description = "根据id获取文件")
    public ResponseMessage get_file(RequestMessage request) {
        HashMap<String, Object> data = new HashMap<>();

        try {
            JsonNode jsonNode = JsonUtils.getObjectMapper().readTree(request.getJson());
            GetFileRequestParams params = JsonUtils.parseObject(jsonNode.get("params").asText(), GetFileRequestParams.class);

            SysOssVo byId = ossService.getById(Long.valueOf(params.getFile_id()));

            data.put("url", byId.getUrl());
            data.put("name", byId.getFileName());

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 构建响应消息
        return ResponseMessage.ok(data);
    }

}