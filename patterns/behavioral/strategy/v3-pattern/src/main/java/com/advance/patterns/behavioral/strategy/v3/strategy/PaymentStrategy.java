package com.advance.patterns.behavioral.strategy.v3.strategy;

import com.advance.patterns.behavioral.strategy.v3.domain.PaymentChannel;
import com.advance.patterns.behavioral.strategy.v3.domain.PaymentCommand;
import com.advance.patterns.behavioral.strategy.v3.domain.PaymentException;
import com.advance.patterns.behavioral.strategy.v3.domain.PaymentResult;

/**
 * 渠道算法的统一契约；只负责执行，不负责查找其他策略或隐式切换渠道。
 *
 * @author advance
 * @since 2026-10-27 18:35:37
 */
public interface PaymentStrategy {
    /**
     * 返回稳定的路由标识；同一注册表中只允许一个策略负责一个渠道。
     * @return 非空渠道，实例生命周期内保持不变
     */
    PaymentChannel getChannel();

    /**
     * 执行一次支付，结果必须属于本策略渠道且包含有效交易号。
     * @param command 已由服务校验的请求；实现也应保护直接调用入口
     * @return 非空统一结果
     * @throws IllegalArgumentException 输入非法或不属于本策略
     * @throws PaymentException 渠道拒绝或响应不完整
     * @throws IllegalStateException 调用超时，结果可能未知
     */
    PaymentResult pay(PaymentCommand command);
}
