package com.example.lambad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class ConvertUtil类型转换 {

    private ConvertUtil类型转换() {
    }

    /**
     * 将List转为Map
     *
     * @param list         原数据
     * @param keyExtractor Key的抽取规则
     * @param <K>          Key
     * @param <V>          Value
     * @return
     */
    public static <K, V> Map<K, V> listToMap(List<V> list, Function<V, K> keyExtractor) {
        if (list == null || list.isEmpty()) {
            return new HashMap<>();
        }
        Map<K, V> map = new HashMap<>(list.size());
        for (V element : list) {
            K key = keyExtractor.apply(element);
            if (key == null) {
                continue;
            }
            map.put(key, element);
        }
        return map;
    }

    /**
     * 将List转为Map，可以指定过滤规则
     *
     * @param list         原数据
     * @param keyExtractor Key的抽取规则
     * @param predicate    过滤规则
     * @param <K>          Key
     * @param <V>          Value
     * @return
     */
    public static <K, V> Map<K, V> listToMap(List<V> list, Function<V, K> keyExtractor, Predicate<V> predicate) {
        if (list == null || list.isEmpty()) {
            return new HashMap<>();
        }
        Map<K, V> map = new HashMap<>(list.size());
        for (V element : list) {
            K key = keyExtractor.apply(element);
            if (key == null || !predicate.test(element)) {
                continue;
            }
            map.put(key, element);
        }
        return map;
    }

    /**
     * 将List映射为List，比如List<Person> personList转为List<String> nameList
     *
     * @param originList 原数据
     * @param mapper     映射规则
     * @param <T>        原数据的元素类型
     * @param <R>        新数据的元素类型
     * @return
     */
    public static <T, R> List<R> resultToList(List<T> originList, Function<T, R> mapper) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        List<R> newList = new ArrayList<>(originList.size());
        for (T originElement : originList) {
            R newElement = mapper.apply(originElement);
            if (newElement == null) {
                continue;
            }
            newList.add(newElement);
        }
        return newList;
    }

    /**
     * 将List映射为List，比如List<Person> personList转为List<String> nameList
     * 可以指定过滤规则
     *
     * @param originList 原数据
     * @param mapper     映射规则
     * @param predicate  过滤规则
     * @param <T>        原数据的元素类型
     * @param <R>        新数据的元素类型
     * @return
     */
    public static <T, R> List<R> resultToList(List<T> originList, Function<T, R> mapper, Predicate<T> predicate) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        List<R> newList = new ArrayList<>(originList.size());
        for (T originElement : originList) {
            R newElement = mapper.apply(originElement);
            if (newElement == null || !predicate.test(originElement)) {
                continue;
            }
            newList.add(newElement);
        }
        return newList;
    }

    // ---------- 以下是测试案例 ----------

    private static List<Person> list;

    static {
        list = new ArrayList<>();
        list.add(new Person("i", 18, "杭州", 999.9));
        list.add(new Person("am", 19, "温州", 777.7));
        list.add(new Person("iron", 21, "杭州", 888.8));
        list.add(new Person("man", 17, "宁波", 888.8));
    }

    public static void main(String[] args) {
        Map<String, Person> nameToPersonMap = listToMap(list, Person::getName);
        System.out.println(nameToPersonMap);

        Map<String, Person> personGt18 = listToMap(list, Person::getName, person -> person.getAge() >= 18);
        System.out.println(personGt18);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Person {
        private String name;
        private Integer age;
        private String address;
        private Double salary;
    }
}