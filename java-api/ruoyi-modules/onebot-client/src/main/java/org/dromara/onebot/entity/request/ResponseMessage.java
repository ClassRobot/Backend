package org.dromara.onebot.entity.request;

import lombok.*;
import org.dromara.onebot.entity.base.BaseMessage;

import java.util.HashMap;

/**
 * OneBot响应消息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessage  {
    /**
     * 状态
     */
    private String status;

    /**
     * 状态码
     */
    @Builder.Default
    private Integer retcode = 0;

    /**
     * 返回消息
     */
    @Builder.Default
    private String message = "";

    /**
     * 返回数据
     */
    private Object data;

    /**
     * 回声字段，用于标识请求
     */
    private String echo;

    public static ResponseMessage ok(Object data){
        return ResponseMessage.builder()
                .status("ok")
                .data(data)
                .build();
    }

    public static ResponseMessage ok(){
        return ResponseMessage.ok(new HashMap<>());
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserData {
        private String user_id;
        private String user_name;

        /**
         * 用户显示名称
         */
        private String user_displayname;
        private String user_remark;
    }
}
