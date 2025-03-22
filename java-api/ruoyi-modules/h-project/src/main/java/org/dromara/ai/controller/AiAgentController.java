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
import org.dromara.ai.domain.vo.AiAgentVo;
import org.dromara.ai.domain.bo.AiAgentBo;
import org.dromara.ai.service.IAiAgentService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 智能体
 *
 * @author Lion Li
 * @date 2024-11-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/ai/agent")
public class AiAgentController extends BaseController {

    private final IAiAgentService aiAgentService;

    /**
     * 查询智能体列表
     */
    @SaCheckPermission("ai:agent:list")
    @GetMapping("/list")
    public TableDataInfo<AiAgentVo> list(AiAgentBo bo, PageQuery pageQuery) {
        return aiAgentService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出智能体列表
     */
    @SaCheckPermission("ai:agent:export")
    @Log(title = "智能体", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(AiAgentBo bo, HttpServletResponse response) {
        List<AiAgentVo> list = aiAgentService.queryList(bo);
        ExcelUtil.exportExcel(list, "智能体", AiAgentVo.class, response);
    }

    /**
     * 获取智能体详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("ai:agent:query")
    @GetMapping("/{id}")
    public R<AiAgentVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(aiAgentService.queryById(id));
    }

    /**
     * 新增智能体
     */
    @SaCheckPermission("ai:agent:add")
    @Log(title = "智能体", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody AiAgentBo bo) {
        return toAjax(aiAgentService.insertByBo(bo));
    }

    /**
     * 修改智能体
     */
    @SaCheckPermission("ai:agent:edit")
    @Log(title = "智能体", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody AiAgentBo bo) {
        return toAjax(aiAgentService.updateByBo(bo));
    }

    /**
     * 删除智能体
     *
     * @param ids 主键串
     */
    @SaCheckPermission("ai:agent:remove")
    @Log(title = "智能体", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(aiAgentService.deleteWithValidByIds(List.of(ids), true));
    }
}
