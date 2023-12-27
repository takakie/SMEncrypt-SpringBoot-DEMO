package com.gaomu.utils.crypto;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * SM3密码编码
 *
 * @author zhujj
 * @date 2023年12月14日
 */
public class SM3PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        String pwdDigest = rawPassword.toString();
        for (int i = 0; i < 3; i++){
            pwdDigest = SM3Util.passwordDigest(pwdDigest);
        }
        return pwdDigest;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String pwdDigest = rawPassword.toString();
        for (int i = 0; i < 3; i++){
            pwdDigest = SM3Util.passwordDigest(pwdDigest);
        }
        return pwdDigest.equals(encodedPassword);
    }

    public static void main(String[] args){
        //生成数据库密码
        String originalPassword = "admin123";
        System.out.println("originalPassword: " + originalPassword);
        SM3PasswordEncoder sm3PasswordEncoder = new SM3PasswordEncoder();
        String pwdDigest = sm3PasswordEncoder.encode(originalPassword);
        System.out.println("pwdDigest: " + pwdDigest);
    }
}
