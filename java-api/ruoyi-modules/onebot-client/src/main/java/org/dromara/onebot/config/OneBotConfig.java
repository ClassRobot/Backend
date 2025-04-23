package org.dromara.onebot.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OneBotConfig {

    private String baseurl;

    private Long botId;
    
    /**
     * 平台名称
     */
    @Builder.Default
    private String platform = "YanXi-Adapter";
    
    /**
     * 实现版本
     */
    @Builder.Default
    private String version = "0.0.1";
    
    /**
     * OneBot协议版本
     */
    @Builder.Default
    private String onebotVersion = "12";
    
    /**
     * 用户ID
     */
    @Builder.Default
    private String userId = "YanXiBot1";
}
