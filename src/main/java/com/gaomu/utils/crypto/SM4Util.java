package com.gaomu.utils.crypto;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.symmetric.SM4;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

import org.apache.commons.codec.DecoderException;


public class SM4Util {
    /**
     * sm4 对称加密
     * @param args
     */

    /**
     * cbc加密
     *
     * @param plainTxt
     * @return
     */
    public static String encryptCBC(String plainTxt,String key, String iv) {
        //默认16进制密钥
        SymmetricCrypto sm4 = new SM4(Mode.CBC, Padding.PKCS5Padding, HexUtil.decodeHex(key), HexUtil.decodeHex(iv));
        return sm4.encryptBase64(plainTxt);
    }

    /**
     * cbc解密
     *
     * @param cipherTxt
     * @return
     */
    public static String decryptCBC(String cipherTxt, String key, String iv) {
        //默认16进制密钥
        String plainTxt = "";
        try {
            SymmetricCrypto sm4 = new SM4(Mode.CBC, Padding.PKCS5Padding, HexUtil.decodeHex(key), HexUtil.decodeHex(iv));
            byte[] cipherHex = Base64.decode(cipherTxt.trim());
            plainTxt = sm4.decryptStr(cipherHex, CharsetUtil.CHARSET_UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plainTxt;
    }


    public static void main(String[] args) throws DecoderException {
        //SM4 requires a 128 bit key
        //需要一个长度为16的字符串 16*8=128 bit
        String key = RandomUtil.randomString(16);
        String iv = RandomUtil.randomString(16);
        byte[] bytes = key.getBytes();
        byte[] bytes1 = iv.getBytes();
        System.out.println(Base64.encode(bytes));
        System.out.println(Base64.encode(bytes1));
        System.out.println("生成1个128bit的加密key:"+key);
        System.out.println("生成1个128bit的加密iv:"+iv);
        String str = "admin123";
        String enstr = encryptCBC(str, key, iv);
        System.out.println(enstr);
        String destr = decryptCBC(enstr, key, iv);
        System.out.println(destr);

//        //原文
//        String str = "hello";
//        System.out.println("原文:"+str);
//
//        StopWatch sw = StopWatch.create("q11");
//        sw.start();
//
//        SM4 sm41 = SmUtil.sm4(key.getBytes());
//        //加密为Hex
//        String hexPass = sm41.encryptHex(str);
//        System.out.println("Hex形式的密文:"+hexPass);
//        sw.stop();
//        System.out.println(sw.getLastTaskInfo().getTimeSeconds());
//
//        sw.start();
//        //加密为base64
//        String base64Pass = sm41.encryptBase64(str);
//        System.out.println("base64形式的密文:"+base64Pass);
//        sw.stop();
//        System.out.println(sw.getLastTaskInfo().getTimeSeconds());
//
//        System.out.println("--------------");
//        //hex解密
//        sw.start();
//        String s = sm41.decryptStr(hexPass);
//        sw.stop();
//        System.out.println(s);
//        System.out.println(sw.getLastTaskInfo().getTimeSeconds());
//
//        System.out.println("--------------");
//        //base64解密
//        sw.start();
//        String s2 = sm41.decryptStr(base64Pass);
//        sw.stop();
//        System.out.println(sw.getLastTaskInfo().getTimeSeconds());
//        System.out.println(s2);
    }
}
