package org.dromara.onebot.sdk.event.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * Created on 2022/7/8.
 *
 * @author cnlimiter
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FriendAddRequestEvent extends RequestEvent {
}
