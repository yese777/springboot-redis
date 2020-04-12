package com.yese;

import com.yese.pojo.User;
import com.yese.service.Userservice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

@SpringBootTest
class RedisJedisApplicationTests {

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



    @Autowired
    private JedisPool jedisPool;//Jedis连接池

    /**
     * 事务的简单演示
     */
    @Test
    void contextLoads() {
        // 得到 jedis 对象
        Jedis jedis = jedisPool.getResource();

        // 开启事务
        Transaction multi = jedis.multi();

        try {
            multi.set("name1", "zhangsan");
            multi.set("name2", "lisi");
            // 模拟抛出异常
            int i = 1 / 0;
            // 执行事务
            multi.exec();
        } catch (Exception e) {
            // 失败就放弃事务
            multi.discard();
            e.printStackTrace();
        } finally {
            // 关闭连接
            jedis.close();
        }
    }

}
