package com.gaomu.utils.crypto;

import org.springframework.security.crypto.password.PasswordEncoder;
import com.gaomu.utils.crypto.SM3Utils;
/**
 * SM3密码编码
 *
 * @author zhujj
 * @date 2023年12月14日
 */
public class SM3PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return SM3Utils.salt_digest(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String doSM3Encrypt = SM3Utils.salt_digest(rawPassword.toString());
        return doSM3Encrypt.equals(encodedPassword);
    }
}
