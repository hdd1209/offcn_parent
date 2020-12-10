package com.hyd.user;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserApp.class})
public class ScwUserApplicationDemo {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void redisDemo(){
        stringRedisTemplate.opsForValue().set("msg","通过StringRedis存储的内容");
    }

}
