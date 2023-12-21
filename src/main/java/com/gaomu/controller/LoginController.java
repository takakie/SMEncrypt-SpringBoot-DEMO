package com.gaomu.controller;

import com.gaomu.domain.ResponseResult;
import com.gaomu.domain.User;
import com.gaomu.server.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        //登录实现
        return loginService.login(user);
    }
}
