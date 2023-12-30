package com.gaomu.filter;


import com.gaomu.utils.crypto.SM3Util;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Component
public class SignatureHeaderFilter extends OncePerRequestFilter {
    @Value("${timeOut}")
    private long TIMEOUT;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException {
        StringBuilder data = new StringBuilder(request.getRequestURI());
        String timestamp = request.getHeader("Timestamp");
        String sign = request.getHeader("Sign");
        long timestampThreshold = System.currentTimeMillis();
        if (sign == null || timestamp == null) {
            throw new ServletException("签名不存在");
            //请求体时间戳需在超时时间范围内，且不能是未来时间
        } else if (Long.parseLong(timestamp) < (timestampThreshold - TIMEOUT * 1000) || Long.parseLong(timestamp) > timestampThreshold){
            throw new ServletException("请求超时");
        } else {
            // 获取 GET 参数
            if ("GET".equalsIgnoreCase(request.getMethod()) && Objects.nonNull(request.getParameterMap())) {
                data.append("?");
                Map<String, String[]> parameterMap = request.getParameterMap();
                for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                    String name = entry.getKey();
                    String[] values = entry.getValue();
                    data.append(name).append('=').append(values[0]).append('&');
                }
                data.deleteCharAt(data.length() - 1);

            } else if ("POST".equalsIgnoreCase(request.getMethod()) && Objects.nonNull(request.getReader())){
                //如果需要多次调用Reader，请使用包装类 HttpServletRequestWrapper
                StringBuilder sb = new StringBuilder();
                String line;
                try (BufferedReader reader = request.getReader()) {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                }
                data.append('?').append(sb);
            }
        }
        if (SM3Util.checkSign(data.toString(), sign,  timestamp)){
            System.out.println("签名校验通过-OK");
        }
        else {
            throw new ServletException("签名校验失败");
        }
        filterChain.doFilter(request, response);
    }
}
