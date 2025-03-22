package org.dromara.ai.domain.bo;

import org.dromara.ai.domain.AiAgent;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 智能体业务对象 ai_agent
 *
 * @author Lion Li
 * @date 2024-11-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = AiAgent.class, reverseConvertGenerate = false)
public class AiAgentBo extends BaseEntity {

    /**
     * 编号
     */
    @NotNull(message = "编号不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 智能体名称
     */
    @NotBlank(message = "智能体名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 状态
     */
    @NotNull(message = "状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long aiSort;


}
