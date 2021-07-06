package com.example.lambad;

public class Functional函数式接口类型 {

    /**
     * 给函数式接口赋值的格式：
     * 函数式接口 = 入参 -> 出参/制造出参的语句
     * 函数式接口的作用
     * 实际开发中，出入参类型的排列组合是有限的，所以JDK干脆内置了一部分函数式接口。
     * 一般来说，我们只需熟练掌握以下4大核心函数式接口即可：
     * • 消费型接口 Consumer<T>    void accept(T t)
     * • 供给型接口 Supplier<T>       T get()
     * • 函数型接口 Function<T, R>   R apply(T t)
     * • 断定型接口 Predicate<T>     boolean test(T t)
     * <p>
     * <p>
     * • 消费型接口 Consumer<T>     T t -> void        例：x -> System.out.println(x)
     * • 供给型接口 Supplier<T>       () -> T t            例：() -> {return 1+2;}
     * • 映射型接口 Function<T, R>   T t -> R r          例：user -> user.getAge()，T是入参，R是出参
     * • 断定型接口 Predicate<T>     T t -> boolean    例：user -> user.getAge()>18
     * Consumer，消费型接口，是黑洞，只管吃(C)，不会往外吐，所以只有入参，没有返回值。
     * Supplier，供给型接口，是泉眼，不断往外涌出泉水(S)，不需要入参，有返回值。
     * Function，映射型接口，就是高中数学的函数映射，x --> f(x) --> result，所以有入参，也有返回值。
     * Predicate，断定型接口，是特殊的映射函数，x --> f(x) --> boolean，只会评(P)论true/false。
     *
     * @param args
     */
    public static void main(String[] args) {
        FunctionalInterface1 interface1 = str -> System.out.println(str);
        FunctionalInterface2 interface2 = () -> "abc";
        FunctionalInterface3 interface3 = str -> Integer.parseInt(str);
        FunctionalInterface4 interface4 = str -> str.length() > 3;
    }


    /**
     * 消费型，吃葡萄不吐葡萄皮：有入参，无返回值
     * (T t) -> {}
     */
    interface FunctionalInterface1 {
        void method(String str);
    }

    /**
     * 供给型，无中生有：没有入参，却有返回值
     * () -> T t
     */
    interface FunctionalInterface2 {
        String method();
    }

    /**
     * 映射型，转换器：把T转成R返回
     * T t -> R r
     */
    interface FunctionalInterface3 {
        int method(String str);
    }

    /**
     * 特殊的映射型：把T转为boolean
     * T t -> boolean
     */
    interface FunctionalInterface4 {
        boolean method(String str);
    }
}
