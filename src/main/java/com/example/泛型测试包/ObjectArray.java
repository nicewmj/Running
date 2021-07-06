package com.example.泛型测试包;

/**
 * @author qiyu
 * @date 2020-09-09 00:02
 */
public class ObjectArray {
    public static void main(String[] args) {
        // ArrayList<T>底层还是Object[]数组
        Object[] objects = new Object[4];

        // 引入泛型后的两个作用：

        // 1. 在编译期把元素类型限制为指定的类型，比如ArrayList<Integer>
        objects[0] = 1;
        objects[1] = 2;

        // 1. 在编译期把元素类型限制为指定的类型，比如ArrayList<String>
        objects[2] = "3";
        objects[3] = "4";

        // 2. 编译期编译后，会根据类型参数自动转换，不用我们操心。转为Integer
        Integer zero = (Integer) objects[0];
        Integer one = (Integer) objects[1];

        // 2. 编译期编译后，会根据类型参数自动转换，不用我们操心。转为String
        String two = (String) objects[2];
        String three = (String) objects[3];

        System.out.println(zero + " " + one + " " + two + " " + three);

    }
}