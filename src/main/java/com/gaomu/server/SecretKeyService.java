package com.gaomu.server;

import com.gaomu.domain.ResponseResult;

public interface SecretKeyService {
    ResponseResult<String> getPublicKey();

}
