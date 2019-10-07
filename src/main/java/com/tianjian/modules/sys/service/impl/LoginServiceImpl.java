package com.tianjian.modules.sys.service.impl;

import com.tianjian.common.utils.RedisUtils;
import com.tianjian.common.utils.SecurityConsts;
import com.tianjian.modules.sys.entity.User;
import com.tianjian.modules.sys.service.ILoginService;
import com.tianjian.shiro.jwt.JwtUtil;
import com.tianjian.common.utils.ReJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
@Service
@Transactional
public class LoginServiceImpl implements ILoginService {

    @Override
    public ReJson login(User user) {
        //当前时间戳
        String currentTimeMillis = String.valueOf(System.currentTimeMillis());
        if ("zhengsx".equals(user.getUsername()) && "1".equals(user.getPassword())) {
            String sign = JwtUtil.sign(user.getUsername(), user.getPassword(),currentTimeMillis);
            Map<String, Object> map = new HashMap<>();
            map.put("token", sign);
            RedisUtils.set(SecurityConsts.USERNAME_PREFIX + user.getUsername(), currentTimeMillis);
            return ReJson.ok("登录成功").put("token", sign);
        } else {
            return ReJson.error("登录失败");
        }
    }
}
