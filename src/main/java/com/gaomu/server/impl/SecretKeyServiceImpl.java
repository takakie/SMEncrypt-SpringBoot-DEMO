package com.gaomu.server.impl;

import com.gaomu.domain.ResponseResult;
import com.gaomu.server.SecretKeyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class SecretKeyServiceImpl implements SecretKeyService {

    @Value("${secretKey.publicKey}")
    private String publicKey;

    @Override
    public ResponseResult getPublicKey() {
        System.out.println("publicKey :" + publicKey );
        Map<String, String > map = new HashMap<>();
        map.put("publicKey", publicKey);
        return new ResponseResult(200, "获取成功", map);
    }
}
