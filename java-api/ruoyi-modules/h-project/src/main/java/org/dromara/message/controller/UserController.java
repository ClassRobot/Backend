package org.dromara.message.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.dromara.ai.domain.vo.AiMessageVo;
import org.dromara.common.core.domain.R;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.message.domain.MessagesBo;
import org.dromara.message.service.IMessageService;
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
    private final IMessageService messageService;

    /**
     * 发送私聊消息
     */
    @PostMapping("/sendPrivateMessage")
    public R sendPrivateMessage(@RequestBody MessagesBo messages){
        Long userId = LoginHelper.getUserId();
        messageService.sendPrivateMessage(messages, userId);
        if(messages.getReceiveId() == 0){
            oneBotClient.sendPrivateMessage(userId.toString(), messages.getMessages());
        }
        return R.ok();
    }

    /**
     * 获取聊天记录
     */
    @GetMapping("/getMessage")
    public TableDataInfo<AiMessageVo> getMessage(PageQuery pageQuery){
        return messageService.queryPageList(pageQuery);
    }

}
