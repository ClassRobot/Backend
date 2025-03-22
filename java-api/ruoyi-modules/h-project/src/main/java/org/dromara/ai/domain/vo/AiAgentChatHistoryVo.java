package org.dromara.ai.domain.vo;

import org.dromara.ai.domain.AiAgentChatHistory;
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
 * 聊天记录视图对象 ai_agent_chat_history
 *
 * @author Lion Li
 * @date 2024-11-07
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = AiAgentChatHistory.class)
public class AiAgentChatHistoryVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ExcelProperty(value = "编号")
    private Long id;

    /**
     * 对话id
     */
    @ExcelProperty(value = "对话id")
    private Long agentChatId;

    /**
     * 智能体id
     */
    @ExcelProperty(value = "智能体id")
    private Long agentId;

    /**
     * 对话身份
     */
    @ExcelProperty(value = "对话身份", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "ai_agent_chat_history_role")
    private String role;

    /**
     * 对话内容
     */
    @ExcelProperty(value = "对话内容")
    private String message;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Long aiSort;


}
