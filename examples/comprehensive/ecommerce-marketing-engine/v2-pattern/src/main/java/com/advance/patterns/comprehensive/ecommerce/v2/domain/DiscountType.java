package com.advance.patterns.comprehensive.ecommerce.v2.domain;

/**
 * 营销活动配置的优惠计算类型。
 *
 * @author advance
 * @since 2026-10-29 13:45:00
 */
public enum DiscountType {
    /** 固定金额立减。 */
    FIXED,
    /** 按比例计算并受最高优惠金额限制。 */
    PERCENTAGE
}
