package com.example.controller;

import com.example.entity.UploadUser;
import com.example.entity.User;
import com.example.entity.UserPOJO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class UserController {
    /**
     * 在跨域方法上加@CrossOrigin即可完美解决跨域问题
     * @param id
     * @return
     */

    @CrossOrigin("http://localhost:7070")
    @GetMapping(value = "/getUser/{id}")
    public User getUser(@PathVariable("id") Long id) {
        // id没用上，就是演示一下@PathVariable注解
        System.out.println("id:" + id);

        User user = new User();
        user.setName("bravo");
        user.setAge(18);
        user.setAddress("wenzhou");

        return user;
    }


    /**
     * 新增用户
     * 前段的json请求，后端必须加上 @RequestBody
     *
     * 什么时候加@RequestBody？
     * POST请求是JSON时，必须加@RequestBody
     * POST请求时普通表单时，不用加，加了反而错
     * POST请求是文件上传时，不用加，用MultipartFile
     */
    @PostMapping("addUser")
    public UploadUser addUser(@RequestBody UploadUser user) {
        log.info("user:{}", user);
        return user;
    }


    @PostMapping("/addUserPOJO")
    public UserPOJO addUser(@RequestBody UserPOJO user) {
        user.setAge(null);
//        user.setName(null);
        return user;
    }

}
