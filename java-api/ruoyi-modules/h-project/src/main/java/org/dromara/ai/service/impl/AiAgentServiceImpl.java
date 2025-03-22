package org.dromara.ai.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.dromara.ai.domain.bo.AiAgentBo;
import org.dromara.ai.domain.vo.AiAgentVo;
import org.dromara.ai.domain.AiAgent;
import org.dromara.ai.mapper.AiAgentMapper;
import org.dromara.ai.service.IAiAgentService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 智能体Service业务层处理
 *
 * @author Lion Li
 * @date 2024-11-07
 */
@RequiredArgsConstructor
@Service
public class AiAgentServiceImpl implements IAiAgentService {

    private final AiAgentMapper baseMapper;

    /**
     * 查询智能体
     *
     * @param id 主键
     * @return 智能体
     */
    @Override
    public AiAgentVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询智能体列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 智能体分页列表
     */
    @Override
    public TableDataInfo<AiAgentVo> queryPageList(AiAgentBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<AiAgent> lqw = buildQueryWrapper(bo);
        Page<AiAgentVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的智能体列表
     *
     * @param bo 查询条件
     * @return 智能体列表
     */
    @Override
    public List<AiAgentVo> queryList(AiAgentBo bo) {
        LambdaQueryWrapper<AiAgent> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<AiAgent> buildQueryWrapper(AiAgentBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<AiAgent> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), AiAgent::getName, bo.getName());
        lqw.eq(bo.getStatus() != null, AiAgent::getStatus, bo.getStatus());
        lqw.eq(bo.getAiSort() != null, AiAgent::getAiSort, bo.getAiSort());
        return lqw;
    }

    /**
     * 新增智能体
     *
     * @param bo 智能体
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(AiAgentBo bo) {
        AiAgent add = MapstructUtils.convert(bo, AiAgent.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改智能体
     *
     * @param bo 智能体
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(AiAgentBo bo) {
        AiAgent update = MapstructUtils.convert(bo, AiAgent.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(AiAgent entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除智能体信息
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
