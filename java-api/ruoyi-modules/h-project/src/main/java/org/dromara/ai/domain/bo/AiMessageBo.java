package org.dromara.ai.domain.bo;

import lombok.*;
import org.dromara.ai.domain.AiMessage;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.*;

/**
 * AI辅导员的聊天记录业务对象 ai_message
 *
 * @author Lion Li
 * @date 2025-03-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = AiMessage.class, reverseConvertGenerate = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiMessageBo extends BaseEntity {

    /**
     * 编号
     */
    @NotNull(message = "编号不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 发送者id
     */
    @NotNull(message = "发送者id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 接收者id（群聊则是群聊id）
     */
    @NotNull(message = "接收者id（群聊则是群聊id）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long receiveId;

    /**
     * 消息类型
     */
    @NotBlank(message = "消息类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String detailType;

    /**
     * 消息的json数据
     */
    @NotBlank(message = "消息的json数据不能为空", groups = { AddGroup.class, EditGroup.class })
    private String data;

    /**
     * 消息的文本形式
     */
    @NotBlank(message = "消息的文本形式不能为空", groups = { AddGroup.class, EditGroup.class })
    private String altMssage;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;

    /**
     * 是否已读
     */
    @NotNull(message = "是否已读不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer readFlag;


}
