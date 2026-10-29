package com.advance.patterns.comprehensive.ecommerce.v1;

import org.junit.jupiter.api.Test;

/**
 * 对照原始分支版本的两笔营销订单结果。
 *
 * @author advance
 * @since 2026-10-29 13:45:00
 */
public class NaivePromotionEngineTest {
    /**
     * 输入 VIP 600 元订单和首单电子产品 300 元订单；期望分别选择 VIP 折扣券实付550元、
     * 新客折扣券实付270元。
     */
    @Test
    public void shouldCalculateExampleOrdersWithCoupledBranches() {
        System.out.println("期望：VIP订单最优优惠50元；新客电子产品订单优惠30元；");
        System.out.println("规则判断和优惠计算目前写在同一分支中");
        new NaivePromotionEngine().runScenarios();
    }
}
