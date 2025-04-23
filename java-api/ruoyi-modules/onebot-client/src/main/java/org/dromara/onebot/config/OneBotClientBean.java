package org.dromara.onebot.config;


import org.dromara.onebot.handler.MessageHandler;
import org.dromara.onebot.handler.RequestMessageHandlerManager;
import org.dromara.onebot.socket.OneBotClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OneBotClientBean {

    @Value("${oneBot.client.url}")
    private String baseurl;

    @Value("${oneBot.client.botId}")
    private Long botId;


    @Bean
    public OneBotClient getOneBotClient(List<MessageHandler> handlers,
                                        RequestMessageHandlerManager requestMessageHandlerManager){

        OneBotConfig config = OneBotConfig.builder()
            .baseurl(baseurl)
            .botId(botId)
            .build();

        OneBotClient client = OneBotClient.create(config);

        requestMessageHandlerManager.init(handlers);

        client.setRequestMessageHandlerManager(requestMessageHandlerManager);

        return client;
    }
}
