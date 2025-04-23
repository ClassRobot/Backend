package org.dromara.ai.handler;

import lombok.RequiredArgsConstructor;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.message.domain.MessagesBo;
import org.dromara.message.service.IMessageService;
import org.dromara.onebot.annotation.RequestMessageMapping;
import org.dromara.onebot.entity.message.ChatMessage;
import org.dromara.onebot.entity.request.RequestMessage;
import org.dromara.onebot.entity.request.ResponseMessage;
import org.dromara.onebot.handler.RequestMessageHandler;
import org.dromara.system.domain.vo.SysUserVo;
import org.dromara.system.service.ISysUserService;
import org.springframework.stereotype.Service;

/**
 * 示例请求消息处理器
 */
@Service
@RequiredArgsConstructor
public class UserRequestMessageHandler implements RequestMessageHandler {

    private final ISysUserService userService;

    /**
     * 处理send_message请求
     */
    @RequestMessageMapping(value = "get_user_info", description = "获取用户信息")
    public ResponseMessage get_user_info(RequestMessage request) {
        String userId = (String) request.getParams().get("user_id");

        SysUserVo sysUserVo = userService.selectUserById(Long.valueOf(userId));

        ResponseMessage.UserData build = ResponseMessage.UserData.builder().user_id(userId)
                .user_name(sysUserVo.getUserName())
                .user_displayname(sysUserVo.getNickName())
                .user_remark(sysUserVo.getRemark() == null ? "" : sysUserVo.getRemark())
                .build();

        // 构建响应消息
        return ResponseMessage.ok(build);
    }

}