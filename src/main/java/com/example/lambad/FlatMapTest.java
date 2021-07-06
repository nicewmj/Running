package com.example.lambad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

public class FlatMapTest {

    public static void main(String[] args) {
        /**
         * 需求：
         * 1.要求返回所有的key，格式为 list<Long>      提示:keyset
         * 2.要求最终返回所有value，格式为 List<Long>   提示:flatMap()，Function需要啥你就转成啥
         *
         * @param args
         */
        Map<Long, List<Long>> map = new HashMap<>();
        map.put(1L, new ArrayList<>(Arrays.asList(1L, 2L, 3L)));
        map.put(2L, new ArrayList<>(Arrays.asList(4L, 5L, 6L)));

        List<Long> list = new ArrayList<>(map.keySet());
        System.out.println("key = " + list);
//        Collection<List<Long>> values = map.values();
//        Stream<Long> longStream = map.values().stream().flatMap(v -> v.stream());
        List<Long> collect = map.values().stream().flatMap(Collection::stream).collect(Collectors.toList());

        System.out.println("values = " + collect);


    }


}