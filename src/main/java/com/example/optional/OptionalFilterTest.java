package com.example.optional;

import lombok.Data;

import java.util.Optional;
import java.util.Random;

public class OptionalFilterTest {

    public static void main(String[] args) {

        // 需求：调用getUser()得到person，并且person的age大于18才返回username，否则返回不存在

        // 普通的写法（如果层级深一点会很难看）
        Person user = getUser();
        if (user != null && user.getAge() > 18) {
            System.out.println(user.getName());
        } else {
            System.out.println("不存在");
        }

        // 你尝试用map()，但你发现直接返回username了，你甚至无法再次判断是否age>18
        String username1 = Optional.ofNullable(getUser())
                .map(Person::getName)
                .orElse("不存在");
        System.out.println("username1 = " + username1);

        // 引入filter()
        String username2 = Optional.ofNullable(getUser())
                .filter(person -> person.getAge() > 18)
                .map(Person::getName)
                .orElse("不存在");
        System.out.println("username2 = " + username2);
    }

    public static Person getUser() {
        Random random = new Random();
//        if (RandomUtils.nextBoolean()) {
        if (random.nextBoolean()) {
            return null;
        } else {
            Person person = new Person();
            person.setName("鲍勃");
            // commons.lang3
//            person.setAge(RandomUtils.nextInt(0, 50));
            person.setAge(random.nextInt(50));
            return person;
        }
    }

    @Data
    static class Person {
        private String name;
        private Integer age;
    }
}