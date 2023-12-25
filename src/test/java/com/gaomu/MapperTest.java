package com.gaomu;

import com.gaomu.domain.User;
import com.gaomu.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@SpringBootTest
public class MapperTest {
    @Autowired
    private UserMapper userMapper;
    @Test
    public void TestBCryptPasswordEncoder(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String str = passwordEncoder.encode("123456");
        boolean flag = passwordEncoder.matches("123456", "$2a$10$m9YSh0vb4CNyCLlLgAAfr.XxSwplEqCTAVtO3fHAzKOA6T3F.W0Z6");
        System.out.println(str);
        System.out.println(flag);
    }

    @Test
    public void testUserMapper(){
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }


}
