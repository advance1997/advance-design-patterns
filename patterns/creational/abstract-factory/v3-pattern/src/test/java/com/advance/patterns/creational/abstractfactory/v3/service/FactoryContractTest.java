package com.advance.patterns.creational.abstractfactory.v3.service;

import com.advance.patterns.creational.abstractfactory.v3.domain.PaymentChannel;
import com.advance.patterns.creational.abstractfactory.v3.domain.PaymentReceipt;
import com.advance.patterns.creational.abstractfactory.v3.factory.PaymentComponentFactory;
import com.advance.patterns.creational.abstractfactory.v3.product.AlipayPaymentClient;
import com.advance.patterns.creational.abstractfactory.v3.product.AlipayRefundClient;
import com.advance.patterns.creational.abstractfactory.v3.product.PaymentClient;
import com.advance.patterns.creational.abstractfactory.v3.product.RefundClient;
import com.advance.patterns.creational.abstractfactory.v3.product.WechatPaymentClient;
import com.advance.patterns.creational.abstractfactory.v3.product.WechatRefundClient;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

/**
 * 验证抽象工厂的装配边界与异常传播；使用具名普通测试替身，无断言或函数回调。
 *
 * @author advance
 * @since 2026-10-27 19:50:53
 */
public class FactoryContractTest {
    /**
     * testNullFactory：直接调用并观察路径。
     * <p>期望结果：空工厂抛出 IllegalArgumentException：factory 不能为空；不创建产品。</p>
     */
    @Test
    public void testNullFactory() {
        System.out.println("\ntestNullFactory ---> 期望：空工厂抛出 IllegalArgumentException：factory 不能为空；不创建产品");
        try {
            new PaymentService(null);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNullFactoryChannel：直接调用并观察路径。
     * <p>期望结果：工厂声明空渠道；IllegalStateException：工厂必须声明非空产品族渠道；不创建产品。</p>
     */
    @Test
    public void testNullFactoryChannel() {
        System.out.println("\ntestNullFactoryChannel ---> 期望：工厂声明空渠道；IllegalStateException：工厂必须声明非空产品族渠道；不创建产品");
        try {
            new PaymentService(new StubFactory(null, new AlipayPaymentClient(), new AlipayRefundClient()));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNullPaymentProduct：直接调用并观察路径。
     * <p>期望结果：工厂返回空支付产品；IllegalStateException：工厂不能返回空产品；不执行支付或退款。</p>
     */
    @Test
    public void testNullPaymentProduct() {
        System.out.println("\ntestNullPaymentProduct ---> 期望：工厂返回空支付产品；IllegalStateException：工厂不能返回空产品；不执行支付或退款");
        try {
            new PaymentService(new StubFactory(PaymentChannel.ALIPAY, null, new AlipayRefundClient()));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNullRefundProduct：直接调用并观察路径。
     * <p>期望结果：工厂返回空退款产品；IllegalStateException：工厂不能返回空产品；不执行支付或退款。</p>
     */
    @Test
    public void testNullRefundProduct() {
        System.out.println("\ntestNullRefundProduct ---> 期望：工厂返回空退款产品；IllegalStateException：工厂不能返回空产品；不执行支付或退款");
        try {
            new PaymentService(new StubFactory(PaymentChannel.ALIPAY, new AlipayPaymentClient(), null));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testMixedProducts：直接调用并观察路径。
     * <p>期望结果：支付宝工厂返回支付宝支付与微信退款；IllegalStateException，在装配阶段拒绝，不执行支付。</p>
     */
    @Test
    public void testMixedProducts() {
        System.out.println("\ntestMixedProducts ---> 期望：支付宝工厂返回支付宝支付与微信退款；IllegalStateException，在装配阶段拒绝，不执行支付");
        try {
            new PaymentService(new StubFactory(PaymentChannel.ALIPAY,
                    new AlipayPaymentClient(), new WechatRefundClient()));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testWrongDeclaredFamily：直接调用并观察路径。
     * <p>期望结果：工厂声明微信，却返回两种支付宝产品；IllegalStateException，在装配阶段拒绝。</p>
     */
    @Test
    public void testWrongDeclaredFamily() {
        System.out.println("\ntestWrongDeclaredFamily ---> 期望：工厂声明微信，却返回两种支付宝产品；IllegalStateException，在装配阶段拒绝");
        try {
            new PaymentService(new StubFactory(PaymentChannel.WECHAT_PAY,
                    new AlipayPaymentClient(), new AlipayRefundClient()));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testWrongPaymentProduct：直接调用并观察路径。
     * <p>期望结果：支付宝工厂返回微信支付与支付宝退款；IllegalStateException，在装配阶段拒绝。</p>
     */
    @Test
    public void testWrongPaymentProduct() {
        System.out.println("\ntestWrongPaymentProduct ---> 期望：支付宝工厂返回微信支付与支付宝退款；IllegalStateException，在装配阶段拒绝");
        try {
            new PaymentService(new StubFactory(PaymentChannel.ALIPAY,
                    new WechatPaymentClient(), new AlipayRefundClient()));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testProductsCreatedOnce：直接调用并观察路径。
     * <p>期望结果：同一服务连续支付两笔；工厂的每种创建方法仅调用 1 次；凭证分别为 ALI-PAY-001 与 ALI-PAY-002。</p>
     */
    @Test
    public void testProductsCreatedOnce() {
        System.out.println("\ntestProductsCreatedOnce ---> 期望：同一服务连续支付两笔；工厂的每种创建方法仅调用 1 次；"
                + "凭证分别为 ALI-PAY-001 与 ALI-PAY-002");
        StubFactory factory = new StubFactory(PaymentChannel.ALIPAY,
                new AlipayPaymentClient(), new AlipayRefundClient());
        PaymentService service = new PaymentService(factory);
        System.out.println("第一笔实际凭证：" + service.pay("PAY-001", new BigDecimal("12.34")));
        System.out.println("第二笔实际凭证：" + service.pay("PAY-002", new BigDecimal("23.45")));
        System.out.println("支付产品创建次数：" + factory.getPaymentCreations());
        System.out.println("退款产品创建次数：" + factory.getRefundCreations());
    }

    /**
     * testPaymentFailurePropagation：直接调用并观察路径。
     * <p>期望结果：模拟支付超时；IllegalStateException：模拟支付超时，结果未知；只调用一次，不自动退款。</p>
     */
    @Test
    public void testPaymentFailurePropagation() {
        System.out.println("\ntestPaymentFailurePropagation ---> 期望：模拟支付超时；IllegalStateException：模拟支付超时，结果未知；"
                + "只调用一次，不自动退款");
        try {
            PaymentService service = new PaymentService(new StubFactory(PaymentChannel.ALIPAY,
                    new TimeoutPaymentClient(), new AlipayRefundClient()));
            service.pay("PAY-001", new BigDecimal("12.34"));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testRefundFailurePropagation：直接调用并观察路径。
     * <p>期望结果：模拟退款超时；IllegalStateException：模拟退款超时，结果未知；只调用一次，不重试。</p>
     */
    @Test
    public void testRefundFailurePropagation() {
        System.out.println("\ntestRefundFailurePropagation ---> 期望：模拟退款超时；IllegalStateException：模拟退款超时，结果未知；只调用一次，不重试");
        try {
            PaymentService service = new PaymentService(new StubFactory(PaymentChannel.ALIPAY,
                    new AlipayPaymentClient(), new TimeoutRefundClient()));
            PaymentReceipt receipt = service.pay("PAY-001", new BigDecimal("12.34"));
            service.refund(receipt, new BigDecimal("1.00"));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 可配置的具名工厂替身，用于展示工厂实现仍可能违反产品族契约。
     *
     * @author advance
     * @since 2026-10-27 19:50:53
     */
    private static final class StubFactory implements PaymentComponentFactory {
        /** 工厂声明的渠道，测试允许为空。 */
        private final PaymentChannel channel;
        /** 固定返回的支付产品，测试允许为空。 */
        private final PaymentClient paymentClient;
        /** 固定返回的退款产品，测试允许为空。 */
        private final RefundClient refundClient;
        /** 支付产品创建方法调用次数，仅用于单线程测试。 */
        private int paymentCreations;
        /** 退款产品创建方法调用次数，仅用于单线程测试。 */
        private int refundCreations;

        /**
         * 保存本次测试的工厂返回值，不代替被测服务做校验。
         * @param channel 声明的渠道
         * @param paymentClient 支付产品
         * @param refundClient 退款产品
         */
        private StubFactory(PaymentChannel channel, PaymentClient paymentClient, RefundClient refundClient) {
            this.channel = channel;
            this.paymentClient = paymentClient;
            this.refundClient = refundClient;
        }

        /**
         * 返回测试配置的产品族。
         * @return 渠道，可能故意为空或与产品不符
         */
        @Override
        public PaymentChannel getChannel() {
            return channel;
        }

        /**
         * 记录调用次数并返回预先准备的支付产品。
         * @return 支付产品，可能为空
         */
        @Override
        public PaymentClient createPaymentClient() {
            paymentCreations++;
            System.out.println("[abstract-factory/v3] StubFactory.createPaymentClient ---> 第 "
                    + paymentCreations + " 次创建请求");
            return paymentClient;
        }

        /**
         * 记录调用次数并返回预先准备的退款产品。
         * @return 退款产品，可能为空
         */
        @Override
        public RefundClient createRefundClient() {
            refundCreations++;
            System.out.println("[abstract-factory/v3] StubFactory.createRefundClient ---> 第 "
                    + refundCreations + " 次创建请求");
            return refundClient;
        }

        /**
         * 获取支付创建次数。
         * @return 本实例的调用次数
         */
        private int getPaymentCreations() {
            return paymentCreations;
        }

        /**
         * 获取退款创建次数。
         * @return 本实例的调用次数
         */
        private int getRefundCreations() {
            return refundCreations;
        }
    }

    /**
     * 模拟支付超时的具名客户端，不执行真实支付。
     *
     * @author advance
     * @since 2026-10-27 19:50:53
     */
    private static final class TimeoutPaymentClient implements PaymentClient {
        /**
         * 声明支付宝产品族。
         * @return ALIPAY
         */
        @Override
        public PaymentChannel getChannel() {
            return PaymentChannel.ALIPAY;
        }

        /**
         * 打印一次调用后抛出超时，观察服务是否原样传播。
         * @param paymentNo 支付单号
         * @param amount 元金额
         * @return 本替身永不返回
         * @throws IllegalStateException 固定模拟超时
         */
        @Override
        public PaymentReceipt pay(String paymentNo, BigDecimal amount) {
            System.out.println("[abstract-factory/v3] TimeoutPaymentClient.pay ---> 模拟支付超时");
            throw new IllegalStateException("模拟支付超时，结果未知");
        }
    }

    /**
     * 模拟退款超时的具名客户端，不执行真实退款。
     *
     * @author advance
     * @since 2026-10-27 19:50:53
     */
    private static final class TimeoutRefundClient implements RefundClient {
        /**
         * 声明支付宝产品族。
         * @return ALIPAY
         */
        @Override
        public PaymentChannel getChannel() {
            return PaymentChannel.ALIPAY;
        }

        /**
         * 打印一次调用后抛出超时，不自动重试。
         * @param receipt 原支付凭证
         * @param amount 退款元金额
         * @return 本替身永不返回
         * @throws IllegalStateException 固定模拟超时
         */
        @Override
        public String refund(PaymentReceipt receipt, BigDecimal amount) {
            System.out.println("[abstract-factory/v3] TimeoutRefundClient.refund ---> 模拟退款超时");
            throw new IllegalStateException("模拟退款超时，结果未知");
        }
    }
}
