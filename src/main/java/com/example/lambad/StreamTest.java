package com.example.lambad;

import jdk.management.resource.internal.inst.SocketOutputStreamRMHooks;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

public class StreamTest {
    private static List<Person> list;

    static {
        list = new ArrayList<>();
        list.add(new Person("i", 18, "杭州", 999.9));
        list.add(new Person("am", 19, "温州", 777.7));
        list.add(new Person("seay", 21, "杭州", 888.8));
        list.add(new Person("you", 17, "宁波", 888.8));
    }

    public static void main(String[] args) {

        /**
         * 所以collect()、Collector、Collectors三者的关系是：
         * collect()通过传入不同的Collector对象来明确如何收集元素，
         * 比如收集成List还是Set还是拼接字符串？而通常我们不需要自己实现Collector接口，
         * 只需要通过Collectors获取。
         * 这倒颇有点像Executor和Executors的关系，一个是线程池接口，一个是线程池工具类。
         */
        // 1.先获取流（不用管其他乱七八糟的创建方式，记住这一个就能应付95%的场景）
              /*  list.stream()
                .filter(Person -> Person.getAge() > 20)//过滤调 年龄大于20的
                .map(Person::getName)// // 3.只要名字，不需要整个Person对象（为什么在这个案例中，filter只能用Lambda，map却可以用方法引用？）
                .collect(Collectors.toList());//collect()是用来收集处理后的元素的，它有两个重载的方法：*/

        //二    sorted  排序
        // JDK8之前：Collections工具类+匿名内部类。Collections类似于Arrays工具类，我经常用Arrays.asList(）
        Collections.sort(list, new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {
                return p1.getName().length() - p2.getName().length();
            }
        });

        // JDK8之前：List本身也实现了sort()
        list.sort(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getName().length() - o2.getName().length();
            }
        });

        // JDK8之后：Lambda传参给Comparator接口，其实就是实现Comparator#compare()。注意，equals()是Object的，不妨碍
        list.sort((p1, p2) -> p1.getName().length() - p2.getName().length());

        //  // JDK8之后：使用JDK1.8为Comparator接口新增的comparing()方法
        list.sort(Comparator.comparing(p -> p.getName().length()));
        List<String> result2 = list.stream()
                .filter(person -> person.getAge() > 18)
                .sorted(Comparator.comparing(Person::getName, String::compareTo).reversed())
                .map(Person::getName)
                .collect(Collectors.toList());
        System.out.println("排序= " + result2);


        /**
         *   二    limit/skip  分割 跳过
         *   peek()
         *   它接受一个Consumer，一般有两种用法：
         * • 设置值
         * • 观察数据peek这个单词本身就带有“观察”的意思。
         */

        List<String> collect = list.stream()
                .filter(person -> person.getAge() > 17)
                .peek(person -> System.out.println("peek =" + person))//先不用管，它不会影响整个流程，就是打印看看filter操作后还剩什么元素
