package com.gaomu.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gaomu.domain.LoginUser;
import com.gaomu.domain.User;
import com.gaomu.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
        queryWrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(queryWrapper);
        //如果没有查询到用户就行抛出异常
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名或密码错误!");
        }
        System.out.println("-------------------" + username + "--登录成功------------------------");
        //查询对应的权限信息
        //把数据封装成UserDetails返回
        return new LoginUser(user);
    }
}
