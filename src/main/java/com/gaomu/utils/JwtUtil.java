package com.gaomu.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

public class JwtUtil {

    //有效期为
    public static final Long JWT_TTL = 60 * 60 * 1000L;
    //设置私钥明文
    public static final String JWT_KEY = "takake";

    public static String getUUID(){
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        return token;
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMills = System.currentTimeMillis();
        Date now = new Date(nowMills);
        if (ttlMillis==null){
            ttlMillis=JwtUtil.JWT_TTL;
        }
        long expMills = nowMills + ttlMillis;
        Date expDate = new Date((expMills));
        return Jwts.builder()
                .setId(uuid) //唯一ID
                .setSubject(subject)  //主题
                .setIssuer("gaomu")  //签发者
                .setIssuedAt(now)   //签发时间
                .signWith(signatureAlgorithm, secretKey)    //使用HS256加密算法
                .setExpiration(expDate);
    }

    /**
     * 生成jwt
     * @param subject token中要存放的数据(json格式)
     * @para ttlMillis token 超时时间
     * @return
     */
    public static String createJWT(String subject, Long ttlMillis){
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());//设置超时时间
        return builder.compact();
    }

    /**
     * 生成jwt
     * @param subject token中要存放的数据(json格式)
     * @return
     */
    public static String createJWT(String subject){
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());//设置时间
        return builder.compact();
    }

    /**
     * 创建token
     * @param id
     * @param subject
     * @param ttlMillis
     * @return
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id); //设置过去时间
        return builder.compact();
    }

    /**
     * 生成加密的私钥 secretKey
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodeKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        SecretKey key = new SecretKeySpec(encodeKey, 0, encodeKey.length, "AES");
        return key;
    }

    /**
     * 解析
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)  // Use parseClaimsJws instead of parseClaimsJwt
                .getBody();
    }

    public static void main(String[] args) throws Exception{
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzMzJlZjc1N2Y4NmU0YTU0ODgyMmE1YmIyOTJmM2YzOSIsInN1YiI6IjEiLCJpc3MiOiJnYW9tdSIsImlhdCI6MTcwMzMwNzE1MSwiZXhwIjoxNzAzMzEwNzUxfQ._vL3KDAeNmBJsUmNmR1T-Ce8G4_sr6RKH7rFlWclOG8";
        Claims claims = parseJWT(token);
        System.out.println(claims);
    }
}
