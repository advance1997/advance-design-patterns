package com.advance.patterns.behavioral.strategy.v3.service;

import com.advance.patterns.behavioral.strategy.v3.domain.PaymentChannel;
import com.advance.patterns.behavioral.strategy.v3.domain.PaymentCommand;
import com.advance.patterns.behavioral.strategy.v3.domain.PaymentException;
import com.advance.patterns.behavioral.strategy.v3.domain.PaymentResult;
import com.advance.patterns.behavioral.strategy.v3.registry.PaymentStrategyRegistry;
import com.advance.patterns.behavioral.strategy.v3.strategy.PaymentStrategy;

/**
 * 支付上下文：校验、委托路由、执行策略；不依赖具体渠道类或渠道分支。
 *
 * @author advance
 * @since 2026-10-27 18:35:37
 */
public final class PaymentService {
    /** 负责选择已有策略的注册表。 */
    private final PaymentStrategyRegistry registry;

    /**
     * 注入路由注册表。
     * @param registry 已完成装配的注册表
     * @throws IllegalArgumentException 注册表为空
     */
    public PaymentService(PaymentStrategyRegistry registry) {
        if (registry == null) {
            throw new IllegalArgumentException("registry 不能为空");
        }
        this.registry = registry;
    }

    /**
     * 选择并执行算法，异常原样向上传播，不重试、不降级。
     * @param command 支付请求
     * @return 属于请求渠道的有效成功结果
     * @throws IllegalArgumentException 输入非法、渠道未注册或金额无法转换
     * @throws PaymentException 策略失败、返回空结果或返回其他渠道结果
     * @throws IllegalStateException 网关超时，不能认为外部支付已回滚
     */
    public PaymentResult pay(PaymentCommand command) {
        System.out.println("[strategy/v3] PaymentService.pay ---> 校验请求并委托注册表选择策略");
        if (command == null) {
            throw new IllegalArgumentException("command 不能为空");
        }
        command.validate();
        PaymentStrategy strategy = registry.getStrategy(command.getChannel());
        System.out.println("[strategy/v3] PaymentService.pay ---> 执行 " + strategy.getClass().getSimpleName());
        PaymentResult result = strategy.pay(command);
        if (result == null || result.getChannel() != command.getChannel()) {
            throw new PaymentException("策略返回空结果或结果渠道与请求不一致");
        }
        System.out.println("[strategy/v3] PaymentService.pay ---> 支付成功，返回统一结果");
        return result;
    }
}
