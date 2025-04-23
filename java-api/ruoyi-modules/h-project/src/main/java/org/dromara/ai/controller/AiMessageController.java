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
import org.dromara.ai.domain.vo.AiMessageVo;
import org.dromara.ai.domain.bo.AiMessageBo;
import org.dromara.ai.service.IAiMessageService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * AI辅导员的聊天记录
 *
 * @author Lion Li
 * @date 2025-03-26
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/message")
public class AiMessageController extends BaseController {

    private final IAiMessageService aiMessageService;

    /**
     * 查询AI辅导员的聊天记录列表
     */
    @SaCheckPermission("ai:message:list")
    @GetMapping("/list")
    public TableDataInfo<AiMessageVo> list(AiMessageBo bo, PageQuery pageQuery) {
        return aiMessageService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出AI辅导员的聊天记录列表
     */
    @SaCheckPermission("ai:message:export")
    @Log(title = "AI辅导员的聊天记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(AiMessageBo bo, HttpServletResponse response) {
        List<AiMessageVo> list = aiMessageService.queryList(bo);
        ExcelUtil.exportExcel(list, "AI辅导员的聊天记录", AiMessageVo.class, response);
    }

    /**
     * 获取AI辅导员的聊天记录详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("ai:message:query")
    @GetMapping("/{id}")
    public R<AiMessageVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(aiMessageService.queryById(id));
    }

    /**
     * 新增AI辅导员的聊天记录
     */
    @SaCheckPermission("ai:message:add")
    @Log(title = "AI辅导员的聊天记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody AiMessageBo bo) {
        return toAjax(aiMessageService.insertByBo(bo));
    }

    /**
     * 修改AI辅导员的聊天记录
     */
    @SaCheckPermission("ai:message:edit")
    @Log(title = "AI辅导员的聊天记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody AiMessageBo bo) {
        return toAjax(aiMessageService.updateByBo(bo));
    }

    /**
     * 删除AI辅导员的聊天记录
     *
     * @param ids 主键串
     */
    @SaCheckPermission("ai:message:remove")
    @Log(title = "AI辅导员的聊天记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(aiMessageService.deleteWithValidByIds(List.of(ids), true));
    }
}
