package org.dromara.onebot.entity.request.params;

import lombok.Data;

/**
 * OneBot请求消息
 */
@Data
public class UploadFileRequestParams {

    /**
     * 上传文件的方式，可以为 url、path、data 或扩展的方式
     */
    private String type;

    /**
     * 文件名，如 foo.jpg
     */
    private String name;

    /**
     * 文件 URL，当 type 为 url 时必须传入，OneBot 实现必须支持以 HTTP(S) 协议从此 URL 下载要上传的文件
     */
    private String url;

    /**
     * 	下载 URL 时需要添加的 HTTP 请求头，可选传入，当 type 为 url 时 OneBot 实现必须在请求 URL 时加上这些请求头
     */
    private String headers;

    /**
     * 文件路径，当 type 为 path 时必须传入，OneBot 实现必须能从此路径访问要上传的文件
     */
    private String path;

    /**
     * 	文件数据，当 type 为 data 时必须传入
     */
    private String data;

    /**
     * 文件数据（原始二进制）的 SHA256 校验和，全小写，可选传入
     */
    private String sha256;

}
