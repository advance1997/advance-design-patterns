package com.advance.patterns.comprehensive.ecommerce.v2.domain;

import java.math.BigDecimal;

/**
 * 一次优惠计算所需的订单与用户条件；实例状态由单次请求持有，不放入共享 AST。
 *
 * @author advance
 * @since 2026-10-29 13:45:00
 */
public class OrderContext {
    /** 订单编号。 */
    private final String orderId;
    /** 优惠前订单总金额。 */
    private final BigDecimal amount;
    /** 用户是否为 VIP。 */
    private final boolean vip;
    /** 当前订单是否为用户首单。 */
    private final boolean firstOrder;
    /** 订单是否包含电子产品。 */
    private final boolean electronics;

    /**
     * 创建不可变订单上下文。
     *
     * @param orderId 订单编号
     * @param amount 优惠前订单金额，不得为负数
     * @param vip 是否为 VIP 用户
     * @param firstOrder 是否为首单
     * @param electronics 是否包含电子产品
     * @throws IllegalArgumentException 订单编号为空或金额为负时抛出
     */
    public OrderContext(String orderId, BigDecimal amount, boolean vip,
            boolean firstOrder, boolean electronics) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("订单编号不能为空");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("订单金额不能为空或小于零");
        }
        this.orderId = orderId;
        this.amount = amount;
        this.vip = vip;
        this.firstOrder = firstOrder;
        this.electronics = electronics;
    }

    /**
     * 获取订单编号。
     *
     * @return 订单编号
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 获取优惠前订单金额。
     *
     * @return 订单总金额
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 判断用户是否为 VIP。
     *
     * @return VIP 用户返回 true
     */
    public boolean isVip() {
        return vip;
    }

    /**
     * 判断当前订单是否为首单。
     *
     * @return 首单返回 true
     */
    public boolean isFirstOrder() {
        return firstOrder;
    }

    /**
     * 判断订单是否包含电子产品。
     *
     * @return 包含电子产品返回 true
     */
    public boolean hasElectronics() {
        return electronics;
    }
}
