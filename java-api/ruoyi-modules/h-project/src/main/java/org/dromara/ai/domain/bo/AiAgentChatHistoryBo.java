package org.dromara.ai.domain.bo;

import lombok.*;
import org.dromara.ai.domain.AiAgentChatHistory;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.*;

/**
 * 聊天记录业务对象 ai_agent_chat_history
 *
 * @author Lion Li
 * @date 2024-11-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = AiAgentChatHistory.class, reverseConvertGenerate = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiAgentChatHistoryBo extends BaseEntity {

    /**
     * 编号
     */
    @NotNull(message = "编号不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 对话id
     */
    private Long agentChatId;

    /**
     * 智能体id
     */
    private Long agentId;

    /**
     * 对话身份
     */
    @NotBlank(message = "对话身份不能为空", groups = { AddGroup.class, EditGroup.class })
    private String role;

    /**
     * 对话内容
     */
    @NotBlank(message = "对话内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String message;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long aiSort;


}
