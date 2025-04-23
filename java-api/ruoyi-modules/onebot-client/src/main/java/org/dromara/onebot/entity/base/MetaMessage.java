package org.dromara.onebot.entity.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.onebot.entity.base.BaseMessage;

/**
 * OneBot元数据消息
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MetaMessage extends BaseMessage {
    /**
     * 版本信息
     */
    private VersionInfo version;
    
    /**
     * 版本信息类
     */
    @Data
    public static class VersionInfo {
        /**
         * 实现名称
         */
        private String impl;
        
        /**
         * 版本号
         */
        private String version;
        
        /**
         * OneBot版本
         */
        private String onebot_version;
    }
}