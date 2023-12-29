package com.gaomu.server.impl;

import com.gaomu.domain.LoginUser;
import com.gaomu.domain.ResponseResult;
import com.gaomu.server.DataTestService;
import com.gaomu.utils.crypto.SM4Util;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class DataTestServiceImpl implements DataTestService {

    @Override
    public ResponseResult getApiTest(String nickName, String phoneNumber) {
        if(Objects.isNull(nickName) || Objects.isNull(phoneNumber)){
            return new ResponseResult(403, "请求参数为空");
        }
        //从loginUser中获取用户密钥等数据
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String nickName1 = loginUser.getUser().getNickName();
        String phoneNumber1 = loginUser.getUser().getPhoneNumber();
        String secretKey = loginUser.getUser().getSecretKey();
        String iv = loginUser.getUser().getIv();

        //解密手机号
        phoneNumber = SM4Util.decryptCBC(phoneNumber, secretKey, iv);

        if (Objects.equals(nickName1, nickName) && Objects.equals(phoneNumber1, phoneNumber)) {
            String email = loginUser.getUser().getEmail();
            String cipherEmail =  SM4Util.encryptCBC(email, secretKey, iv);
            Map<String, String > map = new HashMap<>();
            map.put("email", cipherEmail);
            return new ResponseResult(200, "获取邮件成功", map);
        }

        return new ResponseResult(403, "请输入正确的昵称和手机号");
    }
}
