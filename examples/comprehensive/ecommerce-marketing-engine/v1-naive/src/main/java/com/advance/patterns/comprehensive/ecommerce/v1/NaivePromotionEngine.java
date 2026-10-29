package com.advance.patterns.comprehensive.ecommerce.v1;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

/**
 * 使用优惠券类型分支同时处理资格规则和优惠计算的基线实现。
 *
 * @author advance
 * @since 2026-10-29 13:45:00
 */
public class NaivePromotionEngine {
    /**
     * 输出专题中的两笔订单，观察业务规则和优惠算法耦合在同一组分支中。
     */
    public void runScenarios() {
        System.out.println("=== V1 订单1001：VIP订单金额600元 ===");
        evaluate(new OrderContext("1001", new BigDecimal("600"), true, false, false));
        System.out.println("\n=== V1 订单1002：新客购买300元电子产品 ===");
        evaluate(new OrderContext("1002", new BigDecimal("300"), false, true, true));
    }

    /**
     * 在一个方法中按优惠券类型判断条件、计算优惠并选择最高优惠。
     *
     * @param order 当前模拟订单
     */
    private void evaluate(OrderContext order) {
        List<Coupon> coupons = Arrays.asList(
                new Coupon("VIP_FIXED", "VIP立减券"),
                new Coupon("VIP_PERCENT", "VIP折扣券"),
                new Coupon("NEW_USER", "新客折扣券"));
        Coupon bestCoupon = null;
        BigDecimal bestDiscount = BigDecimal.ZERO;
        for (Coupon coupon : coupons) {
            boolean eligible = false;
            BigDecimal discount = BigDecimal.ZERO;
            if ("VIP_FIXED".equals(coupon.type)) {
                eligible = order.vip && order.amount.compareTo(new BigDecimal("500")) >= 0;
                if (eligible) {
                    discount = new BigDecimal("20").min(order.amount);
                }
            } else if ("VIP_PERCENT".equals(coupon.type)) {
                eligible = order.vip && order.amount.compareTo(new BigDecimal("500")) >= 0;
                if (eligible) {
                    discount = order.amount.multiply(new BigDecimal("0.10"))
                            .min(new BigDecimal("50")).min(order.amount)
                            .setScale(2, RoundingMode.HALF_UP);
                }
            } else if ("NEW_USER".equals(coupon.type)) {
                eligible = order.firstOrder && (order.amount.compareTo(new BigDecimal("500")) >= 0
                        || order.electronics);
                if (eligible) {
                    discount = order.amount.multiply(new BigDecimal("0.10"))
                            .min(new BigDecimal("30")).min(order.amount)
                            .setScale(2, RoundingMode.HALF_UP);
                }
            }
            System.out.println("[优惠引擎/v1] NaivePromotionEngine.evaluate ---> "
                    + coupon.name + (eligible ? "符合条件" : "不符合条件")
                    + "，优惠金额：" + discount);
            if (eligible && discount.compareTo(bestDiscount) > 0) {
                bestCoupon = coupon;
                bestDiscount = discount;
            }
        }
        printResult(order, bestCoupon, bestDiscount);
    }

    /**
     * 输出选择的一张互斥优惠券及应付金额。
     *
     * @param order 当前订单
     * @param coupon 优惠金额最大的可用券；无可用券时为 null
     * @param discount 实际优惠金额
     */
    private void printResult(OrderContext order, Coupon coupon, BigDecimal discount) {
        BigDecimal payable = order.amount.subtract(discount);
        System.out.println("[优惠引擎/v1] NaivePromotionEngine.printResult ---> 订单："
                + order.orderId + "，最优优惠券："
                + (coupon == null ? "无" : coupon.name));
        System.out.println("[优惠引擎/v1] NaivePromotionEngine.printResult ---> 订单金额："
                + order.amount + "，优惠：" + discount + "，实付：" + payable);
    }

    /**
     * 保存判断优惠条件所需的订单外部状态。
     */
    static class OrderContext {
        /** 订单编号。 */
        private final String orderId;
        /** 优惠前订单金额。 */
        private final BigDecimal amount;
        /** 是否为 VIP 用户。 */
        private final boolean vip;
        /** 是否为用户首单。 */
        private final boolean firstOrder;
        /** 订单是否包含电子产品。 */
        private final boolean electronics;

        /**
         * 创建模拟订单上下文。
         *
         * @param orderId 订单编号
         * @param amount 优惠前订单金额
         * @param vip 是否为 VIP 用户
         * @param firstOrder 是否为首单
         * @param electronics 是否包含电子产品
         */
        OrderContext(String orderId, BigDecimal amount, boolean vip,
                boolean firstOrder, boolean electronics) {
            this.orderId = orderId;
            this.amount = amount;
            this.vip = vip;
            this.firstOrder = firstOrder;
            this.electronics = electronics;
        }
    }

    /**
     * 基线版本内的优惠券类型和名称。
     */
    static class Coupon {
        /** 分支识别码。 */
        private final String type;
        /** 面向用户的优惠券名称。 */
        private final String name;

        /**
         * 创建优惠券。
         *
         * @param type 分支使用的优惠券类型
         * @param name 展示名称
         */
        Coupon(String type, String name) {
            this.type = type;
            this.name = name;
        }
    }
}
