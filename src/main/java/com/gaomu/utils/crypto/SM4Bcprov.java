package com.gaomu.utils.crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

public class SM4Bcprov {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte[] encrypt(byte[] key, byte[] input) throws Exception {
        Cipher cipher = Cipher.getInstance("SM4/ECB/PKCS5Padding", "BC");
        SecretKeySpec sm4Key = new SecretKeySpec(key, "SM4");
        cipher.init(Cipher.ENCRYPT_MODE, sm4Key);
        return cipher.doFinal(input);
    }

    public static byte[] decrypt(byte[] key, byte[] input) throws Exception {
        Cipher cipher = Cipher.getInstance("SM4/ECB/PKCS5Padding", "BC");
        SecretKeySpec sm4Key = new SecretKeySpec(key, "SM4");
        cipher.init(Cipher.DECRYPT_MODE, sm4Key);
        return cipher.doFinal(input);
    }

    public static void main(String[] args) throws Exception {
        // 这是一个示例，你需要使用自己的 key 和要加密的数据
        byte[] key = "0123456789abcdef".getBytes();
        byte[] data = "Hello, SM4!".getBytes(StandardCharsets.UTF_8);
        byte[] encryptedData = encrypt(key, data);
        System.out.println("Encrypted data: " + Hex.toHexString(encryptedData));

        byte[] decryptedData = decrypt(key, encryptedData);
        System.out.println("Decrypted data: " + new String(decryptedData));

        String str = "YWRtaW4xMjM=";
        byte[] decodedBytes = Base64.getDecoder().decode(str);
        System.out.println(Arrays.toString(decodedBytes));
        System.out.println(Base64.getEncoder().encodeToString(decodedBytes));;
        String strx = new String(decodedBytes, StandardCharsets.UTF_8);
        System.out.println(strx);

    }
}
