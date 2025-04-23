package org.dromara.onebot.entity.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.onebot.entity.base.BaseMessage;

import java.util.List;

/**
 * OneBot状态消息
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StatusMessage extends BaseMessage {
    /**
     * 状态信息
     */
    private StatusInfo status;
    
    /**
     * 状态信息类
     */
    @Data
    public static class StatusInfo {
        /**
         * 是否良好
         */
        private Boolean good;
        
        /**
         * 机器人列表
         */
        private List<BotInfo> bots;
    }
    
    /**
     * 机器人信息类
     */
    @Data
    public static class BotInfo {
        /**
         * 自身信息
         */
        private SelfInfo self;
        
        /**
         * 是否在线
         */
        private Boolean online;
    }
    
    /**
     * 自身信息类
     */
    @Data
    public static class SelfInfo {
        /**
         * 平台
         */
        private String platform;
        
        /**
         * 用户ID
         */
        private String user_id;
    }
}