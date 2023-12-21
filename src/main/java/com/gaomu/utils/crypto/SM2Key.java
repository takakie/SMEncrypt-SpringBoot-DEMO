package com.gaomu.utils.crypto;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SM2Key {
    static final BouncyCastleProvider bc = new BouncyCastleProvider();
    /**
     * 从字符串中读取 私钥 key
     * @param privateKeyStr String
     * @return PrivateKey
     */
    public static PrivateKey strToPrivateKey(String privateKeyStr){
        PrivateKey privateKey = null;
        try {
            byte[] encPriv = Base64.decode(privateKeyStr);
            KeyFactory keyFact = KeyFactory.getInstance("EC", bc);
            privateKey = keyFact.generatePrivate(new PKCS8EncodedKeySpec(encPriv));
        }catch (Exception e){
            e.printStackTrace();
        }
        return privateKey;
    }

    /**
     * 从字符串中读取 公钥 key
     * @param publicKeyStr String
     * @return PublicKey
     */
    public  static PublicKey strToPublicKey(String publicKeyStr){
        PublicKey publicKey =  null;
        try {
            byte[] encPub = Base64.decode(publicKeyStr);
            KeyFactory keyFact = KeyFactory.getInstance("EC", bc);
            publicKey = keyFact.generatePublic(new X509EncodedKeySpec(encPub));
        }catch (Exception e){
            e.printStackTrace();
        }
        return publicKey;
    }


    /**
     * 公钥 key  转文件
     * @param publicKey PublicKey
     * @param keyPath String
     */
    public static void exportPublicKey(PublicKey publicKey,String keyPath){
        byte[] encPubl = publicKey.getEncoded();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(keyPath));
            writer.write("-----BEGIN SM2 PUBLIC KEY-----\n");
            writer.write(Base64.encode(encPubl));
            writer.write("\n-----END SM2 PUBLIC KEY-----");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 私钥 key 转文件
     * @param privateKey PrivateKey
     * @param keyPath String
     */
    public static void exportPrivateKey(PrivateKey privateKey, String keyPath){
        byte[] encPriv = privateKey.getEncoded();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(keyPath));
            writer.write("-----BEGIN SM2 PRIVATE KEY-----\n");
            writer.write(Base64.encode(encPriv));
            writer.write("\n-----END SM2 PRIVATE KEY-----");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static PublicKey importPublicKey(String keyPath){
        PublicKey pubKey = null;
        try {
            List<String> lines = Files.readAllLines(Paths.get(keyPath), StandardCharsets.UTF_8);
            String base64PublicKeyString = lines.stream()
                    .filter(line -> !line.startsWith("-----BEGIN SM2 PUBLIC KEY-----")
                            && !line.startsWith("-----END SM2 PUBLIC KEY-----"))
                    .collect(Collectors.joining());
            pubKey = strToPublicKey(base64PublicKeyString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pubKey;
    }


    public static PrivateKey importPrivateKey(String keyPath){
        PrivateKey priKey = null;
        try {
            List<String> lines = Files.readAllLines(Paths.get(keyPath), StandardCharsets.UTF_8);
            String base64PrivateKeyString = lines.stream()
                    .filter(line -> !line.startsWith("-----BEGIN SM2 PRIVATE KEY-----")
                            && !line.startsWith("-----END SM2 PRIVATE KEY-----"))
                    .collect(Collectors.joining());
            priKey = strToPrivateKey(base64PrivateKeyString);
            //System.out.println("BASE64 Private Key: " + base64PrivateKeyString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return priKey;
    }

    public static void main(String[] args){
        String text = "admin123";
        System.err.println(text);

        KeyPair pair = SecureUtil.generateKeyPair("SM2");
        PublicKey aPublic = pair.getPublic();
        PrivateKey aPrivate = pair.getPrivate();
        System.out.println(Arrays.toString(aPublic.getEncoded()));
        System.out.println(Arrays.toString(aPrivate.getEncoded()));
        //公钥 key 和私钥 key 转文件
        SM2Key.exportPublicKey(aPublic,"E:\\Projects\\JAVA\\Encrypt-SpringBoot\\src\\main\\java\\com\\gaomu\\config\\sm2Key\\public_key.pem");
        SM2Key.exportPrivateKey(aPrivate,"E:\\Projects\\JAVA\\Encrypt-SpringBoot\\src\\main\\java\\com\\gaomu\\config\\sm2Key\\private_key.pem");
        PublicKey PublicKey = importPublicKey("E:\\Projects\\JAVA\\Encrypt-SpringBoot\\src\\main\\java\\com\\gaomu\\config\\sm2Key\\public_key.pem");
        PrivateKey privateKey = importPrivateKey("E:\\Projects\\JAVA\\Encrypt-SpringBoot\\src\\main\\java\\com\\gaomu\\config\\sm2Key\\private_key.pem");
        System.out.println("PublicKeyBytes" + Arrays.toString(PublicKey.getEncoded()));
        System.out.println("PrivateKeyBytes" + Arrays.toString(privateKey.getEncoded()));
    }
}
