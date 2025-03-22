package org.dromara.onebot.client.interfaces;


import org.dromara.onebot.sdk.event.Event;

/**
 * @Project: onebot-client
 * @Author: cnlimiter
 * @CreateTime: 2024/1/26 22:32
 * @Description:
 */

public interface EventsBus {
    void callEvent(Event event);

    void register(Listener listener);

    void unregister(Listener listener);
}
