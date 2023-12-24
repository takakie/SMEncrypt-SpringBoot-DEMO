package com.gaomu.controller;

import com.gaomu.domain.ResponseResult;
import com.gaomu.server.DataTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class HelloController {
    @Autowired
    private DataTestService dataTestService;
    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }


    @RequestMapping("/getApiTest")
    public ResponseResult getApiTest(@RequestParam String nickName, @RequestParam String phoneNumber){
        return dataTestService.getApiTest(nickName, phoneNumber);
    }
}
