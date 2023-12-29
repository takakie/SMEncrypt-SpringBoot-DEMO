package com.gaomu.utils.crypto;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.symmetric.SM4;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.SymmetricCrypto;



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
        String cipherTxt = "";
        //默认16进制密钥
        try {
            SymmetricCrypto sm4 = new SM4(Mode.CBC, Padding.PKCS5Padding, HexUtil.decodeHex(key), HexUtil.decodeHex(iv));
            cipherTxt = sm4.encryptBase64(plainTxt);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("SM4 encryption failed");
        }

        return cipherTxt;
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
            throw new RuntimeException("SM4 decryption failed");
        }
        return plainTxt;
    }


    public static void main(String[] args){
        //SM4 requires a 128 bit key
        //需要一个长度为16的字符串 16*8=128 bit UTF-8编码密钥
        String key = RandomUtil.randomString(16);
        String iv = RandomUtil.randomString(16);
        byte[] bytes = key.getBytes();
        byte[] bytes1 = iv.getBytes();
        System.out.println(Base64.encode(bytes));
        System.out.println(Base64.encode(bytes1));
        System.out.println("生成1个128bit的加密key:"+ HexUtil.encodeHexStr(bytes));
        System.out.println("生成1个128bit的加密iv:"+ HexUtil.encodeHexStr(bytes1));
        System.out.println("生成1个128bit的加密key:"+key);
        System.out.println("生成1个128bit的加密iv:"+iv);
        String str = "admin123";
        String enstr = encryptCBC(str, key, iv);
        System.out.println(enstr);
        String destr = decryptCBC(enstr, key, iv);
        System.out.println(destr);

    }
}
