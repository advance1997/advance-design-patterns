package com.advance.patterns.comprehensive.ecommerce.v2;

import com.advance.patterns.comprehensive.ecommerce.v2.catalog.InMemoryPromotionCatalog;
import com.advance.patterns.comprehensive.ecommerce.v2.discount.DiscountStrategyRegistry;
import com.advance.patterns.comprehensive.ecommerce.v2.domain.OrderContext;
import com.advance.patterns.comprehensive.ecommerce.v2.engine.PromotionEngine;
import com.advance.patterns.comprehensive.ecommerce.v2.rule.RuleAstPool;
import java.math.BigDecimal;

/**
 * 组装四种设计模式的协作对象并运行专题中的两笔订单。
 *
 * @author advance
 * @since 2026-10-29 13:45:00
 */
public class MarketingEngineApplication {
    /**
     * 装配引擎依赖并计算 VIP 订单和新客电子产品订单。
     */
    public void runScenarios() {
        PromotionEngine engine = new PromotionEngine(new RuleAstPool(),
                new InMemoryPromotionCatalog(), new DiscountStrategyRegistry());
        System.out.println("=== 订单1001：VIP用户购买600元商品 ===");
        engine.evaluateOrder(new OrderContext("1001", new BigDecimal("600"),
                true, false, false));
        System.out.println("\n=== 订单1002：新用户购买300元电子产品 ===");
        engine.evaluateOrder(new OrderContext("1002", new BigDecimal("300"),
                false, true, true));
    }
}
