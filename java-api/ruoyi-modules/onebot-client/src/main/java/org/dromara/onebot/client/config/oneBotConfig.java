package org.dromara.onebot.client.config;

import org.dromara.onebot.client.OneBotClient;
import org.dromara.onebot.client.core.BotConfig;
import org.dromara.onebot.client.internal.TestHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class oneBotConfig {

    @Value("${oneBot.client.url}")
    private String baseurl;

    @Value("${oneBot.client.botId}")
    private Long botId;


    @Bean
    public OneBotClient getOneBotClient(){

        BotConfig botConfig = BotConfig.builder()
            .botId(botId)
            .baseurl(baseurl)
            .prefix("/onebot/v12")
            .reconnect(true)
            .interval(5000)
            .reconnectInterval(5)
            .reconnectMaxTimes(3)
            .build();

        OneBotClient onebot = OneBotClient.create(botConfig, new TestHandler())//创建websocket客户端
            .open();//注册事件监听器

        return onebot;
    }

}
