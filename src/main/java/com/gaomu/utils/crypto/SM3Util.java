package com.gaomu.utils.crypto;

import cn.hutool.crypto.SmUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.digest.SM3;

import java.nio.charset.StandardCharsets;


public class SM3Util {
    //传输盐值请务必与前端保持一致,为保证传输完整性可使用随机盐
    public static String TRANSMIT_SALT = "digest_customized_salt";

    //自定义自己的密码HASH盐,为保证密码保密性可使用随机盐
    public static String USER_PASSWORD_SLAT = "password_salt";

    public static String SEPARATOR = "$";


    public static String passwordDigest(String data){
        SM3 sm3 = SmUtil.sm3WithSalt(USER_PASSWORD_SLAT.getBytes(StandardCharsets.UTF_8));
        byte[] hash = sm3.digest(data.getBytes());
        return HexUtil.encodeHexStr(hash);
    }

    public static boolean checkSign(String data, String sign, String timestamp){
        data += SEPARATOR + timestamp;
        SM3 sm3 = SmUtil.sm3WithSalt((TRANSMIT_SALT + SEPARATOR) .getBytes(StandardCharsets.UTF_8));
        byte[] hash = sm3.digest(data.getBytes());
        String enSign = HexUtil.encodeHexStr(hash);
        System.out.println("originalData: " + data);
        System.out.println("javaSign: " + enSign);
        System.out.println("vueSign: " + sign);
        return enSign.equals(sign);
    }

    public static void main(String[] args) {
        String content = "admin123";
        String hexUtil = passwordDigest(content);
        System.out.println(hexUtil);
    }
}
