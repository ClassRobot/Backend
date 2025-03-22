package org.dromara.onebot.sdk.event.notice.misc;

import org.dromara.onebot.sdk.event.notice.NoticeEvent;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Created on 2022/7/8.
 *
 * @author cnlimiter
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReceiveOfflineFilesNoticeEvent extends NoticeEvent {

    @SerializedName("file")
    private File file;

    /**
     * 文件对象
     */
    @Data
    public static class File {

        @SerializedName("name")
        private String name;

        @SerializedName("size")
        private long size;

        @SerializedName("url")
        private String url;

    }

}
