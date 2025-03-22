package org.dromara.onebot.client.internal;

import com.baomidou.lock.annotation.Lock4j;
import org.dromara.onebot.client.OneBotClient;
import org.dromara.onebot.client.annotations.SubscribeEvent;
import org.dromara.onebot.client.interfaces.Listener;
import org.dromara.onebot.sdk.event.message.GroupMessageEvent;

/**
 * @Project: onebot-client
 * @Author: cnlimiter
 * @CreateTime: 2024/2/20 9:57
 * @Description:
 */


public class TestHandler implements Listener {

    @SubscribeEvent(internal = true)
    public void msg1(GroupMessageEvent event){
        System.out.println(event);
    }
}
