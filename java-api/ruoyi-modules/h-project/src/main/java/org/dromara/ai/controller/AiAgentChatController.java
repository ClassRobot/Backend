package org.dromara.ai.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.web.core.BaseController;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.ai.domain.vo.AiAgentChatVo;
import org.dromara.ai.domain.bo.AiAgentChatBo;
import org.dromara.ai.service.IAiAgentChatService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 智能体的对话
 *
 * @author Lion Li
 * @date 2024-11-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/ai/agentChat")
public class AiAgentChatController extends BaseController {

    private final IAiAgentChatService aiAgentChatService;

    /**
     * 查询智能体的对话列表
     */
    @SaCheckPermission("ai:agentChat:list")
    @GetMapping("/list")
    public TableDataInfo<AiAgentChatVo> list(AiAgentChatBo bo, PageQuery pageQuery) {
        return aiAgentChatService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出智能体的对话列表
     */
    @SaCheckPermission("ai:agentChat:export")
    @Log(title = "智能体的对话", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(AiAgentChatBo bo, HttpServletResponse response) {
        List<AiAgentChatVo> list = aiAgentChatService.queryList(bo);
        ExcelUtil.exportExcel(list, "智能体的对话", AiAgentChatVo.class, response);
    }

    /**
     * 获取智能体的对话详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("ai:agentChat:query")
    @GetMapping("/{id}")
    public R<AiAgentChatVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(aiAgentChatService.queryById(id));
    }

    /**
     * 新增智能体的对话
     */
    @SaCheckPermission("ai:agentChat:add")
    @Log(title = "智能体的对话", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody AiAgentChatBo bo) {
        return toAjax(aiAgentChatService.insertByBo(bo));
    }

    /**
     * 修改智能体的对话
     */
    @SaCheckPermission("ai:agentChat:edit")
    @Log(title = "智能体的对话", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody AiAgentChatBo bo) {
        return toAjax(aiAgentChatService.updateByBo(bo));
    }

    /**
     * 删除智能体的对话
     *
     * @param ids 主键串
     */
    @SaCheckPermission("ai:agentChat:remove")
    @Log(title = "智能体的对话", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(aiAgentChatService.deleteWithValidByIds(List.of(ids), true));
    }
}
