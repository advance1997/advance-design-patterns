package com.advance.patterns.behavioral.strategy.v3.service;

import com.advance.patterns.behavioral.strategy.v3.domain.PaymentChannel;
import com.advance.patterns.behavioral.strategy.v3.domain.PaymentCommand;
import com.advance.patterns.behavioral.strategy.v3.domain.PaymentResult;
import com.advance.patterns.behavioral.strategy.v3.registry.PaymentStrategyRegistry;
import com.advance.patterns.behavioral.strategy.v3.strategy.AlipayStrategy;
import com.advance.patterns.behavioral.strategy.v3.strategy.PaymentStrategy;
import com.advance.patterns.behavioral.strategy.v3.strategy.WechatPayStrategy;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * 展示策略注册边界和新增算法；不使用断言，逐项人工对照输出。
 *
 * @author advance
 * @since 2026-10-27 18:35:37
 */
public class StrategyRegistryTest {
    /**
     * testNewChannel：观察装配、路由或扩展契约。
     * <p>期望结果：仅装配 UnionPayStrategy；输入 UNION_PAY、12.34 元；结果 UNION_PAY / UNION-001；PaymentService 不需要增加分支。</p>
     */
    @Test
    public void testNewChannel() {
        System.out.println("\ntestNewChannel ---> 期望：仅装配 UnionPayStrategy；输入 UNION_PAY、12.34 元；"
                + "结果 UNION_PAY / UNION-001；PaymentService 不需要增加分支");
        List<PaymentStrategy> strategies = new ArrayList<PaymentStrategy>();
        strategies.add(new UnionPayStrategy());
        PaymentService service = new PaymentService(new PaymentStrategyRegistry(strategies));
        PaymentResult result = service.pay(new PaymentCommand("PAY-001",
                new BigDecimal("12.34"), PaymentChannel.UNION_PAY));
        System.out.println("实际支付结果：" + result);
    }

