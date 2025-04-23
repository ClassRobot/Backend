package org.dromara.message.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.ai.domain.vo.AiMessageVo;
import org.dromara.common.core.domain.R;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.message.domain.MessagesBo;
import org.dromara.message.service.IMessageService;
import org.dromara.onebot.socket.OneBotClient;
import org.dromara.system.domain.vo.SysUserVo;
import org.dromara.system.service.ISysUserService;
import org.springframework.web.bind.annotation.*;


/***
 * 聊天接口
 */
@RestController
@RequestMapping("/api/ai/user")
@RequiredArgsConstructor
public class UserController {

    private final ISysUserService iSysUserService;

    /**
     * 获取个人信息
     */
    @GetMapping("/getUserInfo")
    public R getUserInfo(){
        SysUserVo sysUserVo = iSysUserService.selectUserById(LoginHelper.getUserId());
        return R.ok(sysUserVo);
    }


}
