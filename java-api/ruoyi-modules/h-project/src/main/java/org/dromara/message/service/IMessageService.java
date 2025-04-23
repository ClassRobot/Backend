package org.dromara.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.dromara.ai.domain.AiMessage;
import org.dromara.ai.domain.bo.AiMessageBo;
import org.dromara.ai.domain.vo.AiMessageVo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.message.domain.MessagesBo;

import java.util.Collection;
import java.util.List;

/**
 * AI辅导员的聊天记录Service接口
 *
 * @author Lion Li
 * @date 2025-03-26
 */
public interface IMessageService {

    /**
     * 发送私聊消息
     * @param messages 消息
     * @param userid 发送者
     * @return AiMessage 消息实体对象
     */
    AiMessage sendPrivateMessage(MessagesBo messages, Long userid);

    /**
     * 查询聊天记录
     * @param pageQuery 分页
     * @return AiMessage 消息实体对象
     */
    TableDataInfo<AiMessageVo> queryPageList(PageQuery pageQuery);
}
