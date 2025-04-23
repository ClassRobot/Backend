package org.dromara.onebot.entity.message;

import lombok.Getter;
import org.dromara.onebot.socket.OneBotClient;

import java.net.URI;

@Getter
public enum ChatMessageTypeEnum {

    /**
     * 私发消息
     */
    PRIVATE("private");

    private String value;

    ChatMessageTypeEnum(String type) {
        this.value = type;
    }
}
