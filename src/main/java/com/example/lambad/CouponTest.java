package com.example.lambad;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

public class CouponTest {

    public static void main(String[] args) throws JsonProcessingException {
        List<CouponResponse> coupons = getCoupons();
        List<CouponInfo> respone = new ArrayList<>();
        // TODO 对优惠券统计数量
        //方法一：最直观
        Map<Long, CouponInfo> couponInfoMap = new HashMap<>();
        for (CouponResponse coupon : coupons) {
            //getOrDefault 获取map中的value值 如果该key 存在则返回该key的value值，不存在则返回自定义 默认的 值 new CouponInfo()
            CouponInfo couponInfo = couponInfoMap.getOrDefault(coupon.getId(), new CouponInfo());
            if (couponInfo.getNum() != null) {
                couponInfo.setNum(couponInfo.getNum() + 1);
                continue;
            }
            couponInfo.setCondition(coupon.getCondition());
            couponInfo.setDenominations(coupon.getDenominations());
            couponInfo.setId(coupon.getId());
            couponInfo.setName(coupon.getName());
            couponInfo.setNum(1);
            couponInfoMap.put(coupon.getId(), couponInfo);
            respone.add(couponInfo);
        }


        //方法二 :

        Map<Long, CouponInfo> collect = coupons.stream().collect(Collectors.toMap(
                CouponResponse::getId,
                couponResponse -> {
                    CouponInfo couponInfo = new CouponInfo();
                    couponInfo.setId(couponResponse.getId());
                    couponInfo.setName(couponResponse.getName());
                    couponInfo.setNum(1);
                    couponInfo.setCondition(couponResponse.getCondition());
                    couponInfo.setDenominations(couponResponse.getDenominations());
                    return couponInfo;
                },
                // // 相同id的优惠券，保留前者，并给前者num +1
                (pre, next) -> {
                    pre.setNum(pre.getNum() + 1);
                    return pre;

                }

        ));


        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(respone));
        System.out.println(collect);
    }

    private static List<CouponResponse> getCoupons() {
        return Arrays.asList(
                new CouponResponse(1L, "满5减4", 500L, 400L),
                new CouponResponse(1L, "满5减4", 500L, 400L),
                new CouponResponse(2L, "满10减9", 1000L, 900L),
                new CouponResponse(3L, "满60减50", 6000L, 5000L)
        );
    }

    @Data
    @AllArgsConstructor
    static class CouponResponse {
        private Long id;
        private String name;
        private Long condition;
        private Long denominations;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class CouponInfo {
        private Long id;
        private String name;
        private Integer num;
        private Long condition;
        private Long denominations;
    }
}