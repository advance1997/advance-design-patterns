package com.advance.patterns.behavioral.strategy.v3.strategy;

import com.advance.patterns.behavioral.strategy.v3.domain.PaymentChannel;
import com.advance.patterns.behavioral.strategy.v3.domain.PaymentCommand;
import com.advance.patterns.behavioral.strategy.v3.domain.PaymentException;
import com.advance.patterns.behavioral.strategy.v3.domain.PaymentResult;
import com.advance.patterns.behavioral.strategy.v3.gateway.AlipayClient;

/**
 * 封装支付宝支付算法：按元调用并解析 code/tradeNo。
 *
 * @author advance
 * @since 2026-10-27 18:35:37
 */
public final class AlipayStrategy implements PaymentStrategy {
    /** 渠道网关，不保存单次请求状态；并发能力取决于网关实现。 */
    private final AlipayClient alipayClient;

    /**
     * 注入渠道网关。
     * @param alipayClient 模拟或真实边界的网关实现
     * @throws IllegalArgumentException 网关为空
     */
    public AlipayStrategy(AlipayClient alipayClient) {
        if (alipayClient == null) {
            throw new IllegalArgumentException("alipayClient 不能为空");
        }
        this.alipayClient = alipayClient;
    }

    /**
     * 声明本策略负责的固定渠道，不依赖某次请求。
     * @return ALIPAY 渠道
     */
    @Override
    public PaymentChannel getChannel() {
        return PaymentChannel.ALIPAY;
    }

    /**
     * 执行渠道算法，直接调用时也校验输入和渠道归属。
     * @param command 支付请求
     * @return 统一的成功结果
     * @throws IllegalArgumentException 输入非法、渠道不匹配或金额无法转换
     * @throws PaymentException 渠道拒绝或响应不完整
     * @throws IllegalStateException 网关超时，结果未知
     */
    @Override
    public PaymentResult pay(PaymentCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command 不能为空");
        }
        command.validate();
        if (command.getChannel() != PaymentChannel.ALIPAY) {
            throw new IllegalArgumentException("AlipayStrategy.pay 仅支持 ALIPAY");
        }
        System.out.println("[strategy/v3] AlipayStrategy.pay ---> 调用 AlipayClient.pay，amount 单位为元");
        AlipayClient.Response response = alipayClient.pay(command.getPaymentNo(), command.getAmount());
        if (response == null || !"10000".equals(response.getCode())) {
            throw new PaymentException("支付宝响应为空或 code 非 10000，不能确认支付成功");
        }
        System.out.println("[strategy/v3] AlipayStrategy.pay ---> 读取 tradeNo，转换统一支付结果");
        return new PaymentResult(PaymentChannel.ALIPAY, response.getTradeNo());
    }
}
