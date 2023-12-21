package com.gaomu.utils.crypto;

import cn.hutool.crypto.SecureUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.security.crypto.codec.Utf8;

import java.io.*;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

public class SM2Util {
    static final BouncyCastleProvider bc = new BouncyCastleProvider();
    public static Map<String,Object> generateKey(){
        KeyPair pair = SecureUtil.generateKeyPair("SM2");
        Map<String,Object> map = new HashMap<>();
        map.put("publicKey",pair.getPublic().getEncoded());
        map.put("privateKey",pair.getPrivate().getEncoded());
        return map;
    }
    public static void  main(String[] args){
        String text = "admin123";

        SM2 sm2 = SmUtil.sm2();

        //第一种 使用随机密钥对
        String enstr = sm2.encryptBase64(text, KeyType.PublicKey);
        System.out.println("密文：" + enstr);
        String s1 = sm2.decryptStr(enstr, KeyType.PrivateKey);
        System.out.println("明文：" + s1);
        //第二种 自定义密钥

    }

}
