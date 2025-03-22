package org.dromara.ai.service.impl;

import org.dromara.ai.domain.AiAgent;
import org.dromara.ai.mapper.AiAgentMapper;
import org.dromara.common.core.domain.dto.UserDTO;
import org.dromara.common.core.service.UserService;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.system.mapper.SysUserMapper;
import org.springframework.stereotype.Service;
import org.dromara.ai.domain.bo.AiAgentChatBo;
import org.dromara.ai.domain.vo.AiAgentChatVo;
import org.dromara.ai.domain.AiAgentChat;
import org.dromara.ai.mapper.AiAgentChatMapper;
import org.dromara.ai.service.IAiAgentChatService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 智能体的对话Service业务层处理
 *
 * @author Lion Li
 * @date 2024-11-07
 */
@RequiredArgsConstructor
@Service
public class AiAgentChatServiceImpl implements IAiAgentChatService {

    private final AiAgentChatMapper baseMapper;
    private final AiAgentMapper aiAgentMapper;
    private final UserService userService;
//    private final SysUserMapper userMapper;

    /**
     * 查询智能体的对话
     *
     * @param id 主键
     * @return 智能体的对话
     */
    @Override
    public AiAgentChatVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询智能体的对话列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 智能体的对话分页列表
     */
    @Override
    public TableDataInfo<AiAgentChatVo> queryPageList(AiAgentChatBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<AiAgentChat> lqw = buildQueryWrapper(bo);
        Page<AiAgentChatVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);

        if(bo.getParams().containsKey("extend")){
            setExtendInfo(result.getRecords());
        }

        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的智能体的对话列表
     *
     * @param bo 查询条件
     * @return 智能体的对话列表
     */
    @Override
    public List<AiAgentChatVo> queryList(AiAgentChatBo bo) {
        LambdaQueryWrapper<AiAgentChat> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<AiAgentChat> buildQueryWrapper(AiAgentChatBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<AiAgentChat> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getAgentId() != null, AiAgentChat::getAgentId, bo.getAgentId());
        lqw.eq(bo.getUid() != null, AiAgentChat::getUid, bo.getUid());
        lqw.like(StringUtils.isNotBlank(bo.getChatName()), AiAgentChat::getChatName, bo.getChatName());
        lqw.eq(bo.getAiSort() != null, AiAgentChat::getAiSort, bo.getAiSort());
        return lqw;
    }

    /**
     * 新增智能体的对话
     *
     * @param bo 智能体的对话
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(AiAgentChatBo bo) {
        AiAgentChat add = MapstructUtils.convert(bo, AiAgentChat.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改智能体的对话
     *
     * @param bo 智能体的对话
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(AiAgentChatBo bo) {
        AiAgentChat update = MapstructUtils.convert(bo, AiAgentChat.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(AiAgentChat entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除智能体的对话信息
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

    public void setExtendInfo(List<AiAgentChatVo> list){
        List<Long> uidList = list.stream().map(item -> item.getUid()).collect(Collectors.toList());
        List<Long> agentIdList = list.stream().map(item -> item.getAgentId()).collect(Collectors.toList());

        HashMap<Long, UserDTO> userDTOS = userService
            .selectListByIds(uidList).stream()
            .collect(Collectors.toMap(UserDTO::getUserId,
                Function.identity(), (k1, k2) -> k1, HashMap::new));

        HashMap<Long, AiAgent> aiAgents = aiAgentMapper
            .selectList(Wrappers.<AiAgent>lambdaQuery()
                .in(AiAgent::getId, agentIdList)
                .select(AiAgent::getId, AiAgent::getName))
            .stream()
            .collect(Collectors.toMap(AiAgent::getId,
                Function.identity(), (k1, k2) -> k1, HashMap::new));

        list.forEach(item->{
            item.setAgentName(aiAgents.get(item.getAgentId()).getName());
            item.setNickName(userDTOS.get(item.getUid()).getNickName());
        });
    }
}
