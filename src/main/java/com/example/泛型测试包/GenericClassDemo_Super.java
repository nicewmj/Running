package com.example.泛型测试包;

import java.util.ArrayList;
import java.util.List;

/**
 * super通配符被称为下边界通配符，它的特点是：
 * • List<? super Human>接收：只能指向父类型List，比如List<Creature>、List<Primate>
 * • List<? super Human>取出：只能转Object（WHY）
 * • List<? super Human>存入：只能存Human及其子类型元素（WHY）
 * <p>
 * super小结
 * • List<? super Human>接收：只能指向父类型List，比如List<Creature>、List<Primate>
 * • List<? super Human>取出：只能转Object
 * • List<? super Human>存入：只能存Human及其子类型元素
 * <p>
 * 但如果对形参开放程度要求不高，却对方法调用有一些要求，就考虑使用extends或super。而对于extends和super的取舍，
 * 《Effective Java》提出了所谓的：PECS(Producer Extends Consumer Super)
 * • 频繁往外读取内容的，适合用<? extends T>：extends返回值稍微精确些
 * • 经常往里插入的，适合用<? super T>：super允许存入子类型元素
 */
public class GenericClassDemo_Super {

    public static void main(String[] args) {

        List<? super Human> humanList = new ArrayList<>();
        // 别紧张，还没开始呢，我现在自己指向自己，还不让存东西了吗？
        humanList.add(new Human("人类"));

        // 只能指向Human及其父类型的List：灵长类、生物类
//        humanList = new ArrayList<Chinese>(); // ERROR

        humanList = new ArrayList<Creature>();
        humanList = new ArrayList<Primate>();

        // 哇，牛逼啊，简单泛型和extends都搞不定的存入问题让你super整得服服帖帖
        humanList.add(new Human("女性"));
        humanList.add(new Chinese("中国人"));

        // super：也不是啦，我虽然能存东西，但规定只能存Human及其子类型元素
//        humanList.add(new Primate("灵长类动物"));// ERROR
//        humanList.add(new Creature("外星生物"));// ERROR
//        humanList.add("无关类型，比如String");// ERROR

        // 简单类型&extends：哈哈哈，你这啥玩意，怎么只能往Object自动转型
        Object object = humanList.get(0);


    }


    static class Creature {
        public Creature(String name) {
            this.name = name;
        }

        private String name;

    }

    static class Primate extends Creature {
        public Primate(String name) {
            super(name);
        }
    }

    static class Human extends Primate {
        public Human(String name) {
            super(name);
        }
    }

    static class Chinese extends Human {
        public Chinese(String name) {
            super(name);
        }
    }

    static class Japanese extends Human {
        public Japanese(String name) {
            super(name);
        }
    }

}