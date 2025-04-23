package org.dromara.message.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.onebot.entity.message.ChatMessage.MessageData;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessagesBo {
    /**
     * 消息列表
     */
    private List<MessageData> messages;

    /**
     * 接收者ID，不填则为机器人
     */
    private long receiveId;
}
