package org.dromara.onebot.entity.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.onebot.entity.base.BaseMessage;

/**
 * OneBot事件消息
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EventMessage extends BaseMessage {
    /**
     * 事件内容
     */
    private Object data;
}