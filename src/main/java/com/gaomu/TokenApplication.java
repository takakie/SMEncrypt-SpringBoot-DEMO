package com.gaomu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@MapperScan("com.gaomu.mapper")
public class TokenApplication {
    public static void main(String[] args){
        SpringApplication.run(TokenApplication.class, args);
    }
}
