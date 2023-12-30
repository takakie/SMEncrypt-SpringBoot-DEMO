package com.gaomu.utils.crypto;

import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class SM4YmlEncryptor implements PBEStringEncryptor {

    private String password;

    private static String key = "";
    private static String iv = "43374b4536657054564459466e774c53";

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String encrypt(String message) {
        String ciper = "";
        try {
            ciper = SM4Util.encryptCBC(message, password, iv);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ciper;
    }

    @Override
    public String decrypt(String encryptedMessage) {
        String plaintext = "";
        try {
            plaintext = SM4Util.decryptCBC(encryptedMessage, password, iv);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return plaintext;
    }

    public static void main(String[] args) {

        SM4YmlEncryptor smm4 = new SM4YmlEncryptor();
        smm4.setPassword(key);
        String originalText = "123456";
        String encryptedText = smm4.encrypt(originalText);
        String decryptedText = smm4.decrypt(encryptedText);
        System.out.println("Original text: " + originalText);
        System.out.println("Encrypted text: " + encryptedText);
        System.out.println("decrypted Text: " + decryptedText);

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("04c83eed01847c9b9c985dacb433a70"); // 可以是任何字符串，你的加密密码
        encryptor.setAlgorithm("PBEWithMD5AndDES"); // 这是默认的算法，你也可以使用其他的算法
        encryptor.setStringOutputType("UTF-8");
        String encryptedText1 = encryptor.encrypt(originalText);
        String decryptedText1 = encryptor.decrypt(encryptedText1);
    }
}
