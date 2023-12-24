package com.gaomu.handler;

import com.alibaba.fastjson.JSON;
import com.gaomu.domain.ResponseResult;
import com.gaomu.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        //处理认证异常
        ResponseResult result = new ResponseResult(HttpStatus.FORBIDDEN.value(), "登陆失败!");
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response, json);
    }
}
