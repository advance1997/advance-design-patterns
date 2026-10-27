package com.advance.patterns.behavioral.strategy.v2.payment;

import com.advance.patterns.behavioral.strategy.v2.domain.PaymentChannel;
import com.advance.patterns.behavioral.strategy.v2.domain.PaymentCommand;
import com.advance.patterns.behavioral.strategy.v2.domain.PaymentException;
import com.advance.patterns.behavioral.strategy.v2.domain.PaymentResult;
import com.advance.patterns.behavioral.strategy.v2.gateway.WechatPayClient;

/**
 * 封装微信支付算法：精确转换分金额并解析 success/transactionId。
 *
 * @author advance
 * @since 2026-10-27 18:35:37
 */
public final class WechatPayment {
    /** 渠道网关，不保存单次请求状态；并发能力取决于网关实现。 */
    private final WechatPayClient wechatPayClient;

    /**
     * 注入渠道网关。
     * @param wechatPayClient 模拟或真实边界的网关实现
     * @throws IllegalArgumentException 网关为空
     */
    public WechatPayment(WechatPayClient wechatPayClient) {
        if (wechatPayClient == null) {
            throw new IllegalArgumentException("wechatPayClient 不能为空");
        }
        this.wechatPayClient = wechatPayClient;
    }

    /**
     * 执行渠道算法，直接调用时也校验输入和渠道归属。
     * @param command 支付请求
     * @return 统一的成功结果
     * @throws IllegalArgumentException 输入非法、渠道不匹配或金额无法转换
     * @throws PaymentException 渠道拒绝或响应不完整
     * @throws IllegalStateException 网关超时，结果未知
     */
    public PaymentResult pay(PaymentCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command 不能为空");
        }
        command.validate();
        if (command.getChannel() != PaymentChannel.WECHAT_PAY) {
            throw new IllegalArgumentException("WechatPayment.pay 仅支持 WECHAT_PAY");
        }
        long amountInCents;
        try {
            amountInCents = command.getAmount().movePointRight(2).longValueExact();
        } catch (ArithmeticException exception) {
            throw new IllegalArgumentException("amount 转换为分后超出 long 范围或无法精确表示", exception);
        }
        System.out.println("[strategy/v2] WechatPayment.pay ---> 元转分，amountInCents=" + amountInCents);
        WechatPayClient.Response response =
                wechatPayClient.createTransaction(command.getPaymentNo(), amountInCents);
        if (response == null || !response.isSuccess()) {
            throw new PaymentException("微信响应为空或 success 为 false，不能确认支付成功");
        }
        System.out.println("[strategy/v2] WechatPayment.pay ---> 读取 transactionId，转换统一支付结果");
        return new PaymentResult(PaymentChannel.WECHAT_PAY, response.getTransactionId());
    }
}
