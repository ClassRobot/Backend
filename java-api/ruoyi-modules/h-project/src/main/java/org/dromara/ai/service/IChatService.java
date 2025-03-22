package org.dromara.ai.service;

import org.dromara.ai.domain.bo.AiAgentChatBo;
import org.dromara.ai.domain.bo.AiChatMessageBo;
import org.dromara.ai.domain.vo.AiAgentChatHistoryVo;
import org.dromara.ai.domain.vo.AiAgentChatVo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import reactor.core.publisher.Flux;

import java.util.List;

public interface IChatService {

    /**
     * 查询我的对话列表
     */
    TableDataInfo<AiAgentChatVo> listMyChats(AiAgentChatBo bo, PageQuery pageQuery);

    /**
     * 获取我的聊天记录
     */
    TableDataInfo<AiAgentChatHistoryVo> getMyChatMessages(Long chatId, PageQuery pageQuery);

    /***
     * 发送消息给AI
     */
    String sendMessage(AiChatMessageBo message);

    /***
     * 发送消息给AI，流式返回数据
     */
    Flux sendMessageFlux(AiChatMessageBo message);
}