//                .peek(person -> person.setAge(18)).forEach(System.out::println)//也就是把所有人的年龄设置为18岁。
                .skip(1)
                .limit(2)
                .map(Person::getName)
                .collect(Collectors.toList());

        System.out.println("limit/skip  分割 跳过 " + collect);


        //二    collect
        //        collect()是最重要、最难掌握、同时也是功能最丰富的方法。
        //        最常用的4个方法：Collectors.toList()、Collectors.toSet()、Collectors.toMap()、Collectors.joining()

        // 1  把结果收集为List
        List<String> listCollect = list.stream().map(Person::getName).collect(Collectors.toList());
        System.out.println(" 把结果收集为List " + listCollect);

        // 2 把结果收集为Set
        Set<String> setCollect1 = list.stream().map(Person::getAddress).collect(Collectors.toSet());
        System.out.println(" 把结果收集为Set " + setCollect1);

        /** 3 把结果收集为Map，前面的是key，后面的是value，如果你希望value是具体的某个字段，
         * 可以改为toMap(Person::getName, person -> person.getAge())
         *
         * 关于collect收集成Map的操作，有一个小坑需要注意：
         * Exception in thread "main" java.lang.IllegalStateException: Duplicate key StreamTest.Person(name=seay, age=21, address=杭州, salary=888.8)
         * 因为toMap()不允许key重复，我们必须指定key冲突时的解决策略（比如，保留已存在的key）：
         *保留已存在的key  .collect(Collectors.toMap(Person::getName, person -> person, (preKey, nextKey) -> preKey));
         * 如果你希望key覆盖，可以把(preKey, nextKey) -> preKey)换成(preKey, nextKey) -> nextKey)。
         * 你可能会在同事的代码中发现另一种写法：
         * Map<String, Person> nameToPersonMap = list.stream().collect(Collectors.toMap(Person::getName, Function.identity());
         * 但它依然没有解决key冲突的问题，而且对于大部分人来说，相比person->person，Function.identity()的可读性不佳。
         */


        Map<String, Person> map = list.stream().collect(Collectors.toMap(Person::getName, person -> person));
        System.out.println(" 把结果收集为Map " + map);

        // 把结果收集起来，并用指定分隔符拼接
        String joiningCollect = list.stream().map(Person::getAddress).collect(Collectors.joining("-"));
        System.out.println(" 把结果收集起来，并用指定分隔符拼接 " + joiningCollect);

        // 五类：聚合：max/min/count   最大 最小 统计

        // 匿名内部类的方式，实现Comparator，明确按什么规则比较（所谓最大，必然是在某种规则下的最值）
        Optional<Integer> max = list.stream().map(Person::getAge).max(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        System.out.println("max=" + max);

        //流
        Optional<Double> max1 = list.stream().map(Person::getSalary).max(Double::compareTo);
        System.out.println("Double max1 = " + max1.orElse(null));
        //高阶操作 collect
        Optional<Person> maxCollect = list.stream().collect(Collectors.maxBy((p1, p2) -> p1.getAge() - p2.getAge()));
        System.out.println("高阶操作 collect " + maxCollect.orElse(null));

        // 方式3：方法引用 IDEA建议直接使用 max()，不要用 collect(Collector)
        Optional<Person> collect2 = list.stream().max(Comparator.comparing(Person::getAge));
        System.out.println("高阶操作 方法引用 collect " + collect2.orElse(null));

        // 剩下的minBy和counting
        Optional<Person> collect3 = list.stream().collect(Collectors.minBy(Comparator.comparing(Person::getAge)));
        System.out.println("高阶操作 最小 方法引用 collect " + collect3.orElse(null));
        Optional<Person> min = list.stream().min(Comparator.comparing(Person::getAge));
        System.out.println("高阶操作 最小 方法引用 collect " + min.orElse(null));

        /**
         *  六类  去重：distinct
         *
         * 所谓“去重”，就要明确怎样才算“重复”。那么，distinct()是基于什么标准呢？
         * 还是那两样：hashCode()和equals()，所以记得重写这两个方法（一般使用Lombok的话问题不大）。
         * distinct()提供的去重功能比较简单，就是判断对象重复。如果希望实现更细粒度的去重，比如根据对象的某个属性去重，可以怎么做呢？
         *  https://www.jianshu.com/p/32daa76ea23f
         */

        List<String> count = list.stream().map(Person::getAddress).distinct().collect(Collectors.toList());//地址去重
        List<Person> collect1 = list.stream().map(person -> person).distinct().collect(Collectors.toList());//对象去重
        System.out.println("去重 " + count);
        System.out.println("去重 " + collect1);

        /**
         *  分组
         *
         */
        // GROUP BY address
        Map<String, List<Person>> collect4 = list.stream().collect(Collectors.groupingBy(Person::getAddress));
        System.out.println("分组 collect4" + collect4);

        // GROUP BY address, age
        // // 简单来说，就是collect(groupingBy(xx)) 扩展为 collect(groupingBy(xx, groupingBy(yy)))，嵌套分组
        Map<String, Map<Integer, List<Person>>> collect5 =
                list.stream().collect(Collectors.groupingBy(Person::getAddress, Collectors.groupingBy(Person::getAge)));
        System.out.println("分组 collect5" + collect5);

        // 解决了按字段分组、按多个字段分组，我们再考虑一个问题：有时我们分组的条件不是某个字段，而是某个字段是否满足xx条件
        // 比如 年龄大于等于18的是成年人，小于18的是未成年人
        Map<Boolean, List<Person>> collect6 = list.stream().collect(Collectors.partitioningBy(person -> person.getAge() > 18));
        System.out.println("判断并 分组 collect6" + collect6);

        /**
         * 统计
         */
        // 平均年龄
        Double collect7 = list.stream().collect(Collectors.averagingInt(Person::getAge));
        System.out.println("平均年龄 " + collect7);
        Double collect8 = list.stream().collect(Collectors.averagingDouble(Person::getSalary));
        System.out.println("平均薪资 " + collect8);
        // 有个更绝的，针对某项数据，一次性返回多个纬度的统计结果：总和、平均数、最大值、最小值、总数，但一般用的很少
        IntSummaryStatistics collect9 = list.stream().collect(Collectors.summarizingInt(Person::getAge));
        System.out.println("一次性返回多个纬度的统计结果 " + collect9);

        //遍历
        // 遍历操作，接收Consumer
        list.stream().forEach(System.out::println);
        // 简化，本质上不算同一个方法的简化
        list.forEach(System.out::println);


        //练习
        // 要求分组统计出各个城市的年龄总和，返回格式为 Map<String, Integer>.
        Map<String, IntSummaryStatistics> collect10 =
                list.stream().collect(Collectors.groupingBy(Person::getAddress, Collectors.summarizingInt(Person::getAge)));
        System.out.println("分组统计出各个城市的年龄总和 " + collect10);

        //// 分组统计 要求得到Map<城市, List<用户工资>>
        Map<String, List<Double>> collect11 =
                list.stream().collect(Collectors.groupingBy(Person::getAddress, Collectors.mapping(Person::getSalary, Collectors.toList())));

        System.out.println("分组统计出各个城市的年龄总和 " + collect11);
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


