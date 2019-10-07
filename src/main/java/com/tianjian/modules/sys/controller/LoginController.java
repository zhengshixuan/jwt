package com.tianjian.modules.sys.controller;


import com.tianjian.modules.sys.entity.User;
import com.tianjian.common.utils.ReJson;
import com.tianjian.modules.sys.service.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
public class LoginController {
    @Autowired
    private ILoginService loginService;

    /**
     * 登录
     *
     * @param user
     * @return
     */
    @PostMapping("/login")
    public ReJson login(@RequestBody User user) {
        ReJson re = loginService.login(user);
        return re;
    }

    @RequestMapping("/test")
    public String test() {
        return "测试拦截的方法能进来不?";
    }

    @RequestMapping("/swagger/test")
    public Object swagger(@RequestBody User user) {
        return "测试放行的方法能进来不?";
    }
}
