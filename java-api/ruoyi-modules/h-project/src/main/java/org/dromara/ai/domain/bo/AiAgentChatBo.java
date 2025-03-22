package org.dromara.ai.domain.bo;

import lombok.*;
import org.dromara.ai.domain.AiAgentChat;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.*;

/**
 * 智能体的对话业务对象 ai_agent_chat
 *
 * @author Lion Li
 * @date 2024-11-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = AiAgentChat.class, reverseConvertGenerate = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiAgentChatBo extends BaseEntity {

    /**
     * 编号
     */
    @NotNull(message = "编号不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 智能体id
     */
    @NotNull(message = "智能体id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long agentId;

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long uid;

    /**
     * 对话名称
     */
    @NotBlank(message = "对话名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String chatName;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long aiSort;


}
