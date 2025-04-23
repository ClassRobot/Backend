package org.dromara.ai.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.onebot.entity.message.ChatMessage.MessageData;
import org.dromara.onebot.socket.OneBotClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/***
 * 聊天接口
 */
@RestController
@RequestMapping("/api/ai/chat")
@RequiredArgsConstructor
public class ChatController {

    private final OneBotClient oneBotClient;

    /**
     * 发送私聊消息
     */
    @PostMapping("/sendPrivateMessage")
    public R sendPrivateMessage(@RequestBody List<MessageData> messages){
        oneBotClient.sendPrivateMessage(LoginHelper.getUserId().toString(), messages);
        return R.ok();
    }

}
