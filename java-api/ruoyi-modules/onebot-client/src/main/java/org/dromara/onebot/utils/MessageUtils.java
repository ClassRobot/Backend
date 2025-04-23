package org.dromara.onebot.utils;

import cn.hutool.core.util.IdUtil;
import org.dromara.onebot.entity.message.ChatMessage;
import org.dromara.onebot.entity.message.ChatMessage.MessageData;
import org.dromara.onebot.entity.message.ChatMessageDataEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageUtils {

    /**
     * 创建文本消息元素
     *
     * @return 消息元素
     */
    public static ChatMessage createChatMessage(String user_id, String detail_type) {

        ChatMessage message = new ChatMessage();

        message.setId(IdUtil.fastSimpleUUID());
        message.setTime(System.currentTimeMillis() / 1000);
        message.setType("message");
        message.setMessage_id(message.getId());

        message.setDetail_type(detail_type);
        message.setSub_type("");
        message.setUser_id(user_id);

        return message;
    }

    /**
     * 创建文本消息元素
     *
     * @param text 文本内容
     * @return 消息元素
     */
    public static Map<String, Object> createTextMessage(String text) {
        Map<String, Object> textMsg = new HashMap<>();
        textMsg.put("type", "text");

        Map<String, Object> data = new HashMap<>();
        data.put("text", text);
        textMsg.put("data", data);

        return textMsg;
    }

    /**
     * 创建图片消息元素
     *
     * @param fileId 文件ID
     * @return 消息元素
     */
    public static Map<String, Object> createImageMessage(String fileId) {
        Map<String, Object> imageMsg = new HashMap<>();
        imageMsg.put("type", "image");

        Map<String, Object> data = new HashMap<>();
        data.put("file_id", fileId);
        imageMsg.put("data", data);

        return imageMsg;
    }

    /**
     * 创建图片消息元素（通过URL）
     *
     * @param url 图片URL
     * @return 消息元素
     */
    public static Map<String, Object> createImageMessageByUrl(String url) {
        Map<String, Object> imageMsg = new HashMap<>();
        imageMsg.put("type", "image");

        Map<String, Object> data = new HashMap<>();
        data.put("url", url);
        imageMsg.put("data", data);

        return imageMsg;
    }

    public static String getAltMessageByData(Object message) {

        StringBuffer str = new StringBuffer();

        for (MessageData messageData : (List<MessageData>) message) {
            if(ChatMessageDataEnum.text.toString().equals(messageData.getType())){
                str.append(messageData.getData().get("text"));
            }else if(ChatMessageDataEnum.image.toString().equals(messageData.getType())){
                str.append("[图片]");
            }
        }

        return  str.toString();
    }
}
