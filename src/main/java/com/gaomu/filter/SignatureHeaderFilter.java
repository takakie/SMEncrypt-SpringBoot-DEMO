package com.gaomu.filter;


import com.gaomu.utils.crypto.SM3Utils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@Component
public class SignatureHeaderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException {
        StringBuilder data = new StringBuilder(request.getRequestURI());
        String timestamp = request.getHeader("Timestamp");
        String sign = request.getHeader("Sign");
        if (sign == null || timestamp == null) {
            throw new ServletException("签名校验失败");

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
                System.out.println(data);
            } else if ("POST".equalsIgnoreCase(request.getMethod()) && Objects.nonNull(request.getReader())){
                StringBuilder sb = new StringBuilder();
                String line;
                try (BufferedReader reader = request.getReader()) {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                }
                data.append('?').append(sb);

                //TODO
            }
        }
        if (SM3Utils.checkSign(data.toString(), sign,  timestamp)){
            System.out.println("OK");
        }
//        else {
//            throw new ServletException("签名校验失败");
//        }
        filterChain.doFilter(request, response);
    }
}
