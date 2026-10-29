/**
 * 电商营销优惠引擎综合实战的四模式协作版本。
 *
 * <p>本版本延续 VIP 用户购买 600 元商品和新用户购买 300 元电子产品两笔模拟订单，
 * 并按“优惠券互斥、选择优惠金额最高的一张”完成活动评估。活动配置由内存目录提供，
 * 用模拟数据代替 MySQL。</p>
 *
 * <p>本包通过四种设计模式分离营销规则与优惠算法：</p>
 * <ul>
 *     <li>解释器模式：{@code RuleParser} 将优惠规则 DSL 解析为表达式树，
 *     各 {@code Expression} 节点解释当前订单的活动资格。</li>
 *     <li>组合模式：条件叶子与 AND、OR、NOT 节点实现统一的 {@code Expression} 接口，
 *     递归构成可解释的规则 AST。</li>
 *     <li>享元模式：{@code RuleAstPool} 按规则文本复用不可变 AST；订单上下文仅在解释时传入，
 *     不存储在共享表达式中。</li>
 *     <li>策略模式：{@code DiscountStrategyRegistry} 按活动优惠类型选择固定立减或比例封顶算法，
 *     仅负责计算优惠金额，不负责资格判断。</li>
 * </ul>
 *
 * <p>{@code PromotionEngine} 协调规则判断、优惠计算及互斥优惠券择优。最优券选择是本案例的业务政策，
 * 不属于上述模式自动提供的行为。</p>
 *
 * @author advance
 * @since 2026-10-29 13:45:00
 */
package com.advance.patterns.comprehensive.ecommerce.v2;
