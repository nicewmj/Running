package com.example.timeAPI;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;


public class NewDateApiTest {
    public static void main(String[] args) {

        //旧的方式  Mon Jun 07 09:40:11 CST 2021
        Date date = new Date();
        System.out.println("old api " + date);


        //新的方式 直接得到 yyyy-mm-dd
        LocalDate localDate = LocalDate.now();
        System.out.println("new api " + localDate);
        //// 时间 16:30:00:000
        LocalTime localTime = LocalTime.now();
        System.out.println("new localTime api " + localDate);

        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println("new localDateTime api  " + localDateTime);

        //组合
        LocalDateTime localDateTime1 = LocalDateTime.of(localDate, localTime);
        System.out.println("new localTime 组合api  " + localDateTime1);

        // 可以指定时间
        LocalDate date1 = LocalDate.of(2021, 3, 4);
        System.out.println("指定时间 of  " + date1);
        LocalTime time = LocalTime.of(9, 05, 03);
        System.out.println("指定时间 of  " + time);
        LocalDateTime localDateTime2 = LocalDateTime.of(2021, 03, 04, 9, 05, 03);
        System.out.println("指定时间 of  " + localDateTime2);


        // 获取时间参数的年、月、日（有时需求要用到）
        System.out.println("获取时间参数的年、月、日：");
        LocalDateTime now = LocalDateTime.now();
        System.out.println("year:" + now.getYear());
        System.out.println("month:" + now.getMonth());
        System.out.println("day:" + +now.getDayOfMonth());
        System.out.println("hour:" + now.getHour());
        System.out.println("minute:" + now.getMinute());
        System.out.println("second:" + now.getSecond() + "\n");

        // 计算昨天的同一时刻（由于对象不可修改，所以返回的是新对象）
        System.out.println("计算前一天的当前时刻：");
        LocalDateTime today = LocalDateTime.now();
//        LocalDateTime yesterday = today.plus(-1, ChronoUnit.DAYS);//today: 2021-06-07T10:34:43.757
        //也可以使用api minusDays（）还有很多 点出来看看
        LocalDateTime yesterday = today.minusDays(1);
        System.out.println("today: " + today);
        System.out.println("yesterday: " + yesterday);
        System.out.println("same object: " + today.equals(yesterday) + "\n");

        // 计算当天的00点和24点（你看，这里就看到组合的威力了）
        System.out.println("计算当天的00点和24点：");
        LocalDateTime todayBegin = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        System.out.println("todayBegin: " + todayBegin);
        System.out.println("todayEnd: " + todayEnd + "\n");

        System.out.println("计算一周、一个月、一年前的当前时刻：");
        LocalDateTime oneWeekAgo = today.minus(1, ChronoUnit.WEEKS);
        LocalDateTime oneMonthAgo = today.minus(1, ChronoUnit.MONTHS);
        LocalDateTime oneYearAgo = today.minus(1, ChronoUnit.YEARS);
        System.out.println("oneWeekAgo" + oneWeekAgo);
        System.out.println("oneMonthAgo" + oneMonthAgo);
        System.out.println("oneYearAgo" + oneYearAgo + "\n");

        //如何修改时间：with           从广义上来说，修改时间和增减时间是一样的，但稍微有点区别
        LocalDateTime now1 = LocalDateTime.now();
        System.out.println("now1:" + now1);
        //注意一下，之前我们plus时用的是ChronoUnit，表示增幅单位，而这里ChronoField则是指定修改的字段。
        //同样的，上面演示的也是最最通用的API，大家也可以用以下方法：
        // 将day修改为6号
        LocalDateTime localDateTime3 = now1.withDayOfMonth(6);
        LocalDateTime with = now1.with(ChronoField.DAY_OF_MONTH, 6);
        System.out.println("modifiedDateTime1:" + localDateTime3);
        System.out.println("modifiedDateTime2:" + with);


        /**
         * 如何比较时间：isAfter/isBefore/isEqual
         */
        LocalDateTime now2 = LocalDateTime.now();
        LocalDateTime after = now2.plusSeconds(1);
        boolean after1 = after.isAfter(now2);
        System.out.println("result=" + after1);
        boolean before = after.isBefore(now2);
        System.out.println("result before =" + before);
        boolean equal = after.isEqual(now2);
        System.out.println("result equal =" + equal);

        /**
         *  时区：zone
         */

        // 当地时间
        LocalDateTime now3 = LocalDateTime.now();
        System.out.println("localDateTime:" + now3);
        // 时区（id的形式），默认的是本国时区
        ZoneId zoneId = ZoneId.systemDefault();
        // 为localDateTime补充时区信息
        ZonedDateTime beijingTime = now3.atZone(zoneId);
        System.out.println("beijingTime:" + beijingTime);

        /**
         * 差点忘了，转成秒也非常常用哟：
         */
        Date date2 = new Date();
        System.out.println(date2.getTime() / 1000);
        LocalDateTime now5 = LocalDateTime.now();
        long result = now5.toEpochSecond(ZoneOffset.ofHours(8));
        System.out.println(result);
        LocalDateTime localDateTime5 = LocalDateTime.ofEpochSecond(result, 0, ZoneOffset.ofHours(8));
        System.out.println(localDateTime5);

        /**
         * 6 格式化 与 反格式化
         * 简而言之，一个format()，一个parse()，都要传入DateTimeFormatter对象，可以自定义也可以使用默认的。
         */

        LocalDateTime now6 = LocalDateTime.now();
        System.out.println("格式化前:" + now6);
        String format = now6.format(DateTimeFormatter.ISO_DATE_TIME);
        System.out.println("默认格式:" + format);

        String other = now6.format(DateTimeFormatter.BASIC_ISO_DATE);
        System.out.println("其他格式:" + other);

        String dateTimeFormatter = now6.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("其他格式 dateTimeFormatter :" + dateTimeFormatter);

        LocalDateTime parse = LocalDateTime.parse(dateTimeFormatter, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("反格式化:" + parse);
    }
}
