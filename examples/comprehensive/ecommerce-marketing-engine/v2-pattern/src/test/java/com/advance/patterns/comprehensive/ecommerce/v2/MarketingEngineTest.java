package com.advance.patterns.comprehensive.ecommerce.v2;

import com.advance.patterns.comprehensive.ecommerce.v2.catalog.InMemoryPromotionCatalog;
import com.advance.patterns.comprehensive.ecommerce.v2.domain.OrderContext;
import com.advance.patterns.comprehensive.ecommerce.v2.expression.Expression;
import com.advance.patterns.comprehensive.ecommerce.v2.rule.RuleAstPool;
import com.advance.patterns.comprehensive.ecommerce.v2.rule.RuleParser;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

/**
 * 验证优惠引擎中的规则解释、AST 共享和策略计算协作。
 *
 * @author advance
 * @since 2026-10-29 13:45:00
 */
public class MarketingEngineTest {
    /**
     * 输入 VIP 600 元订单；期望两张 VIP 券规则均成立，固定券优惠20元、折扣券封顶50元，
     * 最终选择后者，实付550元。
     */
    @Test
    public void shouldCalculateBothExampleOrders() {
        System.out.println("期望：VIP订单选择折扣券优惠50元并实付550元；");
        System.out.println("新客电子产品订单优惠30元并实付270元");
        new MarketingEngineApplication().runScenarios();
    }

    /**
     * 输入相同 VIP 规则两次并解析包含 NOT/AND/OR 的规则；期望共享同一 AST，
     * 且 NOT 组合表达式解释为 true。
     */
    @Test
    public void shouldReuseImmutableAstAndInterpretNestedBooleanRules() {
        InMemoryPromotionCatalog catalog = new InMemoryPromotionCatalog();
        RuleAstPool pool = new RuleAstPool();
        String firstRule = catalog.findAll().get(0).getConditionRule();
        String secondRule = catalog.findAll().get(1).getConditionRule();
        Expression firstAst = pool.get(firstRule);
        Expression secondAst = pool.get(secondRule);
        System.out.println("期望：VIP 两张券复用同一 AST 对象：true；相同规则只解析一次");
        System.out.println("实际 AST 对象相同：" + (firstAst == secondAst));

        Expression expression = new RuleParser("!vip && (amount500 || electronics)").parse();
        boolean result = expression.interpret(new OrderContext("1002",
                new BigDecimal("300"), false, true, true));
        System.out.println("期望：!vip && (amount500 || electronics) = true");
        System.out.println("实际规则结果：" + result);
    }
}
