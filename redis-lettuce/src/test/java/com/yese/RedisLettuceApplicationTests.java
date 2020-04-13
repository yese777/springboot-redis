package com.yese;

import com.yese.pojo.User;
import com.yese.service.Userservice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisLettuceApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private Userservice userservice;

    @Test
    void t1() {
        String name = userservice.getString("name");
        System.out.println(name);
    }














    @Test
    void t2() {
        User user = userservice.getById("2");
        System.out.println(user);
    }
}
