package org.dromara.ai.controller;

import cn.dev33.satoken.annotation.SaIgnore;
//import com.mikuac.shiro.common.utils.OneBotMsgUtils;
//import com.mikuac.shiro.core.BotContainer;
import lombok.RequiredArgsConstructor;
import org.dromara.ai.domain.bo.AiAgentChatBo;
import org.dromara.ai.domain.bo.AiChatMessageBo;
import org.dromara.ai.domain.vo.AiAgentChatHistoryVo;
import org.dromara.ai.domain.vo.AiAgentChatVo;
import org.dromara.ai.service.IChatService;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.utils.SpringUtils;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.onebot.client.OneBotClient;
import org.dromara.onebot.client.annotations.SubscribeEvent;
import org.dromara.onebot.client.core.BotConfig;
import org.dromara.onebot.client.interfaces.Listener;
import org.dromara.onebot.sdk.action.misc.ActionData;
import org.dromara.onebot.sdk.event.message.GroupMessageEvent;
import org.dromara.onebot.sdk.response.group.GroupMemberInfoResp;
import org.dromara.onebot.sdk.response.misc.VersionInfoResp;
import org.dromara.onebot.sdk.util.MsgUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;


/***
 * 聊天接口
 */
@SaIgnore
@RestController
@RequestMapping("/api/ai/chat")
@RequiredArgsConstructor
public class ChatController {

    private final IChatService chatService;
    private final OneBotClient oneBotClient;


    /**
     * 获取我的对话
     */
    @GetMapping("/test")
    public R test(){
//        oneBotClient.getBot().sendPrivateMsg(324654, "你好", false);


        oneBotClient.getBot().


        return R.ok();
    }

    /**
     * 获取我的对话
     */
    @GetMapping("/listMyChats")
    public R<TableDataInfo<AiAgentChatVo>> queryPageList(AiAgentChatBo bo, PageQuery pageQuery){
        return R.ok(chatService.listMyChats(bo, pageQuery));
    }


    /**
     * 获取我的聊天记录
     */
    @GetMapping("/getMyChatMessages/{chatId}")
    public R<TableDataInfo<AiAgentChatHistoryVo>> getMyChatMessages(@PathVariable("chatId") Long chatId, PageQuery pageQuery){
        return R.ok(chatService.getMyChatMessages(chatId, pageQuery));
    }


    /***
     * 聊天，流式数据
     * @param message 用户发给ai的消息
     * @return Flux
     */
    @GetMapping(value = "/sendMessageFlux",produces = "text/event-stream;charset=utf-8")
    public Flux sendMessageFlux(@Validated(AddGroup.class) AiChatMessageBo message){
        return chatService.sendMessageFlux(message);
    }

    /***
     * 聊天
     * @param message 用户发给ai的消息
     * @return R<String>
     */
    @GetMapping(value = "/sendMessage")
    public R<String> sendMessage(@Validated(AddGroup.class) AiChatMessageBo message){
        return R.ok(chatService.sendMessage(message));
    }


}