    /**
     * testDuplicateChannel：观察装配、路由或扩展契约。
     * <p>期望结果：重复注册 UNION_PAY；IllegalArgumentException：渠道重复注册：UNION_PAY；不执行支付。</p>
     */
    @Test
    public void testDuplicateChannel() {
        System.out.println("\ntestDuplicateChannel ---> 期望：重复注册 UNION_PAY；"
                + "IllegalArgumentException：渠道重复注册：UNION_PAY；不执行支付");
        try {
            List<PaymentStrategy> strategies = new ArrayList<PaymentStrategy>();
            strategies.add(new UnionPayStrategy());
            strategies.add(new UnionPayStrategy());
            new PaymentStrategyRegistry(strategies);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNullList：观察装配、路由或扩展契约。
     * <p>期望结果：传入 null 列表；IllegalArgumentException：strategyList 不能为空且至少包含一个策略。</p>
     */
    @Test
    public void testNullList() {
        System.out.println("\ntestNullList ---> 期望：传入 null 列表；IllegalArgumentException：strategyList 不能为空且至少包含一个策略");
        try {
            new PaymentStrategyRegistry(null);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testEmptyList：观察装配、路由或扩展契约。
     * <p>期望结果：传入空列表；IllegalArgumentException：strategyList 不能为空且至少包含一个策略。</p>
     */
    @Test
    public void testEmptyList() {
        System.out.println("\ntestEmptyList ---> 期望：传入空列表；IllegalArgumentException：strategyList 不能为空且至少包含一个策略");
        try {
            new PaymentStrategyRegistry(new ArrayList<PaymentStrategy>());
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNullElement：观察装配、路由或扩展契约。
     * <p>期望结果：列表包含 null；IllegalArgumentException：strategyList 不能包含空策略。</p>
     */
    @Test
    public void testNullElement() {
        System.out.println("\ntestNullElement ---> 期望：列表包含 null；IllegalArgumentException：strategyList 不能包含空策略");
        try {
            List<PaymentStrategy> strategies = new ArrayList<PaymentStrategy>();
            strategies.add(null);
            new PaymentStrategyRegistry(strategies);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNullStrategyChannel：观察装配、路由或扩展契约。
     * <p>期望结果：策略声明空渠道；IllegalArgumentException：PaymentStrategy.getChannel 不能返回空渠道。</p>
     */
    @Test
    public void testNullStrategyChannel() {
        System.out.println("\ntestNullStrategyChannel ---> 期望：策略声明空渠道；"
                + "IllegalArgumentException：PaymentStrategy.getChannel 不能返回空渠道");
        try {
            List<PaymentStrategy> strategies = new ArrayList<PaymentStrategy>();
            strategies.add(new BrokenStrategy(null, null));
            new PaymentStrategyRegistry(strategies);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNullLookupChannel：观察装配、路由或扩展契约。
     * <p>期望结果：直接查找 null 渠道；IllegalArgumentException：channel 不能为空。</p>
     */
    @Test
    public void testNullLookupChannel() {
        System.out.println("\ntestNullLookupChannel ---> 期望：直接查找 null 渠道；IllegalArgumentException：channel 不能为空");
        try {
            List<PaymentStrategy> strategies = new ArrayList<PaymentStrategy>();
            strategies.add(new UnionPayStrategy());
            PaymentStrategyRegistry registry = new PaymentStrategyRegistry(strategies);
            registry.getStrategy(null);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNullRegistry：观察装配、路由或扩展契约。
     * <p>期望结果：服务注入 null 注册表；IllegalArgumentException：registry 不能为空。</p>
     */
    @Test
    public void testNullRegistry() {
        System.out.println("\ntestNullRegistry ---> 期望：服务注入 null 注册表；IllegalArgumentException：registry 不能为空");
        try {
            new PaymentService(null);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testDefensiveCopy：观察装配、路由或扩展契约。
     * <p>期望结果：注册后清空原始列表；UNION_PAY 仍可查找和执行，结果 UNION_PAY / UNION-001。</p>
     */
    @Test
    public void testDefensiveCopy() {
        System.out.println("\ntestDefensiveCopy ---> 期望：注册后清空原始列表；UNION_PAY 仍可查找和执行，结果 UNION_PAY / UNION-001");
        List<PaymentStrategy> strategies = new ArrayList<PaymentStrategy>();
        strategies.add(new UnionPayStrategy());
        PaymentStrategyRegistry registry = new PaymentStrategyRegistry(strategies);
        strategies.clear();
        System.out.println("原始列表大小：" + strategies.size());
        PaymentService service = new PaymentService(registry);
        PaymentResult result = service.pay(new PaymentCommand("PAY-001",
                new BigDecimal("12.34"), PaymentChannel.UNION_PAY));
        System.out.println("实际支付结果：" + result);
    }

    /**
     * testNullStrategyResult：观察装配、路由或扩展契约。
     * <p>期望结果：策略返回 null；PaymentException：策略返回空结果或结果渠道与请求不一致；不输出支付成功。</p>
     */
    @Test
    public void testNullStrategyResult() {
        System.out.println("\ntestNullStrategyResult ---> 期望：策略返回 null；PaymentException：策略返回空结果或结果渠道与请求不一致；不输出支付成功");
        try {
            List<PaymentStrategy> strategies = new ArrayList<PaymentStrategy>();
            strategies.add(new BrokenStrategy(PaymentChannel.UNION_PAY, null));
            PaymentService service = new PaymentService(new PaymentStrategyRegistry(strategies));
            service.pay(new PaymentCommand("PAY-001", new BigDecimal("12.34"), PaymentChannel.UNION_PAY));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testWrongResultChannel：观察装配、路由或扩展契约。
     * <p>期望结果：UNION_PAY 策略返回 ALIPAY 结果；PaymentException：策略返回空结果或结果渠道与请求不一致。</p>
     */
    @Test
    public void testWrongResultChannel() {
        System.out.println("\ntestWrongResultChannel ---> 期望：UNION_PAY 策略返回 ALIPAY 结果；"
                + "PaymentException：策略返回空结果或结果渠道与请求不一致");
        try {
            List<PaymentStrategy> strategies = new ArrayList<PaymentStrategy>();
            strategies.add(new BrokenStrategy(PaymentChannel.UNION_PAY,
                    new PaymentResult(PaymentChannel.ALIPAY, "WRONG-001")));
            PaymentService service = new PaymentService(new PaymentStrategyRegistry(strategies));
            service.pay(new PaymentCommand("PAY-001", new BigDecimal("12.34"), PaymentChannel.UNION_PAY));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNullAlipayClient：观察装配、路由或扩展契约。
     * <p>期望结果：支付宝策略注入空网关；IllegalArgumentException：alipayClient 不能为空。</p>
     */
    @Test
    public void testNullAlipayClient() {
        System.out.println("\ntestNullAlipayClient ---> 期望：支付宝策略注入空网关；IllegalArgumentException：alipayClient 不能为空");
        try {
            new AlipayStrategy(null);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNullWechatClient：观察装配、路由或扩展契约。
     * <p>期望结果：微信策略注入空网关；IllegalArgumentException：wechatPayClient 不能为空。</p>
     */
    @Test
    public void testNullWechatClient() {
        System.out.println("\ntestNullWechatClient ---> 期望：微信策略注入空网关；IllegalArgumentException：wechatPayClient 不能为空");
        try {
            new WechatPayStrategy(null);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 新增银联教学策略，只用于证明扩展不修改 PaymentService 和注册表。
     * 不模拟真实银联协议，也不发起真实资金操作。
     *
     * @author advance
     * @since 2026-10-27 18:35:37
     */
    private static final class UnionPayStrategy implements PaymentStrategy {
        /**
         * 声明负责银联渠道。
         * @return UNION_PAY
         */
        @Override
        public PaymentChannel getChannel() {
            return PaymentChannel.UNION_PAY;
        }

        /**
         * 打印并返回固定的教学成功结果。
         * @param command 银联支付请求
         * @return UNION_PAY / UNION-001
         * @throws IllegalArgumentException 输入为空、非法或渠道不匹配
         */
        @Override
        public PaymentResult pay(PaymentCommand command) {
            if (command == null) {
                throw new IllegalArgumentException("command 不能为空");
            }
            command.validate();
            if (command.getChannel() != PaymentChannel.UNION_PAY) {
                throw new IllegalArgumentException("UnionPayStrategy.pay 仅支持 UNION_PAY");
            }
            System.out.println("[strategy/v3] UnionPayStrategy.pay ---> 执行新增的银联教学算法");
            return new PaymentResult(PaymentChannel.UNION_PAY, "UNION-001");
        }
    }

    /**
     * 故意违反接口契约的具名测试替身，验证装配或服务边界能否拒绝错误。
     *
     * @author advance
     * @since 2026-10-27 18:35:37
     */
    private static final class BrokenStrategy implements PaymentStrategy {
        /** 故意允许空渠道以测试注册入口。 */
        private final PaymentChannel channel;
        /** 故意允许空结果或错误渠道以测试执行出口。 */
        private final PaymentResult result;

        /**
         * 配置异常策略的固定行为。
         * @param channel 声明渠道，可为空
         * @param result 返回结果，可为空
         */
        private BrokenStrategy(PaymentChannel channel, PaymentResult result) {
            this.channel = channel;
            this.result = result;
        }

        /**
         * 返回可能违反契约的渠道。
         * @return 配置的渠道
         */
        @Override
        public PaymentChannel getChannel() {
            return channel;
        }

        /**
         * 返回可能违反契约的结果，不调用任何外部系统。
         * @param command 当前请求，本替身不处理业务
         * @return 配置的结果
         */
        @Override
        public PaymentResult pay(PaymentCommand command) {
            System.out.println("[strategy/v3] BrokenStrategy.pay ---> 故意返回异常结果，观察服务保护");
            return result;
        }
    }
}
