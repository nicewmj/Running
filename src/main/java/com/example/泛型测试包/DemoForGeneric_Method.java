package com.example.泛型测试包;

import java.util.*;

public class DemoForGeneric_Method {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5"));
        List<String> stringList = reverseList(list);

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map1 = new HashMap<>();
        map.put("ds", 1);
        map1.put("result", map);


        System.out.println(map1.get("result"));
        String s = String.valueOf(map1.get("result"));
        if ("{}".equals(s)) {
            Object result = map1.get("result");
        }
        Object result = map1.get("result");
    }

    /**
     * 静态的泛型方法，出现在一个普通类中
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List<T> reverseList(List<T> list) {
        List<T> newList = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            newList.add(list.get(i));
        }
        return newList;
    }

    /**
     * 泛型方法，无返回值，所以是void。<E>出现在返回值前，表示声明E变量
     *
     * @param e
     * @param <E>
     */

    public <E> void methodWithoutReturn(E e) {
    }

    /**
     * 泛型方法，有返回值。入参和返回值都是V。注意，即使这个方法也用E，也和上面的E不是同一个
     *
     * @param v
     * @param <V>
     * @return
     */

    public <V> V methodWithReturn(V v) {
        return v;
    }

}