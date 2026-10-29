package com.advance.patterns.comprehensive.ecommerce.v2.engine;

import com.advance.patterns.comprehensive.ecommerce.v2.catalog.InMemoryPromotionCatalog;
import com.advance.patterns.comprehensive.ecommerce.v2.discount.DiscountStrategy;
import com.advance.patterns.comprehensive.ecommerce.v2.discount.DiscountStrategyRegistry;
import com.advance.patterns.comprehensive.ecommerce.v2.domain.OrderContext;
import com.advance.patterns.comprehensive.ecommerce.v2.domain.Promotion;
import com.advance.patterns.comprehensive.ecommerce.v2.domain.PromotionResult;
import com.advance.patterns.comprehensive.ecommerce.v2.expression.Expression;
import com.advance.patterns.comprehensive.ecommerce.v2.rule.RuleAstPool;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 营销计算流程：解释使用条件，选择优惠策略，并在互斥活动中选择优惠最高的一张券。
 *
 * @author advance
 * @since 2026-10-29 13:45:00
 */
public class PromotionEngine {
    /** 用于共享不可变优惠规则 AST 的享元池。 */
    private final RuleAstPool ruleAstPool;
    /** 提供模拟数据库查询的活动目录。 */
    private final InMemoryPromotionCatalog catalog;
    /** 按活动优惠类型选择算法的策略注册表。 */
    private final DiscountStrategyRegistry strategyRegistry;

    /**
     * 创建营销计算引擎及其协作依赖。
     *
     * @param ruleAstPool AST 享元池
     * @param catalog 内存模拟活动目录
     * @param strategyRegistry 优惠计算策略注册表
     */
    public PromotionEngine(RuleAstPool ruleAstPool, InMemoryPromotionCatalog catalog,
            DiscountStrategyRegistry strategyRegistry) {
        this.ruleAstPool = ruleAstPool;
        this.catalog = catalog;
        this.strategyRegistry = strategyRegistry;
    }

    /**
     * 评估全部优惠券，按互斥规则选择优惠金额最大的可用券。
     *
     * @param order 当前订单及用户条件
     * @return 最优优惠券评估结果；没有可用券时返回 null
     */
    public PromotionResult evaluateOrder(OrderContext order) {
        System.out.println("[优惠引擎/v2] PromotionEngine.evaluateOrder ---> 开始评估订单："
                + order.getOrderId() + "，金额：" + order.getAmount());
        List<PromotionResult> results = new ArrayList<PromotionResult>();
        for (Promotion promotion : catalog.findAll()) {
            results.add(evaluatePromotion(order, promotion));
        }
        PromotionResult best = chooseBest(results);
        BigDecimal discount = best == null ? BigDecimal.ZERO : best.getDiscountAmount();
        BigDecimal payable = order.getAmount().subtract(discount);
        System.out.println("[优惠引擎/v2] PromotionEngine.evaluateOrder ---> 最优优惠券："
                + (best == null ? "无" : best.getPromotion().getName()));
        System.out.println("[优惠引擎/v2] PromotionEngine.evaluateOrder ---> 优惠："
                + discount + "，实付：" + payable);
        return best;
    }

    /**
     * 分别解释活动资格条件，并在符合条件后调用其计算策略。
     *
     * @param order 当前订单
     * @param promotion 待评估活动
     * @return 单张优惠券的评估结果
     */
    private PromotionResult evaluatePromotion(OrderContext order, Promotion promotion) {
        System.out.println("[优惠引擎/v2] PromotionEngine.evaluatePromotion ---> 评估："
                + promotion.getName());
        Expression expression = ruleAstPool.get(promotion.getConditionRule());
        boolean eligible = expression.interpret(order);
        BigDecimal discount = BigDecimal.ZERO;
        if (eligible) {
            DiscountStrategy strategy = strategyRegistry.get(promotion.getDiscountType());
            discount = strategy.calculate(order, promotion);
        }
        System.out.println("[优惠引擎/v2] PromotionEngine.evaluatePromotion ---> 资格："
                + eligible + "，优惠：" + discount);
        return new PromotionResult(promotion, eligible, discount);
    }

    /**
     * 从互斥优惠券中选择金额最高的一项，金额相同则保留先出现的活动。
     *
     * @param results 全部优惠券评估结果
     * @return 最大优惠结果；都不满足时返回 null
     */
    private PromotionResult chooseBest(List<PromotionResult> results) {
        PromotionResult best = null;
        for (PromotionResult result : results) {
            if (result.isEligible() && (best == null
                    || result.getDiscountAmount().compareTo(best.getDiscountAmount()) > 0)) {
                best = result;
            }
        }
        return best;
    }
}
