package com.wqz.example.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/redis")
public class RedisController {
    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping("/test")
    public String test(){
        Map<String, String> map = new HashMap<>();
        map.put("name", "wqz");
        map.put("id", "123");

        redisTemplate.opsForValue().set("map", map);
        redisTemplate.opsForValue().set("user", "wuqingzhou");
        System.out.println(redisTemplate.opsForValue().get("map"));
        System.out.println(redisTemplate.opsForValue().get("user"));
        return null;
    }

}
