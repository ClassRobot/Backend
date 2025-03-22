package org.dromara.ai.domain.bo;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiChatMessageBo {

    // 模型类型
    private String modelType;

    // 模型
    private String model;

    // 智能体id
    @NotNull(message = "智能体id不能为空", groups = AddGroup.class)
    private Long agentId;

    // 消息
    @NotNull(message = "消息不能为空", groups = AddGroup.class)
    private String message;

    // 对话id
    @NotNull(message = "对话id不能为空", groups = AddGroup.class)
    private Long chatId;

}
