package com.gaomu.server;

import com.gaomu.domain.ResponseResult;

public interface DataTestService {

    ResponseResult getApiTest(String nickName, String phoneNumber);
}
