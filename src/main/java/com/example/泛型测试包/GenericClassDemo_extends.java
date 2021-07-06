package com.example.泛型测试包;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * extends小结
 * • List<? extends Human>指向：只能指向子类型List，比如List<Chinese>，Human是最上边的类
 * • List<? extends Human>取出：接上一条限制，不论指向什么List元素必然是Human及其子类型，按Human转
 * • List<? extends Human>存入：简单泛型要是能解决早解决了，还轮得到我？直接禁止存入，溜了溜了
 */
public class GenericClassDemo_extends {

    public static void main(String[] args) {

        ArrayList<Chinese> chineseArrayList = new ArrayList<>();

        chineseArrayList.add(new Chinese("李健"));

        chineseArrayList.add(new Chinese("周深"));


        ArrayList<Japanese> japaneseArrayList = new ArrayList<>();

        japaneseArrayList.add(new Japanese("三浦春马"));

        japaneseArrayList.add(new Japanese("瑛太"));

        /**
         * 与编译器约定，左右两边类型不一致也能赋值，但是有条件：
         * 右边List的参数类型必须是Human的子类

         */

        List<? extends Human> humanList = chineseArrayList;
        Human lee = humanList.get(0);
        Human chou = humanList.get(1);
        System.out.println(lee + "&" + chou);

        humanList = japaneseArrayList;
        Human haRuMa = humanList.get(0);
        Human eiTa = humanList.get(1);
        System.out.println(haRuMa + "&" + eiTa);

    }

    @AllArgsConstructor

    static class Human {
        private String name;

    }


    public static class Chinese extends Human {

        public Chinese(String name) {

            super(name);

        }

    }

    public static class Japanese extends Human {
        public Japanese(String name) {
            super(name);
        }

    }
}
