package org.dromara.ai.service;

import org.dromara.ai.domain.vo.AiAgentChatVo;
import org.dromara.ai.domain.bo.AiAgentChatBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 智能体的对话Service接口
 *
 * @author Lion Li
 * @date 2024-11-07
 */
public interface IAiAgentChatService {

    /**
     * 查询智能体的对话
     *
     * @param id 主键
     * @return 智能体的对话
     */
    AiAgentChatVo queryById(Long id);

    /**
     * 分页查询智能体的对话列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 智能体的对话分页列表
     */
    TableDataInfo<AiAgentChatVo> queryPageList(AiAgentChatBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的智能体的对话列表
     *
     * @param bo 查询条件
     * @return 智能体的对话列表
     */
    List<AiAgentChatVo> queryList(AiAgentChatBo bo);

    /**
     * 新增智能体的对话
     *
     * @param bo 智能体的对话
     * @return 是否新增成功
     */
    Boolean insertByBo(AiAgentChatBo bo);

    /**
     * 修改智能体的对话
     *
     * @param bo 智能体的对话
     * @return 是否修改成功
     */
    Boolean updateByBo(AiAgentChatBo bo);

    /**
     * 校验并批量删除智能体的对话信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
