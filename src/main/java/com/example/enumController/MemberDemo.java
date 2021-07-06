package com.example.enumController;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author qiyu
 * @date 2020-09-26 02:57
 */
public class MemberDemo {
    public static void main(String[] args) {
        User user = new User(1L, "bravo", MemberEnum.GOLD_MEMBER.getType());
        BigDecimal productPrice = new BigDecimal("1000");
        BigDecimal discountedPrice = calculateFinalPrice(productPrice, user.getMemberType());
        System.out.println(discountedPrice);
    }

    /**
     * 根据会员身份返回折扣后的商品价格
     *
     * @param originPrice
     * @param user
     * @return
     */
    public static BigDecimal calculateFinalPrice(BigDecimal originPrice, Integer type) {
        return MemberEnum.getEnumByType(type).calculateFinalPrice(originPrice);
    }
}

@Data
@AllArgsConstructor
class User {
    private Long id;
    private String name;
    /**
     * 会员身份
     * 1：黄金会员，6折优惠
     * 2：白银会员，7折优惠
     * 3：青铜会员，8折优惠
     */
    private Integer memberType;
}

enum MemberEnum {
    // ---------- 把这几个枚举当做本应该在外面实现的MemberEnum之类，不要看成MemberEnum内部的 ----------
    GOLD_MEMBER(1, "黄金会员") {
        @Override
        public BigDecimal calculateFinalPrice(BigDecimal originPrice) {
            return originPrice.multiply(new BigDecimal(("0.6")));
        }
    },
    SILVER_MEMBER(2, "白银会员") {
        @Override
        public BigDecimal calculateFinalPrice(BigDecimal originPrice) {
            return originPrice.multiply(new BigDecimal(("0.7")));
        }
    },
    BRONZE_MEMBER(3, "青铜会员") {
        @Override
        public BigDecimal calculateFinalPrice(BigDecimal originPrice) {
            return originPrice.multiply(new BigDecimal(("0.8")));
        }
    },
    ;

    // ---------- 下面才是MemberEnum类的定义 ---------
    private final Integer type;
    private final String desc;

    MemberEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    /**
     * 定义抽象方法，留个子类实现
     *
     * @param originPrice
     * @return
     */
    protected abstract BigDecimal calculateFinalPrice(BigDecimal originPrice);

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static MemberEnum getEnumByType(Integer type) {
        MemberEnum[] values = MemberEnum.values();
        for (MemberEnum memberEnum : values) {
            if (memberEnum.getType().equals(type)) {
                return memberEnum;
            }
        }
        throw new IllegalArgumentException("Invalid Enum type:" + type);
    }
}