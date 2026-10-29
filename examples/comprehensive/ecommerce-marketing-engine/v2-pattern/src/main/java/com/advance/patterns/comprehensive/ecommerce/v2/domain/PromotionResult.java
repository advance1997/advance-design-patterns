package com.advance.patterns.comprehensive.ecommerce.v2.domain;

import java.math.BigDecimal;

/**
 * 一张优惠券对一次订单的资格判断和优惠金额结果。
 *
 * @author advance
 * @since 2026-10-29 13:45:00
 */
public class PromotionResult {
    /** 被评估的活动。 */
    private final Promotion promotion;
    /** 订单是否满足活动使用条件。 */
    private final boolean eligible;
    /** 满足条件时可获得的优惠金额。 */
    private final BigDecimal discountAmount;

    /**
     * 创建单券评估结果。
     *
     * @param promotion 被评估活动
     * @param eligible 是否满足使用条件
     * @param discountAmount 实际优惠金额，不满足时为零
     */
    public PromotionResult(Promotion promotion, boolean eligible,
            BigDecimal discountAmount) {
        this.promotion = promotion;
        this.eligible = eligible;
        this.discountAmount = discountAmount;
    }

    /**
     * 获取被评估活动。
     *
     * @return 营销活动
     */
    public Promotion getPromotion() {
        return promotion;
    }

    /**
     * 查询该活动是否可用。
     *
     * @return 可用时返回 true
     */
    public boolean isEligible() {
        return eligible;
    }

    /**
     * 获取本活动优惠金额。
     *
     * @return 优惠金额
     */
    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }
}
