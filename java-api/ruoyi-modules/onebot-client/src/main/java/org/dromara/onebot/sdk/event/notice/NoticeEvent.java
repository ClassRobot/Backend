package org.dromara.onebot.sdk.event.notice;

import org.dromara.onebot.sdk.event.Event;
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
public class NoticeEvent extends Event {

    @SerializedName( "notice_type")
    private String noticeType;

    @SerializedName( "user_id")
    private long userId;

}
