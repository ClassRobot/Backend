package org.dromara.ai.domain.vo;

import org.dromara.ai.domain.AiAgent;
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
 * 智能体视图对象 ai_agent
 *
 * @author Lion Li
 * @date 2024-11-07
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = AiAgent.class)
public class AiAgentVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ExcelProperty(value = "编号")
    private Long id;

    /**
     * 智能体名称
     */
    @ExcelProperty(value = "智能体名称")
    private String name;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "ai_agent_status")
    private Long status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Long aiSort;


}
