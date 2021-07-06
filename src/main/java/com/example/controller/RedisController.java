package com.example.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.entity.User;
import com.example.redisUtils.RedisUtils;
import com.example.redisUtils.SerializeUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RedisController {

    @RequestMapping("testRedis")
    public String TestRedis() {
        //1 redis 存储 map 对象
        Map<String, String> map = new HashMap<>();
        map.put("a", "b");
        map.put("c", "d");
        map.put("e", "f");
        if (RedisUtils.exists("TESTREDIS", "R1")) {
            RedisUtils.del("TESTREDIS", "R1");
        }
        String hmset = RedisUtils.hmset("TESTREDIS", map, "R1");
        String hget = RedisUtils.hget("TESTREDIS", "a", "R1");

        //2 redis 存储java对象 序列化
        User user = new User();
        user.setId("1");
        user.setGender("女");
        user.setName("李四");
        user.setAge(13);
        Jedis r1 = RedisUtils.getResource("R1");
        String set = r1.set("user".getBytes(), SerializeUtil.serialize(user));
        System.out.println("--------------------------------------------------------------------" + set);
        try {
            User userResult = (User) SerializeUtil.deserialize(r1.get("user".getBytes()));
            System.out.println("--------------------------------------------------------------------" + userResult);
            System.out.println(userResult.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        // 3 redis 存储java对象 json 方式
        String string = JSONObject.toJSONString(user);
        String set1 = RedisUtils.set("TTT", string, "R1");
        String s = RedisUtils.get("TTT", "R1");
        System.out.println("--------------------------------------------------------------------" + s);
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = jsonObject.getJSONArray(s);

        return "OK";
    }

}
