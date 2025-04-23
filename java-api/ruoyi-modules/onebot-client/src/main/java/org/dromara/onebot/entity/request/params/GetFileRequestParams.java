package org.dromara.onebot.entity.request.params;

import lombok.Data;

/**
 * OneBot请求消息
 */
@Data
public class GetFileRequestParams {

    /**
     * 上传文件的方式，可以为 url、path、data 或扩展的方式
     */
    private String type;


    /**
     * 文件 ID
     */
    private String file_id;

}
