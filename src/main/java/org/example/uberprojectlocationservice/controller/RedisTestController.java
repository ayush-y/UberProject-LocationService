package org.example.uberprojectlocationservice.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
public class RedisTestController {

    private final StringRedisTemplate stringRedisTemplate;

    public RedisTestController(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @GetMapping("/test/redis")
    public String testRedis() {
        stringRedisTemplate.opsForValue().set("myKey", "Ayush");
        return stringRedisTemplate.opsForValue().get("myKey"); // should return "Ayush"
    }
}
