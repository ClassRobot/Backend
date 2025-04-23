package org.dromara.web.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.system.domain.vo.SysUserVo;

/**
 * 登录验证信息
 *
 * @author Michelle.Chung
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class XcxLoginVo extends LoginVo{

    private SysUserVo userInfo;

}
