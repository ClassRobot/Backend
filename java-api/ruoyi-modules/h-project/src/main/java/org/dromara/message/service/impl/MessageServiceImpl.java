package org.dromara.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.ai.domain.AiMessage;
import org.dromara.ai.domain.bo.AiMessageBo;
import org.dromara.ai.domain.vo.AiMessageVo;
import org.dromara.ai.mapper.AiMessageMapper;
import org.dromara.ai.service.IAiMessageService;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.common.websocket.utils.WebSocketUtils;
import org.dromara.message.domain.MessagesBo;
import org.dromara.message.service.IMessageService;
import org.dromara.onebot.entity.message.ChatMessageTypeEnum;
import org.dromara.onebot.utils.MessageUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * AI辅导员的聊天记录Service业务层处理
 *
 * @author Lion Li
 * @date 2025-03-26
 */
@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements IMessageService {

    private final AiMessageMapper baseMapper;
    private final IAiMessageService aiMessageService;


    @Override
    public AiMessage sendPrivateMessage(MessagesBo messages, Long userid) {

        AiMessage build = AiMessage.builder()
                .altMssage(MessageUtils.getAltMessageByData(messages.getMessages()))
                .data(JsonUtils.toJsonString(messages.getMessages()))
                .detailType(ChatMessageTypeEnum.PRIVATE.getValue())
                .receiveId(messages.getReceiveId())
                .userId(userid)
                .build();

        aiMessageService.save(build);

        String jsonString = JsonUtils.toJsonString(build);

        WebSocketUtils.sendMessage(userid, jsonString);
        WebSocketUtils.sendMessage(build.getReceiveId(), jsonString);

        return build;
    }

    @Override
    public TableDataInfo<AiMessageVo> queryPageList(PageQuery pageQuery) {

        Long userId = LoginHelper.getUserId();

        LambdaQueryWrapper<AiMessage> lqw = Wrappers.<AiMessage>lambdaQuery()
            .eq(AiMessage::getUserId, userId)
            .or()
            .eq(AiMessage::getReceiveId, userId)
            .orderByDesc(AiMessage::getCreateTime);

        Page<AiMessageVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

}
