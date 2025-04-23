package org.dromara.onebot.entity.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SelfMessage {

    /**
     * 机器人平台
     */
    private String platform;

    /**
     * 机器人ID
     */
    private String user_id;
}
