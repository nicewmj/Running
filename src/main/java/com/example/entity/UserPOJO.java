package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * jackson的一些操作
 * 有时我们希望如果字段为null就不要返回给前端，可以使用@JsonInclude，它可以指定很多属性。
 *
 *@JsonInclude还可以加在类上，那么该对象所有为null的字段都不会参与JSON序列化。
 *
 * 对于一些老项目或者其他什么原因，原本传参使用的是下划线，比如user_type，而后端用Java改写时又要符合驼峰命名，此时可以用@JsonProperty做一层“隔离”
 *
 *  时间问题 ：
     * 如果希望更改出入参的时间格式，可以有局部和全局两种方式：
     * • 局部：@JsonFormat / @DateTimeFormat
     * • 全局
     * • YAML
     * • Config
     * @DateTimeFormat只适用于非JSON的POST请求，也就是说，如果项目本身都是JSON请求，你用@DateTimeFormat是无效的。你可以简单理解为：
     * • @DateTimeFormat，走表单请求时间转换器
     * • @JsonFormat，走JSON请求时间转换器
 */

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPOJO {
        /**
         * 姓名
         */
        private String name;
        /**
         * 年龄
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer age;

        /**
         * 用户类型
         */
        @JsonProperty("user_type")
        private Integer userType;

        /**
         * 生日
         */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 走json格式转换器

        // 走表单格式转换器  走json失败，报错
                 /*  {
                "timestamp": "2021-07-01T01:27:45.072+0000",
                "status": 400,
                "error": "Bad Request",
                "message": "",
                "path": "/addUserPOJO"
        } */
//        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date birthday;

        /**
         * 个人标签
         */
        private List<String> tags;

        private User user;

    }