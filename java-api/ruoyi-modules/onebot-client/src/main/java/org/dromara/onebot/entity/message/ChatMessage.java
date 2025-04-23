package org.dromara.onebot.entity.message;

import lombok.*;
import org.dromara.onebot.entity.base.BaseMessage;
import org.dromara.onebot.entity.base.SelfMessage;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class ChatMessage  extends BaseMessage {

    /**
     * 消息参数
     */
    private Object message;

    /**
     * 消息文本
     */
    private String alt_message;

    /**
     * 消息id
     */
    private Object message_id;


    private SelfMessage self;


    private String user_id;


    @Data
    public static class MessageData {
        /**
         * 消息类型
         * */
        private String type;
        /**
         * 消息数据 示例：{text : "你好"}
         * */
        private Map<String, String> data;
    }

}
