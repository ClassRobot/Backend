package org.dromara.ai.service.impl;

import org.dromara.ai.constant.AiMessageRoleConstant;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.dromara.ai.domain.bo.AiAgentChatHistoryBo;
import org.dromara.ai.domain.vo.AiAgentChatHistoryVo;
import org.dromara.ai.domain.AiAgentChatHistory;
import org.dromara.ai.mapper.AiAgentChatHistoryMapper;
import org.dromara.ai.service.IAiAgentChatHistoryService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 聊天记录Service业务层处理
 *
 * @author Lion Li
 * @date 2024-11-07
 */
@RequiredArgsConstructor
@Service
public class AiAgentChatHistoryServiceImpl implements IAiAgentChatHistoryService {

    private final AiAgentChatHistoryMapper baseMapper;

    /**
     * 查询聊天记录
     *
     * @param id 主键
     * @return 聊天记录
     */
    @Override
    public AiAgentChatHistoryVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询聊天记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 聊天记录分页列表
     */
    @Override
    public TableDataInfo<AiAgentChatHistoryVo> queryPageList(AiAgentChatHistoryBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<AiAgentChatHistory> lqw = buildQueryWrapper(bo);
        Page<AiAgentChatHistoryVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的聊天记录列表
     *
     * @param bo 查询条件
     * @return 聊天记录列表
     */
    @Override
    public List<AiAgentChatHistoryVo> queryList(AiAgentChatHistoryBo bo) {
        LambdaQueryWrapper<AiAgentChatHistory> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public List<AiAgentChatHistoryVo> querySystemOverallList(Long agentId) {

        LambdaQueryWrapper<AiAgentChatHistory> lqw = Wrappers.<AiAgentChatHistory>lambdaQuery()
            .eq(AiAgentChatHistory::getRole, AiMessageRoleConstant.SYSTEM)
            .and(n -> {
                n.isNull(AiAgentChatHistory::getAgentChatId)
                .eq(AiAgentChatHistory::getAgentId, agentId)
                .or()
                .isNull(AiAgentChatHistory::getAgentId);
            });

        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<AiAgentChatHistory> buildQueryWrapper(AiAgentChatHistoryBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<AiAgentChatHistory> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getAgentChatId() != null, AiAgentChatHistory::getAgentChatId, bo.getAgentChatId());
        lqw.eq(StringUtils.isNotBlank(bo.getRole()), AiAgentChatHistory::getRole, bo.getRole());
        lqw.eq(StringUtils.isNotBlank(bo.getMessage()), AiAgentChatHistory::getMessage, bo.getMessage());
        lqw.eq(bo.getAiSort() != null, AiAgentChatHistory::getAiSort, bo.getAiSort());
        return lqw;
    }

    /**
     * 新增聊天记录
     *
     * @param bo 聊天记录
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(AiAgentChatHistoryBo bo) {
        AiAgentChatHistory add = MapstructUtils.convert(bo, AiAgentChatHistory.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改聊天记录
     *
     * @param bo 聊天记录
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(AiAgentChatHistoryBo bo) {
        AiAgentChatHistory update = MapstructUtils.convert(bo, AiAgentChatHistory.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(AiAgentChatHistory entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除聊天记录信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }
}
