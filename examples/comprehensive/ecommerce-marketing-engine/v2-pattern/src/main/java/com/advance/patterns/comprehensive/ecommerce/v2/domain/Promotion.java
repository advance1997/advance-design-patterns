package com.advance.patterns.comprehensive.ecommerce.v2.domain;

import java.math.BigDecimal;

/**
 * 不可变的营销活动配置，规则表达式与优惠算法参数分开保存。
 *
 * @author advance
 * @since 2026-10-29 13:45:00
 */
public class Promotion {
    /** 活动唯一编码。 */
    private final String code;
    /** 面向用户展示的活动名称。 */
    private final String name;
    /** 控制优惠券是否可使用的条件表达式。 */
    private final String conditionRule;
    /** 选择优惠计算策略的类型。 */
    private final DiscountType discountType;
    /** 固定金额或折扣比例。 */
    private final BigDecimal discountValue;
    /** 比例优惠的最高金额限制；固定金额类型不使用此字段。 */
    private final BigDecimal maxDiscount;

    /**
     * 创建经过基础参数校验的营销活动。
     *
     * @param code 活动编码
     * @param name 活动名称
     * @param conditionRule 规则 DSL 表达式
     * @param discountType 优惠计算类型
     * @param discountValue 固定金额或折扣比例
     * @param maxDiscount 最大优惠金额；固定金额类型可传零
     * @throws IllegalArgumentException 活动标识、规则或优惠参数不合法时抛出
     */
    public Promotion(String code, String name, String conditionRule,
            DiscountType discountType, BigDecimal discountValue,
            BigDecimal maxDiscount) {
        if (code == null || code.trim().isEmpty() || name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("活动编码和名称不能为空");
        }
        if (conditionRule == null || conditionRule.trim().isEmpty()) {
            throw new IllegalArgumentException("优惠条件表达式不能为空");
        }
        if (discountType == null || discountValue == null
                || discountValue.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("优惠类型或优惠参数不合法");
        }
        if (discountType == DiscountType.PERCENTAGE
                && (discountValue.compareTo(BigDecimal.ONE) > 0
                || maxDiscount == null || maxDiscount.compareTo(BigDecimal.ZERO) < 0)) {
            throw new IllegalArgumentException("折扣比例或最高优惠金额不合法");
        }
        this.code = code;
        this.name = name;
        this.conditionRule = conditionRule;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.maxDiscount = maxDiscount;
    }

    /**
     * 获取活动编码。
     *
     * @return 活动编码
     */
    public String getCode() {
        return code;
    }

    /**
     * 获取活动名称。
     *
     * @return 活动名称
     */
    public String getName() {
        return name;
    }

    /**
     * 获取规则 DSL。
     *
     * @return 活动条件表达式
     */
    public String getConditionRule() {
        return conditionRule;
    }

    /**
     * 获取优惠计算策略类型。
     *
     * @return 优惠类型
     */
    public DiscountType getDiscountType() {
        return discountType;
    }

    /**
     * 获取固定优惠金额或比例参数。
     *
     * @return 优惠参数
     */
    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    /**
     * 获取比例策略的封顶金额。
     *
     * @return 最大优惠金额；固定金额策略忽略该字段
     */
    public BigDecimal getMaxDiscount() {
        return maxDiscount;
    }
}
