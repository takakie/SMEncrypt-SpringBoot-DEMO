package com.gaomu.utils.crypto;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SecureUtil;
import org.apache.ibatis.logging.stdout.StdOutImpl;
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
    private static  String PUBLIC_KEY = "04c83eed01847c9b9c985dacb433a700c7d5131235223f11e1102f288011d949ebf8263d412a0a2f06b6f40d216fd27f79d5fbb579a9a9ee7654cc8a31c455047b";

    private static String PRIVATE_KEY = "f5f0cfcbde0e4ca6a7395f201b09954cedd77def6fe1391d52d2ca83f3188bd8";

    //密钥压缩标志位
    private static String PC = "04";


    public static Map<String,Object> generateKey(){
        KeyPair pair = SecureUtil.generateKeyPair("SM2");
        Map<String,Object> map = new HashMap<>();
        map.put("publicKey",pair.getPublic().getEncoded());
        map.put("privateKey",pair.getPrivate().getEncoded());
        return map;
    }

    public static String sm2Encrypt(String plaintext){
        SM2 sm2 = SmUtil.sm2(null, PUBLIC_KEY);
        return sm2.encryptHex(plaintext, KeyType.PublicKey);
    }

    public static String sm2Decrypt(String cipher) {
        //cipher 为16进制密文
        SM2 sm2 = SmUtil.sm2(PRIVATE_KEY, null);
        //判断是密文标志位，与前端同步
        if(!cipher.startsWith(PC)){
            cipher = PC + cipher;
        }
        System.out.println("cipher:" + cipher);
        return sm2.decryptStr(cipher,KeyType.PrivateKey);
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
        //生成SM2 der密钥对
        KeyPair keyPair = SecureUtil.generateKeyPair("SM2");
        byte[] priKey = keyPair.getPrivate().getEncoded();
        byte[] pubKey = keyPair.getPublic().getEncoded();
        System.out.println("priKeyHex: " + Hex.toHexString(priKey));
        System.out.println("pubKeyHex: " + Hex.toHexString(pubKey));
        System.out.println("priKey: " + Base64.encode(priKey));
        System.out.println("pubKey: " + Base64.encode(pubKey));
        //创建sm2加密对象，如何只是加密对象可设置privateKey为空


        byte[] priKey1 = HexUtil.decodeHex("ea2ba417d0fc27ba0f22f48d597bbd5af9711f81dfc8dec4f2789dbae13cc1bf");
        byte[] pubKey1 = HexUtil.decodeHex("04dfe3ce9d417265354fb45e19a5dd10c3f666486aaa79c6da93e8c004d1ef36c3ca9ce3e1844e370debb039f1269d35508b2bb2bbe02bc3df24330f2f238b6f11");


        SM2 sm2obj = SmUtil.sm2(priKey1,pubKey1);
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
        System.out.println("encStr2: " + encStr2);
        //解密
        String decStr2 = sm2Obj2.decryptStr(encStr2, KeyType.PrivateKey);
        System.out.println("decStr2: " + decStr2);
        System.out.println("================  第四种 前端密钥测试  =====================");
        String enStr2 =  sm2Encrypt("admin123");
        System.out.println("enStr2: " + enStr2);
        String deStr2 = sm2Decrypt("043b14f715f8c331fa474e219e75a7c66f9a35da46abd115ffd18e01cc2516963858dad4ee33686d813e926043bda9238e0b401aeffa06da505e0504b0f86c99644d0977f06e2bce76272cb3ecdd20e9c82ebedfe8c6772ff6bddb7cb42ca7747f128ffc");
        System.out.println("deStr2: " + deStr2);

    }

}
