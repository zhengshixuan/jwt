package com.tianjian.shiro.filter;

import com.alibaba.fastjson.JSONObject;
import com.tianjian.common.utils.ReJson;
import com.tianjian.common.utils.RedisUtils;
import com.tianjian.common.utils.SecurityConsts;
import com.tianjian.shiro.jwt.JwtToken;
import com.tianjian.shiro.jwt.JwtUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * JwtFilter:jwt过滤器来作为shiro的过滤器
 */
public class JwtFilter extends BasicHttpAuthenticationFilter implements Filter {


    /**
     * 执行登录
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        //从header中获取token
        String token = httpServletRequest.getHeader("token");
        if(StringUtils.isEmpty(token)){
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(JSONObject.toJSON(ReJson.error("token为空")));
            return false;
        }
        JwtToken jwtToken = new JwtToken(token);
        refreshTokenIfNeed(token,response);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        try {
            getSubject(request, response).login(jwtToken);
            // 如果没有抛出异常则代表登入成功，返回true
            return true;
        } catch (AuthenticationException e) {
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(JSONObject.toJSON(ReJson.error(e.toString())));
            return false;
        }

    }

    public boolean refreshTokenIfNeed(String token,ServletResponse response) {
        //当
        Long currentTimeMillis = System.currentTimeMillis();
        String username = JwtUtil.getClaim(token, "username");
        String currentTimeMilies = (String) RedisUtils.get(SecurityConsts.USERNAME_PREFIX + username);
        String tokenMillis = JwtUtil.getClaim(token, SecurityConsts.CURRENT_TIME_MILLIS);
        if (currentTimeMillis - Long.parseLong(tokenMillis) > (30 * 60 * 1000L)) {
            String newToken = JwtUtil.sign(username, "111", String.valueOf(currentTimeMillis));
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setHeader("newToken", newToken);
            httpServletResponse.setHeader("Access-Control-Expose-Headers", "token");
            RedisUtils.set(SecurityConsts.USERNAME_PREFIX + username, String.valueOf(currentTimeMillis));
            return true;
        }


        return false;
    }

    /**
     * 执行登录认证
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        try {
            return executeLogin(request, response);
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 对跨域提供支持
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}
