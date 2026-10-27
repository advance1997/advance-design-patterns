package com.advance.patterns.creational.abstractfactory.v2.service;

import com.advance.patterns.creational.abstractfactory.v2.domain.PaymentChannel;
import com.advance.patterns.creational.abstractfactory.v2.domain.PaymentReceipt;
import com.advance.patterns.creational.abstractfactory.v2.factory.PaymentClientFactory;
import com.advance.patterns.creational.abstractfactory.v2.factory.RefundClientFactory;
import com.advance.patterns.creational.abstractfactory.v2.product.PaymentClient;
import com.advance.patterns.creational.abstractfactory.v2.product.RefundClient;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

/**
 * 支付与退款产品族的独立打印式实践；JUnit 只负责运行，结果需人工对照。
 *
 * @author advance
 * @since 2026-10-27 19:50:53
 */
public class PaymentFamilyTest {
    /**
     * testAlipayFamily：直接调用并观察路径。
     * <p>期望结果：支付凭证 ALIPAY / ALI-PAY-001 / 12.34 元；退款 ALI-REFUND-ALI-PAY-001；两个产品均为支付宝。</p>
     */
    @Test
    public void testAlipayFamily() {
        System.out.println("\ntestAlipayFamily ---> 期望：支付凭证 ALIPAY / ALI-PAY-001 / 12.34 元；"
                + "退款 ALI-REFUND-ALI-PAY-001；两个产品均为支付宝");
        PaymentClient paymentClient = PaymentClientFactory.create(PaymentChannel.ALIPAY);
        RefundClient refundClient = RefundClientFactory.create(PaymentChannel.ALIPAY);
        PaymentService service = new PaymentService(paymentClient, refundClient);
        PaymentReceipt receipt = service.pay("PAY-001", new BigDecimal("12.34"));
        System.out.println("实际支付凭证：" + receipt);
        String refundNo = service.refund(receipt, new BigDecimal("2.34"));
        System.out.println("实际退款流水：" + refundNo);
    }

    /**
     * testWechatFamily：直接调用并观察路径。
     * <p>期望结果：支付 1234 分，退款 234 分；凭证 WECHAT_PAY / WX-PAY-001；退款 WX-REFUND-WX-PAY-001。</p>
     */
    @Test
    public void testWechatFamily() {
        System.out.println("\ntestWechatFamily ---> 期望：支付 1234 分，退款 234 分；凭证 WECHAT_PAY / WX-PAY-001；"
                + "退款 WX-REFUND-WX-PAY-001");
        PaymentClient paymentClient = PaymentClientFactory.create(PaymentChannel.WECHAT_PAY);
        RefundClient refundClient = RefundClientFactory.create(PaymentChannel.WECHAT_PAY);
        PaymentService service = new PaymentService(paymentClient, refundClient);
        PaymentReceipt receipt = service.pay("PAY-001", new BigDecimal("12.34"));
        System.out.println("实际支付凭证：" + receipt);
        String refundNo = service.refund(receipt, new BigDecimal("2.34"));
        System.out.println("实际退款流水：" + refundNo);
    }

    /**
     * testOneCentAndFullRefund：直接调用并观察路径。
     * <p>期望结果：支付与全额退款均为 1 分；退款 WX-REFUND-WX-PAY-001。</p>
     */
    @Test
    public void testOneCentAndFullRefund() {
        System.out.println("\ntestOneCentAndFullRefund ---> 期望：支付与全额退款均为 1 分；退款 WX-REFUND-WX-PAY-001");
        PaymentClient paymentClient = PaymentClientFactory.create(PaymentChannel.WECHAT_PAY);
        RefundClient refundClient = RefundClientFactory.create(PaymentChannel.WECHAT_PAY);
        PaymentService service = new PaymentService(paymentClient, refundClient);
        PaymentReceipt receipt = service.pay("PAY-001", new BigDecimal("0.01"));
        String refundNo = service.refund(receipt, new BigDecimal("0.01"));
        System.out.println("实际退款流水：" + refundNo);
    }

