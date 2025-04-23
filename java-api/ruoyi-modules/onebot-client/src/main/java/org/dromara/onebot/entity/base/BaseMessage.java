package org.dromara.onebot.entity.base;

import lombok.Data;

/**
 * OneBot消息基础类
 */
@Data
public class BaseMessage {
    /**
     * 消息ID
     */
    private String id;

    /**
     * 时间戳
     */
    private Long time;

    /**
     * 消息类型
     */
    private String type;

    /**
     * 详细类型
     */
    private String detail_type;

    /**
     * 子类型
     */
    private String sub_type;
}
