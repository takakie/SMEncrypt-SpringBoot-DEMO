package com.gaomu.filter;


import com.gaomu.utils.crypto.SM3Utils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Component
public class SignatureHeaderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String timestamp = request.getHeader("Timestamp");
        String sign = request.getHeader("Sign");

        if (sign == null || timestamp == null) {
            throw new ServletException("签名校验失败");

        } else {
            // 获取 GET 参数
            if ("GET".equalsIgnoreCase(httpRequest.getMethod())) {
                Map<String, String[]> parameterMap = httpRequest.getParameterMap();
                for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                    String name = entry.getKey();
                    String[] values = entry.getValue();
                    // process name and values
                    System.out.println("key: " + name);
                    System.out.println("values: " + values[0]);
                }
            }

        }
//        String data = "";
//        if (SM3Utils.checkSign(data, sign,  timestamp)){
//            filterChain.doFilter(request, response);
//        } else {
//            throw new ServletException("签名校验失败");
//        }
        filterChain.doFilter(request, response);

    }
}