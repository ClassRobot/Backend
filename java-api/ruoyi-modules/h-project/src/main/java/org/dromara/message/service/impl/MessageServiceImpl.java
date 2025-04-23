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
import org.dromara.ai.domain.bo.AiMessageBo;
import org.dromara.ai.domain.vo.AiMessageVo;
import org.dromara.ai.domain.AiMessage;
import org.dromara.ai.mapper.AiMessageMapper;
import org.dromara.ai.service.IAiMessageService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * AI辅导员的聊天记录Service业务层处理
 *
 * @author Lion Li
 * @date 2025-03-26
 */
@RequiredArgsConstructor
@Service
public class AiMessageServiceImpl implements IAiMessageService {

    private final AiMessageMapper baseMapper;

    /**
     * 查询AI辅导员的聊天记录
     *
     * @param id 主键
     * @return AI辅导员的聊天记录
     */
    @Override
    public AiMessageVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询AI辅导员的聊天记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return AI辅导员的聊天记录分页列表
     */
    @Override
    public TableDataInfo<AiMessageVo> queryPageList(AiMessageBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<AiMessage> lqw = buildQueryWrapper(bo);
        Page<AiMessageVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的AI辅导员的聊天记录列表
     *
     * @param bo 查询条件
     * @return AI辅导员的聊天记录列表
     */
    @Override
    public List<AiMessageVo> queryList(AiMessageBo bo) {
        LambdaQueryWrapper<AiMessage> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<AiMessage> buildQueryWrapper(AiMessageBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<AiMessage> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserId() != null, AiMessage::getUserId, bo.getUserId());
        lqw.eq(bo.getReceiveId() != null, AiMessage::getReceiveId, bo.getReceiveId());
        lqw.eq(StringUtils.isNotBlank(bo.getDetailType()), AiMessage::getDetailType, bo.getDetailType());
        lqw.eq(StringUtils.isNotBlank(bo.getData()), AiMessage::getData, bo.getData());
        lqw.eq(StringUtils.isNotBlank(bo.getAltMssage()), AiMessage::getAltMssage, bo.getAltMssage());
        lqw.eq(bo.getReadFlag() != null, AiMessage::getReadFlag, bo.getReadFlag());
        return lqw;
    }

    /**
     * 新增AI辅导员的聊天记录
     *
     * @param bo AI辅导员的聊天记录
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(AiMessageBo bo) {
        AiMessage add = MapstructUtils.convert(bo, AiMessage.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改AI辅导员的聊天记录
     *
     * @param bo AI辅导员的聊天记录
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(AiMessageBo bo) {
        AiMessage update = MapstructUtils.convert(bo, AiMessage.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(AiMessage entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除AI辅导员的聊天记录信息
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
