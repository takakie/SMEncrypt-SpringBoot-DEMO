package com.gaomu.server;

import com.alibaba.fastjson.JSON;
import com.gaomu.domain.ResponseResult;
import com.gaomu.domain.User;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();

}
