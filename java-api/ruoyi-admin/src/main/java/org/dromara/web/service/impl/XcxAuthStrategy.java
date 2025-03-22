package org.dromara.web.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.model.XcxLoginBody;
import org.dromara.common.core.domain.model.XcxLoginUser;
import org.dromara.common.core.enums.UserStatus;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.StreamUtils;
import org.dromara.common.core.utils.ValidatorUtils;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.common.tenant.helper.TenantHelper;
import org.dromara.system.domain.SysClient;
import org.dromara.system.domain.bo.SysSocialBo;
import org.dromara.system.domain.bo.SysUserBo;
import org.dromara.system.domain.vo.SysClientVo;
import org.dromara.system.domain.vo.SysSocialVo;
import org.dromara.system.domain.vo.SysUserVo;
import org.dromara.system.mapper.SysUserMapper;
import org.dromara.system.service.ISysSocialService;
import org.dromara.system.service.ISysUserService;
import org.dromara.web.domain.vo.LoginVo;
import org.dromara.web.service.IAuthStrategy;
import org.dromara.web.service.SysLoginService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 小程序认证策略
 *
 * @author Michelle.Chung
 */
@Slf4j
@Service("xcx" + IAuthStrategy.BASE_NAME)
@RequiredArgsConstructor
public class XcxAuthStrategy implements IAuthStrategy {

    public static final String SOURCE = "wx_applet:";

    private final SysLoginService loginService;
    private final ISysUserService sysUserService;
    private final ISysSocialService sysSocialService;

    @Override
    public LoginVo login(String body, SysClientVo client) {
        XcxLoginBody loginBody = JsonUtils.parseObject(body, XcxLoginBody.class);
        ValidatorUtils.validate(loginBody);
        // xcxCode 为 小程序调用 wx.login 授权后获取
        String xcxCode = loginBody.getXcxCode();
        // 多个小程序识别使用
        String appid = loginBody.getAppid();
        String appSecret = "0c8fe611d23f063856cc948c9f753b6e";

        String loginUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + appSecret+ "&js_code=" + xcxCode + "&grant_type=authorization_code";

        HttpResponse response = HttpUtil.createRequest(Method.GET, loginUrl).execute();

        log.info("小程序登录数据：{} .", response.body());

        // 校验 appid + appsrcret + xcxCode 调用登录凭证校验接口 获取 session_key 与 openid
        String openid = new JSONObject(response.body()).getStr("openid");
        // 框架登录不限制从什么表查询 只要最终构建出 LoginUser 即可
        SysUserVo user = loadUserByOpenid(openid);

        // 此处可根据登录用户的数据不同 自行创建 loginUser 属性不够用继承扩展就行了
        XcxLoginUser loginUser = new XcxLoginUser();
        loginUser.setTenantId(user.getTenantId());
        loginUser.setUserId(user.getUserId());
        loginUser.setUsername(user.getUserName());
        loginUser.setNickname(user.getNickName());
        loginUser.setUserType(user.getUserType());
        loginUser.setClientKey(client.getClientKey());
        loginUser.setDeviceType(client.getDeviceType());
        loginUser.setOpenid(openid);

        SaLoginModel model = new SaLoginModel();
        model.setDevice(client.getDeviceType());
        // 自定义分配 不同用户体系 不同 token 授权时间 不设置默认走全局 yml 配置
        // 例如: 后台用户30分钟过期 app用户1天过期
        model.setTimeout(client.getTimeout());
        model.setActiveTimeout(client.getActiveTimeout());
        model.setExtra(LoginHelper.CLIENT_KEY, client.getClientId());
        // 生成token
        LoginHelper.login(loginUser, model);

        LoginVo loginVo = new LoginVo();
        loginVo.setAccessToken(StpUtil.getTokenValue());
        loginVo.setExpireIn(StpUtil.getTokenTimeout());
        loginVo.setClientId(client.getClientId());
        loginVo.setOpenid(openid);
        return loginVo;
    }

    private SysUserVo loadUserByOpenid(String openid) {
        // 使用 openid 查询绑定用户 如未绑定用户 则根据业务自行处理 例如 创建默认用户

        List<SysSocialVo> socialList = sysSocialService.selectByAuthId(SOURCE + openid);

        // 没有，则注册
        if(socialList.size() == 0){

            SysUserBo userBo = SysUserBo.builder()
                .userType(SOURCE)
                .nickName("小程序用户")
                .userName("asfawfdawe")
                .build();

            sysUserService.insertUser(userBo);

            SysSocialBo socialBo = SysSocialBo.builder()
                .source(SOURCE)
                .authId(SOURCE + openid)
                .openId(openid)
                .userId(userBo.getUserId())
                .userName(userBo.getUserName())
                .accessToken(openid)
                .build();
            sysSocialService.insertByBo(socialBo);


            return sysUserService.selectUserById(userBo.getUserId());
        }

        SysUserVo user = sysUserService.selectUserById(socialList.get(0).getUserId());

        if (ObjectUtil.isNull(user)) {
            log.info("登录用户：{} 不存在.", openid);


        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", openid);
            throw new ServiceException("用户已被停用！");
        }
        return user;
    }

}
