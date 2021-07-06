package com.example.lambad;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.stream.Stream;

public class StreamTest3 {
    public static void main(String[] args) {
        //匹配/查找
        //只要第一个，后续元素不会继续遍历。
/**   关于findFirst()和findAny()有没有人觉得这方法很傻逼？其实是我demo举的不对
 * ，通常来说应该是一堆filter操作任取一个等场景下使用。比如list.stream().filter(student->student.getAge()>18).findFirst()
 * ，即符合条件的任选一个。
 */
        Optional<Integer> integerStream =
                Stream.of(1, 2, 3, 4)
                        .peek(v -> System.out.println(v + ","))
                        .findFirst();
        System.out.println("查找第一个元素 " + integerStream);

        Optional<Integer> findAnyStream =
                Stream.of(1, 2, 3, 4)
                        .peek(v -> System.out.println(v + ","))
                        .findAny();
        System.out.println("查找所有元素 " + findAnyStream);

        /**由于是要allMatch，第一个就不符合，那么其他元素也就没必要测试了。这是一个短路操作。
         * 就好比：
         * if(0>1 && 2>1){
         *     // 2>1 不会被执行，因为0>1不成立，所以2>1被短路了
         * }
         */
        boolean b = Stream.of(1, 2, 3, 4).peek(v -> System.out.println(v + ",")).allMatch(v -> v > 2);
        System.out.println("判断 匹配 " + b);

        /**
         *和allMatch一样，它期望的是没有一个满足，而2>=2已经false，后面元素即使都大于2也不影响最终结果：noneMatch=false，所以也是个短路操作。
         */
        boolean a = Stream.of(1, 2, 3, 4).peek(v -> System.out.println(v + ",")).noneMatch(v -> v > 2);
        System.out.println("判断 匹配 " + a);
    }
}
