package org.dromara.ai.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.dromara.common.mybatis.core.domain.BaseEntity;

import java.io.Serial;

/**
 * AI辅导员的聊天记录对象 ai_message
 *
 * @author Lion Li
 * @date 2025-03-26
 */
@Data
@TableName("public.user")
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AiUser {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id")
    private Long id;


}
