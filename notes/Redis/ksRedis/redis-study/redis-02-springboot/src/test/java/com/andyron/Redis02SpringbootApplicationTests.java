package com.andyron;

import com.andyron.pojo.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class Redis02SpringbootApplicationTests {

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        // redisTemplate 操作不同的数据类型，api和redis命令相同
        // opsForValue  操作字符串  String
        // opsForList   List
        // opsForSet
        // opsForZSet
        // opsForHash
        // ...

        // 除了基本的操作，常用方法都可以通过RedisTemplate对象直接操作，比如事务，基本curd

        // 获取redis链接对象
//        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
//        connection.flushDb();
//        connection.flushAll();


        redisTemplate.opsForValue().set("k1", "v1");
        System.out.println(redisTemplate.opsForValue().get("k1"));
    }

    @Test
    public void test() throws JsonProcessingException {
        User user = new User("王二麻子", 4);
//        String jsonUser = new ObjectMapper().writeValueAsString(user);
        redisTemplate.opsForValue().set("user", user);
        System.out.println(redisTemplate.opsForValue().get("user"));
    }

}
