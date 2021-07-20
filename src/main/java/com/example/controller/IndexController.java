package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @description: 前端控制器
 * @author: xu zhihao
 * @create: 2019-06-14 10:36
 */
@Controller
public class IndexController {

    @GetMapping("layui")
    public String index() {
        return "index.html";
    }

    @GetMapping("testLayui")
    public String TestLayui() {
        return "testLayui.html";
    }

}
