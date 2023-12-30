package com.gaomu.config;

import com.gaomu.utils.crypto.SM4YmlEncryptor;
import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${JASYPT_SM4_PASSWORD:defaultValue}")
    private String jasyptSM4Password;


    @Bean("jasyptStringEncryptor")
    public PBEStringEncryptor stringEncryptor() {
        SM4YmlEncryptor sm4YmlEncryptor = new SM4YmlEncryptor();
        sm4YmlEncryptor.setPassword(jasyptSM4Password);  // 设置你的加密密钥
        return sm4YmlEncryptor;
    }
}
