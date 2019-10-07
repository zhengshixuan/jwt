package com.tianjian.shiro.jwt;

import com.tianjian.modules.sys.entity.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

@Component
public class JwtRealm extends AuthorizingRealm {


    /**
     * 必须重写此方法，不然Shiro会报错
     *
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 授权(验证权限时调用)
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = JwtUtil.getClaim(principals.toString(),"username");
//        User user = userService.findByUserName(username);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();


        return simpleAuthorizationInfo;

    }

    /**
     * 认证,登录时调用
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        // 获取token
        String token = (String) authenticationToken.getPrincipal();

        //用户名
        String username = null;
        try {
            //这里工具类没有处理空指针等异常这里处理一下(这里处理科学一些)
            username = JwtUtil.getClaim(token,"username");
        } catch (Exception e) {
            throw new AuthenticationException("heard的token拼写错误或者值为空");
        }

        if (username == null) {
            throw new AuthenticationException("token无效");
        }
        // 查询用户信息,判断用户是否存在
        User user = new User();
        user.setUsername("zhengsx");
        user.setPassword("1");
        if (user == null) {
            throw new AuthenticationException("用户不存在!");
        }
        if (!JwtUtil.verify(token, username, user.getPassword())) {
            throw new AuthenticationException("用户名或密码错误(token无效或者与登录者不匹配)!");
        }
        return new SimpleAuthenticationInfo(token, token, "JwtRealm");
    }
}
