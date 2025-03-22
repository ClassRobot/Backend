package org.dromara.ai.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 聊天记录对象 ai_agent_chat_history
 *
 * @author Lion Li
 * @date 2024-11-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_agent_chat_history")
public class AiAgentChatHistory extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id")
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
    private String role;

    /**
     * 对话内容
     */
    private String message;

    /**
     * 排序
     */
    private Long aiSort;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private Long delFlag;


}
