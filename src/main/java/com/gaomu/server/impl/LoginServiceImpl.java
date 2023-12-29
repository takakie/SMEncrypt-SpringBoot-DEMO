package com.gaomu.server.impl;

import com.gaomu.domain.LoginUser;
import com.gaomu.domain.ResponseResult;
import com.gaomu.domain.User;
import com.gaomu.server.LoginService;
import com.gaomu.utils.JwtUtil;
import com.gaomu.utils.RedisCache;
import com.gaomu.utils.crypto.SM2Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {
    @Value("${secretKey.privateKey}")
    private String privateKey;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        System.out.println("privateKey: " + privateKey);
        //解密SM2登陆密码,SM4密钥
        user.setPassword(SM2Util.sm2Decrypt(user.getPassword(), privateKey));
        user.setSecretKey(SM2Util.sm2Decrypt(user.getSecretKey(), privateKey));
        user.setIv(SM2Util.sm2Decrypt(user.getIv(), privateKey));
        //Authentication authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //如果认证没通过，给出对应提示
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("authenticate 为空，登陆失败！");
        }
        //认证通过，使用userid生成jwt,jwt存入ResponseResult 返回
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        loginUser.getUser().setSecretKey(user.getSecretKey());
        loginUser.getUser().setIv(user.getIv());
        String loginUserKey = JwtUtil.getUUID();
        String userid = loginUser.getUser().getId().toString();
//        String jwt = JwtUtil.createJWT(userid);
        String jwt = JwtUtil.createJWT(loginUserKey, userid, null);
        Map<String, String > map = new HashMap<>();
        map.put("token", jwt);
        //把完整的用户信息存入redis userid作为key
        redisCache.setCacheObject("LOGIN_USER_KEY:"+loginUserKey, loginUser);
        return new ResponseResult(200, "登陆成功", map);
    }

    @Override
    public ResponseResult logout() {
        //获取SecurityContextHolder的用户id  每个请求的SecurityContextHolder都是独立的
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
        //删除redis当中的值
        redisCache.deleteObject("login:" + userid);
        return new ResponseResult(200, "注销成功");
    }

}
