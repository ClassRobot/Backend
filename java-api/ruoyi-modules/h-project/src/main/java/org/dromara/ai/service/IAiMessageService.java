package org.dromara.ai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.dromara.ai.domain.AiMessage;
import org.dromara.ai.domain.vo.AiMessageVo;
import org.dromara.ai.domain.bo.AiMessageBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * AI辅导员的聊天记录Service接口
 *
 * @author Lion Li
 * @date 2025-03-26
 */
public interface IAiMessageService extends IService<AiMessage> {

    /**
     * 查询AI辅导员的聊天记录
     *
     * @param id 主键
     * @return AI辅导员的聊天记录
     */
    AiMessageVo queryById(Long id);

    /**
     * 分页查询AI辅导员的聊天记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return AI辅导员的聊天记录分页列表
     */
    TableDataInfo<AiMessageVo> queryPageList(AiMessageBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的AI辅导员的聊天记录列表
     *
     * @param bo 查询条件
     * @return AI辅导员的聊天记录列表
     */
    List<AiMessageVo> queryList(AiMessageBo bo);

    /**
     * 新增AI辅导员的聊天记录
     *
     * @param bo AI辅导员的聊天记录
     * @return 是否新增成功
     */
    Boolean insertByBo(AiMessageBo bo);

    /**
     * 修改AI辅导员的聊天记录
     *
     * @param bo AI辅导员的聊天记录
     * @return 是否修改成功
     */
    Boolean updateByBo(AiMessageBo bo);

    /**
     * 校验并批量删除AI辅导员的聊天记录信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
