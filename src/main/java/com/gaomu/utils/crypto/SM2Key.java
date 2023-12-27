package com.gaomu.utils.crypto;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SM2Key {


    static final BouncyCastleProvider bc = new BouncyCastleProvider();

    public static Map<String, String> generateECSm2HexKey() {
        SM2 sm2 = new SM2();
        ECPublicKey publicKey = (ECPublicKey) sm2.getPublicKey();
        ECPrivateKey privateKey = (ECPrivateKey) sm2.getPrivateKey();
        // 获取EC公钥
        byte[] publicKeyBytes = publicKey.getQ().getEncoded(false);
        String publicKeyHex = HexUtil.encodeHexStr(publicKeyBytes);

        // 获取EC私钥
        String privateKeyHex = privateKey.getD().toString(16);
        // BigInteger转成16进制时，不一定长度为64，如果私钥长度小于64，则在前方补0
        StringBuilder privateKey16 = new StringBuilder(privateKeyHex);
        while (privateKey16.length() < 64) {
            privateKey16.insert(0, "0");
        }

        System.out.println("EC_KEY_PUBLIC_KEY: " + publicKeyHex);
        System.out.println("EC_KEY_PRIVATE_KEY: " + privateKey16);

        Map<String, String> result = new HashMap<>();
        result.put("publicKey", publicKeyHex);
        result.put("privateKey", privateKey16.toString());
        return result;
    }

    public static Map<String,String> generateECBASE64Key(){
        KeyPair pair = SecureUtil.generateKeyPair("SM2");
        ECPublicKey publicKey = (ECPublicKey) pair.getPublic();
        ECPrivateKey privateKey = (ECPrivateKey) pair.getPrivate();
        // 获取公钥
        byte[] publicKeyBytes = publicKey.getQ().getEncoded(false);
        // 获取256位私钥
        byte[] privateKeyBytes = privateKey.getD().toByteArray();
        String publicKeyBase64 = Base64.encode(publicKeyBytes);
        String privateKeyBase64 = Base64.encode(privateKeyBytes);

        System.out.println("EC_KEY_PUBLIC_KEY: " + publicKeyBase64);
        System.out.println("EC_KEY_PRIVATE_KEY: " + privateKeyBase64);

        Map<String,String> map = new HashMap<>();
        map.put("publicKey", publicKeyBase64);
        map.put("privateKey", privateKeyBase64);
        return map;
    }

    public static byte[] publicKeyDERToDEC(PublicKey publicKey){
        ECPublicKey eCPublicKey = (ECPublicKey) publicKey;
        return eCPublicKey.getQ().getEncoded(false);
    }

    public static byte[] privateKeyDERToDEC(PrivateKey privateKey){
        ECPrivateKey eCPrivateKey = (ECPrivateKey) privateKey;
        return eCPrivateKey.getD().toByteArray();
    }

    /**
     * 从BASE64字符串中读取 私钥 PrivateKey
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
     * 从BASE64字符串中读取 公钥 key
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
            System.out.println("base64PublicKeyString: " + base64PublicKeyString);
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
            System.out.println("base64PrivateKeyString: " + base64PrivateKeyString);
            priKey = strToPrivateKey(base64PrivateKeyString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return priKey;
    }

    public static void main(String[] args) throws Exception {
        String text = "admin123";
        System.out.println("plaintext: " + text);
        System.out.println("==================================生成EC-字符串-SM2密钥对=================================");
        generateECSm2HexKey();
        generateECBASE64Key();
        System.out.println("=====================================生成SM2密钥对=======================================");
        KeyPair pair = SecureUtil.generateKeyPair("SM2");
        PublicKey aPublic = pair.getPublic();
        PrivateKey aPrivate = pair.getPrivate();
        System.out.println("KEY_PUBLIC_KEY: " + Arrays.toString(aPublic.getEncoded()));
        System.out.println("KEY_PRIVATE_KEY: " + Arrays.toString(aPrivate.getEncoded()));
        //公钥 key 和私钥 key 转文件
        System.out.println("aPrivate: " + aPrivate);
        System.out.println("aPublic: " + aPublic);
        System.out.println("======================================导出密钥对=================================");
        SM2Key.exportPublicKey(aPublic,"public_key.pem");
        SM2Key.exportPrivateKey(aPrivate,"private_key.pem");
        PublicKey PublicKey = importPublicKey("public_key.pem");
        PrivateKey privateKey = importPrivateKey("private_key.pem");
        System.out.println("PublicKeyBytes: " + Arrays.toString(PublicKey.getEncoded()));
        System.out.println("PrivateKeyBytes: " + Arrays.toString(privateKey.getEncoded()));

    }
}
