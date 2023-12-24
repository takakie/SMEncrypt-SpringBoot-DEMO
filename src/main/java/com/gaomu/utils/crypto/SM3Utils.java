package com.gaomu.utils.crypto;

import cn.hutool.crypto.SmUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.digest.SM3;

import java.nio.charset.StandardCharsets;


public class SM3Utils {

    public static String salt = "my_salt";

    public static String timeSeparator = "$";


    public static String salt_digest(String data){
        SM3 sm3 = SmUtil.sm3WithSalt(salt.getBytes(StandardCharsets.UTF_8));
        byte[] hash = sm3.digest(data.getBytes());
        return HexUtil.encodeHexStr(hash);
    }

    public static boolean checkSign(String data, String sign, String timestamp){
        byte[] checkSaltBytes = (timeSeparator + timestamp).getBytes(StandardCharsets.UTF_8);
        SM3 sm3 = SmUtil.sm3WithSalt(checkSaltBytes);
        byte[] hash = sm3.digest(data.getBytes());
        String enSign = HexUtil.encodeHexStr(hash);

        System.out.println("data: " + data);
        System.out.println("timeSeparator + timestamp: " + timeSeparator + timestamp);
        System.out.println("enSign: " + enSign);
        System.out.println("sign: " + sign);
        if (enSign.equals(sign)){
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        String content = "admin123";
        String hexUtil = salt_digest(content);
        System.out.println(hexUtil);
    }

}
