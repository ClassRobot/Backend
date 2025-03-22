package org.dromara.ai.domain.vo;

import org.dromara.ai.domain.AiAgentChat;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 智能体的对话视图对象 ai_agent_chat
 *
 * @author Lion Li
 * @date 2024-11-07
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = AiAgentChat.class)
public class AiAgentChatVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ExcelProperty(value = "编号")
    private Long id;

    /**
     * 智能体id
     */
    @ExcelProperty(value = "智能体id")
    private Long agentId;

    /**
     * 智能体名称
     */
    @ExcelProperty(value = "智能体名称")
    private String agentName;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    private Long uid;

    /**
     * 用户名称
     */
    @ExcelProperty(value = "用户名称")
    private String nickName;

    /**
     * 对话名称
     */
    @ExcelProperty(value = "对话名称")
    private String chatName;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Long aiSort;


}
