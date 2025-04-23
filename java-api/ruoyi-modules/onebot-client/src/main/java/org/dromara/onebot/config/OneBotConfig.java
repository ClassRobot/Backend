package org.dromara.onebot.socket.config;

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
}
