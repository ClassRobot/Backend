package org.dromara.onebot.socket;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dromara.onebot.socket.config.OneBotConfig;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class OneBotClient extends WebSocketClient {

    private OneBotConfig config;
    private static Logger logger;

    public OneBotClient(URI serverUri) {
        super(serverUri);
    }

    public OneBotClient(String serverUri) {
        super(URI.create(serverUri));
    }

    public static OneBotClient create(OneBotConfig config){
        logger = LogManager.getLogger("OneBot Client");

        OneBotClient client = new OneBotClient(config.getBaseurl());

        client.config = config;

        client.connect();

        return client;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        logger.info("OneBotClient {}链接成功 机器人ID：{}", config.getBaseurl(), config.getBotId());


    }

    @Override
    public void onMessage(String s) {
        logger.info("OneBotClient 收到消息：{}", s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        logger.info("OneBotClient {}链接关闭 ：{} : {}",i, s, b);
    }

    @Override
    public void onError(Exception e) {
        logger.info("OneBotClient报错 ：{}", e);
    }
}
