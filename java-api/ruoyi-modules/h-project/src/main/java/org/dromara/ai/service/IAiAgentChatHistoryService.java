package org.dromara.ai.service;

import org.dromara.ai.domain.vo.AiAgentChatHistoryVo;
import org.dromara.ai.domain.bo.AiAgentChatHistoryBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 聊天记录Service接口
 *
 * @author Lion Li
 * @date 2024-11-07
 */
public interface IAiAgentChatHistoryService {

    /**
     * 查询聊天记录
     *
     * @param id 主键
     * @return 聊天记录
     */
    AiAgentChatHistoryVo queryById(Long id);

    /**
     * 分页查询聊天记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 聊天记录分页列表
     */
    TableDataInfo<AiAgentChatHistoryVo> queryPageList(AiAgentChatHistoryBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的聊天记录列表
     *
     * @param bo 查询条件
     * @return 聊天记录列表
     */
    List<AiAgentChatHistoryVo> queryList(AiAgentChatHistoryBo bo);

    /**
     * 根据智能体id查询系统消息列表
     *
     * @param agentId 智能体ID
     * @return 聊天记录列表
     */
    List<AiAgentChatHistoryVo> querySystemOverallList(Long agentId);

    /**
     * 新增聊天记录
     *
     * @param bo 聊天记录
     * @return 是否新增成功
     */
    Boolean insertByBo(AiAgentChatHistoryBo bo);

    /**
     * 修改聊天记录
     *
     * @param bo 聊天记录
     * @return 是否修改成功
     */
    Boolean updateByBo(AiAgentChatHistoryBo bo);

    /**
     * 校验并批量删除聊天记录信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
