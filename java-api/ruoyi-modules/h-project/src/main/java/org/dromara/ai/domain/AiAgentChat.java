package org.dromara.ai.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 智能体的对话对象 ai_agent_chat
 *
 * @author Lion Li
 * @date 2024-11-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_agent_chat")
public class AiAgentChat extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 智能体id
     */
    private Long agentId;

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 对话名称
     */
    private String chatName;

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
