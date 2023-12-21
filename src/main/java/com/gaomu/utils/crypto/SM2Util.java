package com.gaomu.utils.crypto;

import cn.hutool.core.util.HexUtil;
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
import org.bouncycastle.util.encoders.Hex;
import org.springframework.security.crypto.codec.Utf8;

import java.io.*;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

import com.gaomu.utils.crypto.SM2Key;

import static com.gaomu.utils.crypto.SM2Key.importPrivateKey;
import static com.gaomu.utils.crypto.SM2Key.importPublicKey;

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

        System.out.println("================  第一种 随机密钥对 =====================");
        //当新建sm2对象时，不传入密钥数据则会自动生成密钥
        String enStr = sm2.encryptBase64(text, KeyType.PublicKey);
        System.out.println("密文：" + enStr);
        String s1 = sm2.decryptStr(enStr, KeyType.PrivateKey);
        System.out.println("明文：" + s1);

        System.out.println("================  第二种 自定义密钥  =====================");
        //生成SM2 1024位密钥对
        KeyPair keyPair = SecureUtil.generateKeyPair("SM2");
        byte[] priKey = keyPair.getPrivate().getEncoded();
        byte[] pubKey = keyPair.getPublic().getEncoded();
        System.out.println("priKeyHex: " + Hex.toHexString(priKey));
        System.out.println("pubKeyHex: " + Hex.toHexString(pubKey));
        System.out.println("priKey: " + Base64.encode(priKey));
        System.out.println("pubKey: " + Base64.encode(pubKey));
        //创建sm2加密对象，如何只是加密对象可设置privateKey为空
        SM2 sm2obj = SmUtil.sm2(priKey,pubKey);
        //加密
        String encStr = sm2obj.encryptBase64(text,KeyType.PublicKey);
        System.out.println("encStr: " + encStr);
        //解密
        String decStr = sm2obj.decryptStr(encStr, KeyType.PrivateKey);
        System.out.println("decStr: " + decStr);

        System.out.println("================  第三种 文件读取密钥  =====================");
        //生成密钥
        KeyPair pair = SecureUtil.generateKeyPair("SM2");
        PublicKey aPublic = pair.getPublic();
        PrivateKey aPrivate = pair.getPrivate();
        //写pem
        SM2Key.exportPublicKey(aPublic,"E:\\Projects\\JAVA\\Encrypt-SpringBoot\\src\\main\\java\\com\\gaomu\\config\\sm2Key\\public_key.pem");
        SM2Key.exportPrivateKey(aPrivate,"E:\\Projects\\JAVA\\Encrypt-SpringBoot\\src\\main\\java\\com\\gaomu\\config\\sm2Key\\private_key.pem");
        //读pem
        PublicKey PublicKey = importPublicKey("E:\\Projects\\JAVA\\Encrypt-SpringBoot\\src\\main\\java\\com\\gaomu\\config\\sm2Key\\public_key.pem");
        PrivateKey privateKey = importPrivateKey("E:\\Projects\\JAVA\\Encrypt-SpringBoot\\src\\main\\java\\com\\gaomu\\config\\sm2Key\\private_key.pem");

        SM2 sm2Obj2 = SmUtil.sm2(privateKey, PublicKey);
        //加密
        String encStr2 =sm2Obj2.encryptBase64(text,KeyType.PublicKey);
        sm2Obj2.encryptBase64(text,KeyType.PublicKey);
        System.out.println("encStr2: " + encStr2);
        //解密
        String decStr2 = sm2Obj2.decryptStr(encStr2, KeyType.PrivateKey);
        System.out.println("decStr2: " + decStr2);

        System.out.println("================  第四种 前端密钥测试  =====================");


    }

}
