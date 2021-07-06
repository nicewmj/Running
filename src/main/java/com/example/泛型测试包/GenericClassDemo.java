package com.example.泛型测试包;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class GenericClassDemo {

    public static void main(String[] args) throws Exception {

        List<String> list = new ArrayList<>();
        list.add("aaa");
        list.add("bbb");
        // 编译器会阻止
//         list.add(333);

        //利用反射 到运行期的时候 往list 添加 不是string类型的数据
        // 但泛型约束只存在于编译期，底层仍是Object，所以运行期可以往List存入任何类型的元素
        Method add = list.getClass().getMethod("add", Object.class);
        Object invoke = add.invoke(list, 444);

        // 打印输出观察是否成功存入Integer（注意用Object接收）
        for (Object obj : list) {
            System.out.println(obj);
        }


        //测试 打印任意类型方法
        List<String> integerList = new ArrayList<>();
        print(integerList);
    }


    public static <T> void print(List<? extends T> list) {

        System.out.println("打印任意类型方法" + list);

    }
}