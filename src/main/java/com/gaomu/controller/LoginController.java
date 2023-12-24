package com.gaomu.controller;

import com.gaomu.domain.ResponseResult;
import com.gaomu.domain.User;
import com.gaomu.server.LoginService;
import com.gaomu.server.SecretKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private SecretKeyService secretKeyService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        System.out.println(user);
        //登录实现
        return loginService.login(user);
    }
    @RequestMapping("/user/logout")
    public ResponseResult logout(){
        System.out.println("logout successful!!!");
        //登录实现
        return loginService.logout();
    }

    @RequestMapping("/getPublicKey")
    public ResponseResult getPublicKey(){
        return secretKeyService.getPublicKey();
    }
}
