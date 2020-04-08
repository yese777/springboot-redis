package com.yese.service;

import com.yese.pojo.User;

public interface Userservice {

    /**
     * Redis操作String
     * 需求：用户输入一个 key 先判断 Redis 中是否存在该数据
     * 如果存在，在 Redis 中进行查询，并返回
     * 如果不存在，在 MYSQL 数据库查询。将结果赋给 Redis(有效时间 30 秒)，并返回
     */
    String getString(String key);

    /**
     * Redis操作hash
     * 逻辑同上
     */
    User getById(String id);
}
