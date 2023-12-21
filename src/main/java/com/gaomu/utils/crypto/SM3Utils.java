package com.gaomu.utils.crypto;

import cn.hutool.crypto.SmUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.digest.SM3;

import java.nio.charset.StandardCharsets;


public class SM3Utils {

    public static String salt = "my_salt";

    public static String salt_digest(String data){
        SM3 sm3 = SmUtil.sm3WithSalt(salt.getBytes(StandardCharsets.UTF_8));
        byte[] hash = sm3.digest(data.getBytes());
        return HexUtil.encodeHexStr(hash);
    }
    public static void main(String[] args) {
        String content = "admin123";
        String hexUtil = salt_digest(content);
        System.out.println(hexUtil);
    }

}
