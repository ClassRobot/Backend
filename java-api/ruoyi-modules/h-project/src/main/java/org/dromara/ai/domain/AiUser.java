package org.dromara.ai.domain;

import lombok.*;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;

/**
 * AI辅导员的聊天记录对象 ai_message
 *
 * @author Lion Li
 * @date 2025-03-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_message")
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AiMessage extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 发送者id
     */
    private Long userId;

    /**
     * 接收者id（群聊则是群聊id）
     */
    private Long receiveId;

    /**
     * 消息类型
     */
    private String detailType;

    /**
     * 消息的json数据
     */
    private String data;

    /**
     * 消息的文本形式
     */
    private String altMssage;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否已读
     */
    private Long readFlag;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private Long delFlag;


}
