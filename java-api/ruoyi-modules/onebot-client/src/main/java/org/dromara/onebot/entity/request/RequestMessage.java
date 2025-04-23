package org.dromara.onebot.entity.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.onebot.entity.base.BaseMessage;
import org.dromara.onebot.entity.base.SelfMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * OneBot请求消息
 */
@Data
public class RequestMessage {
    /**
     * 动作名称
     */
    private String action;

    /**
     * 动作参数
     */
    private HashMap<String, Object> params;

    /**
     * 回声字段，用于标识请求
     */
    private String echo;

    /**
     * 机器人信息
     */
    private SelfMessage self;

    /**
     * 请求原生数据
     */
    private String json;
}
