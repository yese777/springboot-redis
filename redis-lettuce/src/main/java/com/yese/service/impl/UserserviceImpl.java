package com.yese.service.impl;

import com.yese.pojo.User;
import com.yese.service.Userservice;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Log
public class UserserviceImpl implements Userservice {

    @Autowired
    private RedisTemplate redisTemplate;//<String,Object>

    @Override
    public String getString(String key) {
        // 查的数据
        String val = "";

        // 得到 jedis 对象
        if (redisTemplate.hasKey(key)) {
            log.info("查询 redis 中的数据");
            // 取
            val = (String) redisTemplate.opsForValue().get(key);
        } else {
            val = "zhangsan";
            log.info("查询 mysql 中的数据:" + val);
            //添加：redisTemplate.opsForValue().set(key, val);
            //存，设置过期时间：30秒
            redisTemplate.opsForValue().set(key, val, 30, TimeUnit.SECONDS);
        }
        return val;
    }


    /**
     *
     * 出现了很多相同的"user",可以提取出来
     * 方案一:常量类/枚举
     * 方案二:在实体bean内声明一个获取 keyName 的静态方法
     */
    @Override
    public User getById(String id) {
        // 返回结果
        User user = new User();

        // key的格式:   user:1
        if (redisTemplate.opsForHash().hasKey("user", id)) {
            log.info("查询 redis 中的数据");

            // 取
            user = (User) redisTemplate.opsForHash().get("user", id);

        } else {
            // 此处模拟从数据库中查出来一个 user
            user.setId(id);
            user.setName("李四");
            user.setAge(30);

            log.info("查询 mysql 中的数据:" + user);
            // 存
            redisTemplate.opsForHash().put("user", id, user);
        }

        return user;
    }
}
