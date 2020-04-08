package com.yese.service.impl;

import com.yese.pojo.User;
import com.yese.service.Userservice;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

@Service
@Log
public class UserserviceImpl implements Userservice {

    @Autowired
    private JedisPool jedisPool;//Jedis连接池

    @Override
    public String getString(String key) {
        // 查的数据
        String val = "";

        // 得到 jedis 对象
        Jedis jedis = jedisPool.getResource();
        if (jedis.exists(key)) {
            log.info("查询 redis 中的数据");
            val = jedis.get(key);// 取
        } else {
            val = "zhangsan";
            log.info("查询 mysql 中的数据:" + val);
            jedis.set(key, val);// 存 默认永久有效
            jedis.expire(key, 30);//设置有效期 30 秒
        }
        // 关闭连接
        jedis.close();
        return val;
    }


    @Override
    public User getById(String id) {
        // 返回结果
        User user = new User();

        // 拼接redis中key的格式,只是 key 的命名建议
        String key = "user:" + id;

        // 得到 jedis 对象
        Jedis jedis = jedisPool.getResource();
        if (jedis.exists(key)) {
            log.info("查询 redis 中的数据");
            // 取
            Map<String, String> map = jedis.hgetAll(key);

            user.setId(map.get("id"));
            user.setName(map.get("name"));
            user.setAge(Integer.parseInt(map.get("age")));
        } else {

            // 此处模拟从数据库中查出来一个 user
            user.setId(id);
            user.setName("张三");
            user.setAge(20);
            log.info("查询 mysql 中的数据:" + user);

            Map<String, String> map = new HashMap<>();
            map.put("id", user.getId());
            map.put("name", user.getName());
            map.put("age", user.getAge() + "");

            // 存
            jedis.hmset(key, map);
        }
        // 关闭连接
        jedis.close();
        return user;
    }
}