    /**
     * testTrailingZeros：直接调用并观察路径。
     * <p>期望结果：12.340 元精确转为 1234 分；尾随零不导致校验失败。</p>
     */
    @Test
    public void testTrailingZeros() {
        System.out.println("\ntestTrailingZeros ---> 期望：12.340 元精确转为 1234 分；尾随零不导致校验失败");
        PaymentClient paymentClient = PaymentClientFactory.create(PaymentChannel.WECHAT_PAY);
        RefundClient refundClient = RefundClientFactory.create(PaymentChannel.WECHAT_PAY);
        PaymentService service = new PaymentService(paymentClient, refundClient);
        System.out.println("实际支付凭证：" + service.pay("PAY-001", new BigDecimal("12.340")));
    }

    /**
     * testBlankPaymentNo：直接调用并观察路径。
     * <p>期望结果：空白单号抛出 IllegalArgumentException：paymentNo 不能为空；不出现模拟渠道调用。</p>
     */
    @Test
    public void testBlankPaymentNo() {
        System.out.println("\ntestBlankPaymentNo ---> 期望：空白单号抛出 IllegalArgumentException：paymentNo 不能为空；不出现模拟渠道调用");
        try {
            PaymentClient paymentClient = PaymentClientFactory.create(PaymentChannel.ALIPAY);
            RefundClient refundClient = RefundClientFactory.create(PaymentChannel.ALIPAY);
            PaymentService service = new PaymentService(paymentClient, refundClient);
            service.pay("  ", new BigDecimal("12.34"));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNullPaymentAmount：直接调用并观察路径。
     * <p>期望结果：空金额抛出 IllegalArgumentException；不出现模拟渠道调用。</p>
     */
    @Test
    public void testNullPaymentAmount() {
        System.out.println("\ntestNullPaymentAmount ---> 期望：空金额抛出 IllegalArgumentException；不出现模拟渠道调用");
        try {
            PaymentClient paymentClient = PaymentClientFactory.create(PaymentChannel.ALIPAY);
            RefundClient refundClient = RefundClientFactory.create(PaymentChannel.ALIPAY);
            PaymentService service = new PaymentService(paymentClient, refundClient);
            service.pay("PAY-001", null);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testZeroPaymentAmount：直接调用并观察路径。
     * <p>期望结果：0 元抛出 IllegalArgumentException；不出现模拟渠道调用。</p>
     */
    @Test
    public void testZeroPaymentAmount() {
        System.out.println("\ntestZeroPaymentAmount ---> 期望：0 元抛出 IllegalArgumentException；不出现模拟渠道调用");
        try {
            PaymentClient paymentClient = PaymentClientFactory.create(PaymentChannel.ALIPAY);
            RefundClient refundClient = RefundClientFactory.create(PaymentChannel.ALIPAY);
            PaymentService service = new PaymentService(paymentClient, refundClient);
            service.pay("PAY-001", new BigDecimal("0"));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNegativePaymentAmount：直接调用并观察路径。
     * <p>期望结果：-1 元抛出 IllegalArgumentException；不出现模拟渠道调用。</p>
     */
    @Test
    public void testNegativePaymentAmount() {
        System.out.println("\ntestNegativePaymentAmount ---> 期望：-1 元抛出 IllegalArgumentException；不出现模拟渠道调用");
        try {
            PaymentClient paymentClient = PaymentClientFactory.create(PaymentChannel.ALIPAY);
            RefundClient refundClient = RefundClientFactory.create(PaymentChannel.ALIPAY);
            PaymentService service = new PaymentService(paymentClient, refundClient);
            service.pay("PAY-001", new BigDecimal("-1"));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testFractionOfCent：直接调用并观察路径。
     * <p>期望结果：0.001 元抛出 IllegalArgumentException；不舍入，不出现模拟渠道调用。</p>
     */
    @Test
    public void testFractionOfCent() {
        System.out.println("\ntestFractionOfCent ---> 期望：0.001 元抛出 IllegalArgumentException；不舍入，不出现模拟渠道调用");
        try {
            PaymentClient paymentClient = PaymentClientFactory.create(PaymentChannel.WECHAT_PAY);
            RefundClient refundClient = RefundClientFactory.create(PaymentChannel.WECHAT_PAY);
            PaymentService service = new PaymentService(paymentClient, refundClient);
            service.pay("PAY-001", new BigDecimal("0.001"));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testPaymentOverflow：直接调用并观察路径。
     * <p>期望结果：微信分金额超出 long 范围；IllegalArgumentException：amount 转换为分后超出 long 范围。</p>
     */
    @Test
    public void testPaymentOverflow() {
        System.out.println("\ntestPaymentOverflow ---> 期望：微信分金额超出 long 范围；"
                + "IllegalArgumentException：amount 转换为分后超出 long 范围");
        try {
            PaymentClient paymentClient = PaymentClientFactory.create(PaymentChannel.WECHAT_PAY);
            RefundClient refundClient = RefundClientFactory.create(PaymentChannel.WECHAT_PAY);
            PaymentService service = new PaymentService(paymentClient, refundClient);
            service.pay("PAY-001", new BigDecimal("92233720368547758.08"));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNullReceipt：直接调用并观察路径。
     * <p>期望结果：退款凭证为空；IllegalArgumentException：receipt 不能为空；不出现模拟渠道调用。</p>
     */
    @Test
    public void testNullReceipt() {
        System.out.println("\ntestNullReceipt ---> 期望：退款凭证为空；IllegalArgumentException：receipt 不能为空；不出现模拟渠道调用");
        try {
            PaymentClient paymentClient = PaymentClientFactory.create(PaymentChannel.ALIPAY);
            RefundClient refundClient = RefundClientFactory.create(PaymentChannel.ALIPAY);
            PaymentService service = new PaymentService(paymentClient, refundClient);
            service.refund(null, new BigDecimal("1.00"));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testWrongReceiptChannel：直接调用并观察路径。
     * <p>期望结果：支付宝退款客户端收到微信凭证；IllegalArgumentException：退款客户端渠道与原支付渠道不一致。</p>
     */
    @Test
    public void testWrongReceiptChannel() {
        System.out.println("\ntestWrongReceiptChannel ---> 期望：支付宝退款客户端收到微信凭证；"
                + "IllegalArgumentException：退款客户端渠道与原支付渠道不一致");
        try {
            PaymentClient paymentClient = PaymentClientFactory.create(PaymentChannel.ALIPAY);
            RefundClient refundClient = RefundClientFactory.create(PaymentChannel.ALIPAY);
            PaymentService service = new PaymentService(paymentClient, refundClient);
            PaymentReceipt receipt = new PaymentReceipt(PaymentChannel.WECHAT_PAY, "TRADE-001",
                    new BigDecimal("12.34"));
            service.refund(receipt, new BigDecimal("1.00"));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testExcessRefund：直接调用并观察路径。
     * <p>期望结果：原支付 12.34 元，退款 12.35 元；IllegalArgumentException：单次退款金额不能超过原支付金额。</p>
     */
    @Test
    public void testExcessRefund() {
        System.out.println("\ntestExcessRefund ---> 期望：原支付 12.34 元，退款 12.35 元；"
                + "IllegalArgumentException：单次退款金额不能超过原支付金额");
        try {
            PaymentClient paymentClient = PaymentClientFactory.create(PaymentChannel.ALIPAY);
            RefundClient refundClient = RefundClientFactory.create(PaymentChannel.ALIPAY);
            PaymentService service = new PaymentService(paymentClient, refundClient);
            PaymentReceipt receipt = new PaymentReceipt(PaymentChannel.ALIPAY, "TRADE-001", new BigDecimal("12.34"));
            service.refund(receipt, new BigDecimal("12.35"));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNullRefundAmount：直接调用并观察路径。
     * <p>期望结果：空退款金额抛出 IllegalArgumentException；不出现模拟渠道调用。</p>
     */
    @Test
    public void testNullRefundAmount() {
        System.out.println("\ntestNullRefundAmount ---> 期望：空退款金额抛出 IllegalArgumentException；不出现模拟渠道调用");
        try {
            PaymentClient paymentClient = PaymentClientFactory.create(PaymentChannel.ALIPAY);
            RefundClient refundClient = RefundClientFactory.create(PaymentChannel.ALIPAY);
            PaymentService service = new PaymentService(paymentClient, refundClient);
            PaymentReceipt receipt = new PaymentReceipt(PaymentChannel.ALIPAY, "TRADE-001", new BigDecimal("12.34"));
            service.refund(receipt, null);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testZeroRefundAmount：直接调用并观察路径。
     * <p>期望结果：退款 0 元抛出 IllegalArgumentException；不出现模拟渠道调用。</p>
     */
    @Test
    public void testZeroRefundAmount() {
        System.out.println("\ntestZeroRefundAmount ---> 期望：退款 0 元抛出 IllegalArgumentException；不出现模拟渠道调用");
        try {
            PaymentClient paymentClient = PaymentClientFactory.create(PaymentChannel.ALIPAY);
            RefundClient refundClient = RefundClientFactory.create(PaymentChannel.ALIPAY);
            PaymentService service = new PaymentService(paymentClient, refundClient);
            PaymentReceipt receipt = new PaymentReceipt(PaymentChannel.ALIPAY, "TRADE-001", new BigDecimal("12.34"));
            service.refund(receipt, BigDecimal.ZERO);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNegativeRefundAmount：直接调用并观察路径。
     * <p>期望结果：退款 -1 元抛出 IllegalArgumentException；不出现模拟渠道调用。</p>
     */
    @Test
    public void testNegativeRefundAmount() {
        System.out.println("\ntestNegativeRefundAmount ---> 期望：退款 -1 元抛出 IllegalArgumentException；不出现模拟渠道调用");
        try {
            PaymentClient paymentClient = PaymentClientFactory.create(PaymentChannel.ALIPAY);
            RefundClient refundClient = RefundClientFactory.create(PaymentChannel.ALIPAY);
            PaymentService service = new PaymentService(paymentClient, refundClient);
            PaymentReceipt receipt = new PaymentReceipt(PaymentChannel.ALIPAY, "TRADE-001", new BigDecimal("12.34"));
            service.refund(receipt, new BigDecimal("-1"));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testRefundFractionOfCent：直接调用并观察路径。
     * <p>期望结果：退款 0.001 元抛出 IllegalArgumentException；不舍入，不出现模拟渠道调用。</p>
     */
    @Test
    public void testRefundFractionOfCent() {
        System.out.println("\ntestRefundFractionOfCent ---> 期望：退款 0.001 元抛出 IllegalArgumentException；不舍入，不出现模拟渠道调用");
        try {
            PaymentClient paymentClient = PaymentClientFactory.create(PaymentChannel.WECHAT_PAY);
            RefundClient refundClient = RefundClientFactory.create(PaymentChannel.WECHAT_PAY);
            PaymentService service = new PaymentService(paymentClient, refundClient);
            PaymentReceipt receipt = new PaymentReceipt(PaymentChannel.WECHAT_PAY, "TRADE-001",
                    new BigDecimal("12.34"));
            service.refund(receipt, new BigDecimal("0.001"));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testRefundOverflow：直接调用并观察路径。
     * <p>期望结果：外部传入超大微信凭证，退款分金额超出 long 范围；IllegalArgumentException。</p>
     */
    @Test
    public void testRefundOverflow() {
        System.out.println("\ntestRefundOverflow ---> 期望：外部传入超大微信凭证，退款分金额超出 long 范围；IllegalArgumentException");
        try {
            PaymentClient paymentClient = PaymentClientFactory.create(PaymentChannel.WECHAT_PAY);
            RefundClient refundClient = RefundClientFactory.create(PaymentChannel.WECHAT_PAY);
            PaymentService service = new PaymentService(paymentClient, refundClient);
            PaymentReceipt receipt = new PaymentReceipt(PaymentChannel.WECHAT_PAY, "TRADE-001",
                    new BigDecimal("92233720368547758.08"));
            service.refund(receipt, new BigDecimal("92233720368547758.08"));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testBlankReceiptTradeNo：直接调用并观察路径。
     * <p>期望结果：创建凭证时交易号为空白；IllegalArgumentException：tradeNo 不能为空。</p>
     */
    @Test
    public void testBlankReceiptTradeNo() {
        System.out.println("\ntestBlankReceiptTradeNo ---> 期望：创建凭证时交易号为空白；IllegalArgumentException：tradeNo 不能为空");
        try {
            new PaymentReceipt(PaymentChannel.ALIPAY, "  ", new BigDecimal("12.34"));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNullReceiptChannel：直接调用并观察路径。
     * <p>期望结果：创建凭证时渠道为空；IllegalArgumentException：channel 不能为空。</p>
     */
    @Test
    public void testNullReceiptChannel() {
        System.out.println("\ntestNullReceiptChannel ---> 期望：创建凭证时渠道为空；IllegalArgumentException：channel 不能为空");
        try {
            new PaymentReceipt(null, "TRADE-001", new BigDecimal("12.34"));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testUnsupportedChannel：直接调用并观察路径。
     * <p>期望结果：选择 UNION_PAY 时抛出 IllegalArgumentException：不支持的产品渠道：UNION_PAY。</p>
     */
    @Test
    public void testUnsupportedChannel() {
        System.out.println("\ntestUnsupportedChannel ---> 期望：选择 UNION_PAY 时抛出 IllegalArgumentException：不支持的产品渠道"
                + "：UNION_PAY");
        try {
            PaymentClientFactory.create(PaymentChannel.UNION_PAY);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNullChannel：直接调用并观察路径。
     * <p>期望结果：选择 null 渠道抛出 IllegalArgumentException：不支持的产品渠道：null。</p>
     */
    @Test
    public void testNullChannel() {
        System.out.println("\ntestNullChannel ---> 期望：选择 null 渠道抛出 IllegalArgumentException：不支持的产品渠道：null");
        try {
            PaymentClientFactory.create(null);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testMismatchedProducts：直接调用并观察路径。
     * <p>期望结果：先由支付宝模拟支付成功，再因微信退款客户端错配而失败；IllegalArgumentException：退款客户端渠道与原支付渠道不一致。</p>
     */
    @Test
    public void testMismatchedProducts() {
        System.out.println("\ntestMismatchedProducts ---> 期望：先由支付宝模拟支付成功，再因微信退款客户端错配而失败；"
                + "IllegalArgumentException：退款客户端渠道与原支付渠道不一致");
        try {
            PaymentClient paymentClient = PaymentClientFactory.create(PaymentChannel.ALIPAY);
            RefundClient refundClient = RefundClientFactory.create(PaymentChannel.WECHAT_PAY);
            PaymentService service = new PaymentService(paymentClient, refundClient);
            PaymentReceipt receipt = service.pay("PAY-001", new BigDecimal("12.34"));
            System.out.println("实际支付凭证：" + receipt);
            service.refund(receipt, new BigDecimal("1.00"));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNullPaymentProduct：直接调用并观察路径。
     * <p>期望结果：支付产品为空；IllegalArgumentException：paymentClient 和 refundClient 均不能为空。</p>
     */
    @Test
    public void testNullPaymentProduct() {
        System.out.println("\ntestNullPaymentProduct ---> 期望：支付产品为空；"
                + "IllegalArgumentException：paymentClient 和 refundClient 均不能为空");
        try {
            new PaymentService(null, RefundClientFactory.create(PaymentChannel.ALIPAY));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNullRefundProduct：直接调用并观察路径。
     * <p>期望结果：退款产品为空；IllegalArgumentException：paymentClient 和 refundClient 均不能为空。</p>
     */
    @Test
    public void testNullRefundProduct() {
        System.out.println("\ntestNullRefundProduct ---> 期望：退款产品为空；"
                + "IllegalArgumentException：paymentClient 和 refundClient 均不能为空");
        try {
            new PaymentService(PaymentClientFactory.create(PaymentChannel.ALIPAY), null);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testUnsupportedRefundChannel：直接调用并观察路径。
     * <p>期望结果：退款工厂选择 UNION_PAY；IllegalArgumentException：不支持的产品渠道：UNION_PAY。</p>
     */
    @Test
    public void testUnsupportedRefundChannel() {
        System.out.println("\ntestUnsupportedRefundChannel ---> 期望：退款工厂选择 UNION_PAY；"
                + "IllegalArgumentException：不支持的产品渠道：UNION_PAY");
        try {
            RefundClientFactory.create(PaymentChannel.UNION_PAY);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNullRefundChannel：直接调用并观察路径。
     * <p>期望结果：退款工厂选择 null；IllegalArgumentException：不支持的产品渠道：null。</p>
     */
    @Test
    public void testNullRefundChannel() {
        System.out.println("\ntestNullRefundChannel ---> 期望：退款工厂选择 null；IllegalArgumentException：不支持的产品渠道：null");
        try {
            RefundClientFactory.create(null);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }
}
