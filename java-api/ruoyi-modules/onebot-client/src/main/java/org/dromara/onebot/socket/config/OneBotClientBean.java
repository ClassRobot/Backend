package org.dromara.onebot.socket.config;


import org.dromara.onebot.socket.OneBotClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OneBotClientBean {

    @Value("${oneBot.client.url}")
    private String baseurl;

    @Value("${oneBot.client.botId}")
    private Long botId;

//    @Bean
//    public OneBotClient getOneBotClient(){
//
//        OneBotConfig config = OneBotConfig.builder()
//            .baseurl(baseurl)
//            .botId(botId)
//            .build();
//
//        OneBotClient client = OneBotClient.create(config);
//
//        return client;
//    }
}
