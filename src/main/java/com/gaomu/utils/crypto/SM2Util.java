package com.gaomu.utils.crypto;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SM4;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.core.codec.Base64;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import static com.gaomu.utils.crypto.SM2Key.importPrivateKey;
import static com.gaomu.utils.crypto.SM2Key.importPublicKey;

public class SM2Util {
    private static  String PUBLIC_KEY = "";

    private static String PRIVATE_KEY = "";

    //密钥压缩标志位
    private static final String PC = "04";

    public static Map<String, String> generateECSM2HexKey() {
        return SM2Key.generateECSm2HexKey();
    }

    public static String sm2Encrypt(String plaintext, String publicKey){
        String cipher = "";
        try {
            SM2 sm2 = SmUtil.sm2(null, publicKey);
            cipher = sm2.encryptHex(plaintext, KeyType.PublicKey);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("SM2 encryption failed");
        }
        return cipher;
    }

    public static String sm2Decrypt(String cipher, String privateKey) {
        //判断是密文标志位，与前端同步
        if(!cipher.startsWith(PC)){
            cipher = PC + cipher;
        }
        String plainText = "";
        try {
            SM2 sm2 = SmUtil.sm2(privateKey, null);
            plainText =  sm2.decryptStr(cipher,KeyType.PrivateKey);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("SM2 decryption failed");
        }
        //cipher 为16进制密文
        return plainText;
    }

    public static void  main(String[] args){
        String text = "admin123";
        System.out.println("plaintext: " + text);
        SM2 sm2 = SmUtil.sm2();
        System.out.println("================  HexEC密钥 =====================");
        Map<String, String> sm2KeyPair = generateECSM2HexKey();
        System.out.println("sm2KeyPair: " + sm2KeyPair);
        System.out.println("================  随机密钥对 =====================");
        //当新建sm2对象时，不传入密钥数据则会自动生成密钥
        String enStr = sm2.encryptBase64(text, KeyType.PublicKey);
        System.out.println("密文：" + enStr);
        String deStr = sm2.decryptStr(enStr, KeyType.PrivateKey);
        System.out.println("明文：" + deStr);

        System.out.println("================ 文件读取密钥  =====================");
        //生成密钥
        KeyPair pair = SecureUtil.generateKeyPair("SM2");
        PublicKey aPublic = pair.getPublic();
        PrivateKey aPrivate = pair.getPrivate();
        //写pem
        SM2Key.exportPublicKey(aPublic,"public_key.pem");
        SM2Key.exportPrivateKey(aPrivate,"private_key.pem");
        //读pem
        PublicKey PublicKey = importPublicKey("public_key.pem");
        PrivateKey privateKey = importPrivateKey("private_key.pem");

        SM2 sm22 = SmUtil.sm2(privateKey, PublicKey);
        //加密
        String encStr2 =sm22.encryptBase64(text,KeyType.PublicKey);
        System.out.println("encStr2: " + encStr2);
        //解密
        String decStr2 = sm22.decryptStr(encStr2, KeyType.PrivateKey);
        System.out.println("decStr2: " + decStr2);
        System.out.println("================  前端密钥同步加解密  =====================");
        String enStr2 =  sm2Encrypt("admin123", PUBLIC_KEY);
        System.out.println("enStr2: " + enStr2);
        String deStr2 = sm2Decrypt(enStr2, PRIVATE_KEY);
        System.out.println("deStr2: " + deStr2);

    }
}
