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
import org.dromara.ai.domain.vo.AiAgentChatHistoryVo;
import org.dromara.ai.domain.bo.AiAgentChatHistoryBo;
import org.dromara.ai.service.IAiAgentChatHistoryService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 聊天记录
 *
 * @author Lion Li
 * @date 2024-11-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/ai/agentChatHistory")
public class AiAgentChatHistoryController extends BaseController {

    private final IAiAgentChatHistoryService aiAgentChatHistoryService;

    /**
     * 查询聊天记录列表
     */
    @SaCheckPermission("ai:agentChatHistory:list")
    @GetMapping("/list")
    public TableDataInfo<AiAgentChatHistoryVo> list(AiAgentChatHistoryBo bo, PageQuery pageQuery) {
        return aiAgentChatHistoryService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出聊天记录列表
     */
    @SaCheckPermission("ai:agentChatHistory:export")
    @Log(title = "聊天记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(AiAgentChatHistoryBo bo, HttpServletResponse response) {
        List<AiAgentChatHistoryVo> list = aiAgentChatHistoryService.queryList(bo);
        ExcelUtil.exportExcel(list, "聊天记录", AiAgentChatHistoryVo.class, response);
    }

    /**
     * 获取聊天记录详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("ai:agentChatHistory:query")
    @GetMapping("/{id}")
    public R<AiAgentChatHistoryVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(aiAgentChatHistoryService.queryById(id));
    }

    /**
     * 新增聊天记录
     */
    @SaCheckPermission("ai:agentChatHistory:add")
    @Log(title = "聊天记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody AiAgentChatHistoryBo bo) {
        return toAjax(aiAgentChatHistoryService.insertByBo(bo));
    }

    /**
     * 修改聊天记录
     */
    @SaCheckPermission("ai:agentChatHistory:edit")
    @Log(title = "聊天记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody AiAgentChatHistoryBo bo) {
        return toAjax(aiAgentChatHistoryService.updateByBo(bo));
    }

    /**
     * 删除聊天记录
     *
     * @param ids 主键串
     */
    @SaCheckPermission("ai:agentChatHistory:remove")
    @Log(title = "聊天记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(aiAgentChatHistoryService.deleteWithValidByIds(List.of(ids), true));
    }
}
