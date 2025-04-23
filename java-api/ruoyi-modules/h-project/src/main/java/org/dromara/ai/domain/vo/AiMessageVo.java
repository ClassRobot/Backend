package org.dromara.ai.domain.vo;

import org.dromara.ai.domain.AiMessage;
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
 * AI辅导员的聊天记录视图对象 ai_message
 *
 * @author Lion Li
 * @date 2025-03-26
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = AiMessage.class)
public class AiMessageVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ExcelProperty(value = "编号")
    private Long id;

    /**
     * 发送者id
     */
    @ExcelProperty(value = "发送者id")
    private Long userId;

    /**
     * 接收者id（群聊则是群聊id）
     */
    @ExcelProperty(value = "接收者id", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "群=聊则是群聊id")
    private Long receiveId;

    /**
     * 消息类型
     */
    @ExcelProperty(value = "消息类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "pm_amount_record_type")
    private String detailType;

    /**
     * 消息的json数据
     */
    @ExcelProperty(value = "消息的json数据")
    private String data;

    /**
     * 消息的文本形式
     */
    @ExcelProperty(value = "消息的文本形式")
    private String altMssage;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 是否已读
     */
    @ExcelProperty(value = "是否已读")
    private Integer readFlag;


}
