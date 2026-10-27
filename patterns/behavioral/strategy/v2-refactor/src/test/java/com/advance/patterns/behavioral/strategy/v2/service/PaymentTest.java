package com.advance.patterns.behavioral.strategy.v2.service;

import com.advance.patterns.behavioral.strategy.v2.domain.PaymentChannel;
import com.advance.patterns.behavioral.strategy.v2.domain.PaymentCommand;
import com.advance.patterns.behavioral.strategy.v2.domain.PaymentResult;
import com.advance.patterns.behavioral.strategy.v2.gateway.AlipayClient;
import com.advance.patterns.behavioral.strategy.v2.gateway.WechatPayClient;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

/**
 * 独立支付演示；JUnit 仅负责执行，请人工对照期望值与控制台路径。
 *
 * @author advance
 * @since 2026-10-27 18:35:37
 */
public class PaymentTest {
    /**
     * testAlipaySuccess：观察执行路径和实际结果。
     * <p>期望结果：输入 ALIPAY、12.34 元；仅调用 MockAlipayClient.pay 一次，金额 12.34 元；结果 ALIPAY / ALI-001。</p>
     */
    @Test
    public void testAlipaySuccess() {
        System.out.println("\ntestAlipaySuccess ---> 期望：输入 ALIPAY、12.34 元；"
                + "仅调用 MockAlipayClient.pay 一次，金额 12.34 元；结果 ALIPAY / ALI-001");
        PaymentService service = new PaymentService(new MockAlipayClient("10000", "ALI-001", false),
                new MockWechatPayClient(true, "WX-001", false));
        PaymentResult result = service.pay(new PaymentCommand("PAY-001", new BigDecimal("12.34"),
                PaymentChannel.ALIPAY));
        System.out.println("实际支付结果：" + result);
    }

    /**
     * testWechatSuccess：观察执行路径和实际结果。
     * <p>期望结果：输入 WECHAT_PAY、12.34 元；仅调用 MockWechatPayClient.createTransaction 一次，金额 1234 分；结果 WECHAT_PAY / WX-001。</p>
     */
    @Test
    public void testWechatSuccess() {
        System.out.println("\ntestWechatSuccess ---> 期望：输入 WECHAT_PAY、12.34 元；"
                + "仅调用 MockWechatPayClient.createTransaction 一次，金额 1234 分；结果 WECHAT_PAY / WX-001");
        PaymentService service = new PaymentService(new MockAlipayClient("10000", "ALI-001", false),
                new MockWechatPayClient(true, "WX-001", false));
        PaymentResult result = service.pay(new PaymentCommand("PAY-001", new BigDecimal("12.34"),
                PaymentChannel.WECHAT_PAY));
        System.out.println("实际支付结果：" + result);
    }

    /**
     * testWechatOneCent：观察执行路径和实际结果。
     * <p>期望结果：输入 WECHAT_PAY、0.01 元；客户端收到 1 分；结果 WECHAT_PAY / WX-001。</p>
     */
    @Test
    public void testWechatOneCent() {
        System.out.println("\ntestWechatOneCent ---> 期望：输入 WECHAT_PAY、0.01 元；客户端收到 1 分；结果 WECHAT_PAY / WX-001");
        PaymentService service = new PaymentService(new MockAlipayClient("10000", "ALI-001", false),
                new MockWechatPayClient(true, "WX-001", false));
        PaymentResult result = service.pay(new PaymentCommand("PAY-001", new BigDecimal("0.01"),
                PaymentChannel.WECHAT_PAY));
        System.out.println("实际支付结果：" + result);
    }

    /**
     * testTrailingZeros：观察执行路径和实际结果。
     * <p>期望结果：输入 WECHAT_PAY、12.340 元；尾随零不影响精度，客户端收到 1234 分。</p>
     */
    @Test
    public void testTrailingZeros() {
        System.out.println("\ntestTrailingZeros ---> 期望：输入 WECHAT_PAY、12.340 元；尾随零不影响精度，客户端收到 1234 分");
        PaymentService service = new PaymentService(new MockAlipayClient("10000", "ALI-001", false),
                new MockWechatPayClient(true, "WX-001", false));
        PaymentResult result = service.pay(new PaymentCommand("PAY-001", new BigDecimal("12.340"),
                PaymentChannel.WECHAT_PAY));
        System.out.println("实际支付结果：" + result);
    }

    /**
     * testNullCommand：观察执行路径和实际结果。
     * <p>期望结果：输入空请求；IllegalArgumentException：command 不能为空；不调用网关。</p>
     */
    @Test
    public void testNullCommand() {
        System.out.println("\ntestNullCommand ---> 期望：输入空请求；IllegalArgumentException：command 不能为空；不调用网关");
        try {
            PaymentService service = new PaymentService(new MockAlipayClient("10000", "ALI-001",
                    false), new MockWechatPayClient(true, "WX-001", false));
            PaymentResult result = service.pay(null);
            System.out.println("实际支付结果：" + result);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testBlankPaymentNo：观察执行路径和实际结果。
     * <p>期望结果：输入空白单号；IllegalArgumentException：paymentNo 不能为空；不调用网关。</p>
     */
    @Test
    public void testBlankPaymentNo() {
        System.out.println("\ntestBlankPaymentNo ---> 期望：输入空白单号；IllegalArgumentException：paymentNo 不能为空；不调用网关");
        try {
            PaymentService service = new PaymentService(new MockAlipayClient("10000", "ALI-001",
                    false), new MockWechatPayClient(true, "WX-001", false));
            PaymentResult result = service.pay(new PaymentCommand("  ", new BigDecimal("12.34"),
                    PaymentChannel.ALIPAY));
            System.out.println("实际支付结果：" + result);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNullChannel：观察执行路径和实际结果。
     * <p>期望结果：输入空渠道；IllegalArgumentException：channel 不能为空；不调用网关。</p>
     */
    @Test
    public void testNullChannel() {
        System.out.println("\ntestNullChannel ---> 期望：输入空渠道；IllegalArgumentException：channel 不能为空；不调用网关");
        try {
            PaymentService service = new PaymentService(new MockAlipayClient("10000", "ALI-001",
                    false), new MockWechatPayClient(true, "WX-001", false));
            PaymentResult result = service.pay(new PaymentCommand("PAY-001", new BigDecimal("12.34"), null));
            System.out.println("实际支付结果：" + result);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNullAmount：观察执行路径和实际结果。
     * <p>期望结果：输入空金额；IllegalArgumentException：amount 必须为正数元金额且最多两位有效小数；不调用网关。</p>
     */
    @Test
    public void testNullAmount() {
        System.out.println("\ntestNullAmount ---> 期望：输入空金额；IllegalArgumentException：amount 必须为正数元金额且最多两位有效小数；不调用网关");
        try {
            PaymentService service = new PaymentService(new MockAlipayClient("10000", "ALI-001",
                    false), new MockWechatPayClient(true, "WX-001", false));
            PaymentResult result = service.pay(new PaymentCommand("PAY-001", null, PaymentChannel.ALIPAY));
            System.out.println("实际支付结果：" + result);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testZeroAmount：观察执行路径和实际结果。
     * <p>期望结果：输入 0 元；IllegalArgumentException：amount 必须为正数元金额且最多两位有效小数；不调用网关。</p>
     */
    @Test
    public void testZeroAmount() {
        System.out.println("\ntestZeroAmount ---> 期望：输入 0 元；IllegalArgumentException：amount 必须为正数元金额且最多两位有效小数；不调用网关");
        try {
            PaymentService service = new PaymentService(new MockAlipayClient("10000", "ALI-001",
                    false), new MockWechatPayClient(true, "WX-001", false));
            PaymentResult result = service.pay(new PaymentCommand("PAY-001", new BigDecimal("0"),
                    PaymentChannel.ALIPAY));
            System.out.println("实际支付结果：" + result);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNegativeAmount：观察执行路径和实际结果。
     * <p>期望结果：输入 -1 元；IllegalArgumentException：amount 必须为正数元金额且最多两位有效小数；不调用网关。</p>
     */
    @Test
    public void testNegativeAmount() {
        System.out.println("\ntestNegativeAmount ---> 期望：输入 -1 元；"
                + "IllegalArgumentException：amount 必须为正数元金额且最多两位有效小数；不调用网关");
        try {
            PaymentService service = new PaymentService(new MockAlipayClient("10000", "ALI-001",
                    false), new MockWechatPayClient(true, "WX-001", false));
            PaymentResult result = service.pay(new PaymentCommand("PAY-001", new BigDecimal("-1"),
                    PaymentChannel.ALIPAY));
            System.out.println("实际支付结果：" + result);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testFractionOfCent：观察执行路径和实际结果。
     * <p>期望结果：输入 WECHAT_PAY、0.001 元；IllegalArgumentException：amount 必须为正数元金额且最多两位有效小数；不调用网关。</p>
     */
    @Test
    public void testFractionOfCent() {
        System.out.println("\ntestFractionOfCent ---> 期望：输入 WECHAT_PAY、0.001 元；"
                + "IllegalArgumentException：amount 必须为正数元金额且最多两位有效小数；不调用网关");
        try {
            PaymentService service = new PaymentService(new MockAlipayClient("10000", "ALI-001",
                    false), new MockWechatPayClient(true, "WX-001", false));
            PaymentResult result = service.pay(new PaymentCommand("PAY-001", new BigDecimal("0.001"),
                    PaymentChannel.WECHAT_PAY));
            System.out.println("实际支付结果：" + result);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testWechatOverflow：观察执行路径和实际结果。
     * <p>期望结果：输入 WECHAT_PAY、92233720368547758.08 元；IllegalArgumentException：amount 转换为分后超出 long 范围或无法精确表示；不调用网关。</p>
     */
    @Test
    public void testWechatOverflow() {
        System.out.println("\ntestWechatOverflow ---> 期望：输入 WECHAT_PAY、92233720368547758.08 元；"
                + "IllegalArgumentException：amount 转换为分后超出 long 范围或无法精确表示；不调用网关");
        try {
            PaymentService service = new PaymentService(new MockAlipayClient("10000", "ALI-001",
                    false), new MockWechatPayClient(true, "WX-001", false));
            PaymentResult result = service.pay(new PaymentCommand("PAY-001",
                    new BigDecimal("92233720368547758.08"), PaymentChannel.WECHAT_PAY));
            System.out.println("实际支付结果：" + result);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testUnsupportedChannel：观察执行路径和实际结果。
     * <p>期望结果：输入 UNION_PAY；IllegalArgumentException：不支持支付渠道：UNION_PAY；不调用网关。</p>
     */
    @Test
    public void testUnsupportedChannel() {
        System.out.println("\ntestUnsupportedChannel ---> 期望：输入 UNION_PAY；"
                + "IllegalArgumentException：不支持支付渠道：UNION_PAY；不调用网关");
        try {
            PaymentService service = new PaymentService(new MockAlipayClient("10000", "ALI-001",
                    false), new MockWechatPayClient(true, "WX-001", false));
            PaymentResult result = service.pay(new PaymentCommand("PAY-001", new BigDecimal("12.34"),
                    PaymentChannel.UNION_PAY));
            System.out.println("实际支付结果：" + result);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testAlipayRejected：观察执行路径和实际结果。
     * <p>期望结果：支付宝返回 code=40004；PaymentException：支付宝响应为空或 code 非 10000，不能确认支付成功；不调用微信。</p>
     */
    @Test
    public void testAlipayRejected() {
        System.out.println("\ntestAlipayRejected ---> 期望：支付宝返回 code=40004；"
                + "PaymentException：支付宝响应为空或 code 非 10000，不能确认支付成功；不调用微信");
        try {
            PaymentService service = new PaymentService(new MockAlipayClient("40004", null, false),
                    new MockWechatPayClient(true, "WX-001", false));
            PaymentResult result = service.pay(new PaymentCommand("PAY-001", new BigDecimal("12.34"),
                    PaymentChannel.ALIPAY));
            System.out.println("实际支付结果：" + result);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testWechatRejected：观察执行路径和实际结果。
     * <p>期望结果：微信返回 success=false；PaymentException：微信响应为空或 success 为 false，不能确认支付成功；不调用支付宝。</p>
     */
    @Test
    public void testWechatRejected() {
        System.out.println("\ntestWechatRejected ---> 期望：微信返回 success=false；"
                + "PaymentException：微信响应为空或 success 为 false，不能确认支付成功；不调用支付宝");
        try {
            PaymentService service = new PaymentService(new MockAlipayClient("10000", "ALI-001",
                    false), new MockWechatPayClient(false, null, false));
            PaymentResult result = service.pay(new PaymentCommand("PAY-001", new BigDecimal("12.34"),
                    PaymentChannel.WECHAT_PAY));
            System.out.println("实际支付结果：" + result);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testAlipayMissingTradeNo：观察执行路径和实际结果。
     * <p>期望结果：支付宝成功码但交易号为空白；PaymentException：渠道 ALIPAY 返回的交易号为空，不能确认支付成功。</p>
     */
    @Test
    public void testAlipayMissingTradeNo() {
        System.out.println("\ntestAlipayMissingTradeNo ---> 期望：支付宝成功码但交易号为空白；"
                + "PaymentException：渠道 ALIPAY 返回的交易号为空，不能确认支付成功");
        try {
            PaymentService service = new PaymentService(new MockAlipayClient("10000", "  ", false),
                    new MockWechatPayClient(true, "WX-001", false));
            PaymentResult result = service.pay(new PaymentCommand("PAY-001", new BigDecimal("12.34"),
                    PaymentChannel.ALIPAY));
            System.out.println("实际支付结果：" + result);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testWechatMissingTradeNo：观察执行路径和实际结果。
     * <p>期望结果：微信成功但交易号为空；PaymentException：渠道 WECHAT_PAY 返回的交易号为空，不能确认支付成功。</p>
     */
    @Test
    public void testWechatMissingTradeNo() {
        System.out.println("\ntestWechatMissingTradeNo ---> 期望：微信成功但交易号为空；"
                + "PaymentException：渠道 WECHAT_PAY 返回的交易号为空，不能确认支付成功");
        try {
            PaymentService service = new PaymentService(new MockAlipayClient("10000", "ALI-001",
                    false), new MockWechatPayClient(true, null, false));
            PaymentResult result = service.pay(new PaymentCommand("PAY-001", new BigDecimal("12.34"),
                    PaymentChannel.WECHAT_PAY));
            System.out.println("实际支付结果：" + result);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testAlipayNullResponse：观察执行路径和实际结果。
     * <p>期望结果：支付宝返回 null；PaymentException：支付宝响应为空或 code 非 10000，不能确认支付成功。</p>
     */
    @Test
    public void testAlipayNullResponse() {
        System.out.println("\ntestAlipayNullResponse ---> 期望：支付宝返回 null；"
                + "PaymentException：支付宝响应为空或 code 非 10000，不能确认支付成功");
        try {
            PaymentService service = new PaymentService(new NullAlipayClient(),
                    new MockWechatPayClient(true, "WX-001", false));
            PaymentResult result = service.pay(new PaymentCommand("PAY-001", new BigDecimal("12.34"),
                    PaymentChannel.ALIPAY));
            System.out.println("实际支付结果：" + result);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testWechatNullResponse：观察执行路径和实际结果。
     * <p>期望结果：微信返回 null；PaymentException：微信响应为空或 success 为 false，不能确认支付成功。</p>
     */
    @Test
    public void testWechatNullResponse() {
        System.out.println("\ntestWechatNullResponse ---> 期望：微信返回 null；"
                + "PaymentException：微信响应为空或 success 为 false，不能确认支付成功");
        try {
            PaymentService service = new PaymentService(new MockAlipayClient("10000", "ALI-001",
                    false), new NullWechatPayClient());
            PaymentResult result = service.pay(new PaymentCommand("PAY-001", new BigDecimal("12.34"),
                    PaymentChannel.WECHAT_PAY));
            System.out.println("实际支付结果：" + result);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testAlipayTimeout：观察执行路径和实际结果。
     * <p>期望结果：支付宝模拟超时；IllegalStateException：支付宝模拟超时，支付结果未知；调用一次，不重试或降级。</p>
     */
    @Test
    public void testAlipayTimeout() {
        System.out.println("\ntestAlipayTimeout ---> 期望：支付宝模拟超时；IllegalStateException：支付宝模拟超时，支付结果未知；调用一次，不重试或降级");
        try {
            PaymentService service = new PaymentService(new MockAlipayClient("10000", "ALI-001",
                    true), new MockWechatPayClient(true, "WX-001", false));
            PaymentResult result = service.pay(new PaymentCommand("PAY-001", new BigDecimal("12.34"),
                    PaymentChannel.ALIPAY));
            System.out.println("实际支付结果：" + result);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testWechatTimeout：观察执行路径和实际结果。
     * <p>期望结果：微信模拟超时；IllegalStateException：微信模拟超时，支付结果未知；调用一次，不重试或降级。</p>
     */
    @Test
    public void testWechatTimeout() {
        System.out.println("\ntestWechatTimeout ---> 期望：微信模拟超时；IllegalStateException：微信模拟超时，支付结果未知；调用一次，不重试或降级");
        try {
            PaymentService service = new PaymentService(new MockAlipayClient("10000", "ALI-001",
                    false), new MockWechatPayClient(true, "WX-001", true));
            PaymentResult result = service.pay(new PaymentCommand("PAY-001", new BigDecimal("12.34"),
                    PaymentChannel.WECHAT_PAY));
            System.out.println("实际支付结果：" + result);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 具名支付宝模拟客户端；打印每次调用次数，不使用回调或函数式封装。
     *
     * @author advance
     * @since 2026-10-27 18:35:37
     */
    private static final class MockAlipayClient implements AlipayClient {
        /** 返回的原始成功码。 */
        private final String code;
        /** 返回的交易号，可为空以模拟异常协议。 */
        private final String tradeNo;
        /** 是否模拟超时。 */
        private final boolean timeout;
        /** 当前实例的调用次数，仅用于单线程测试观察。 */
        private int calls;

        /**
         * 配置本用例的固定响应。
         * @param code 原始成功码
         * @param tradeNo 渠道交易号
         * @param timeout 是否抛出超时异常
         */
        private MockAlipayClient(String code, String tradeNo, boolean timeout) {
            this.code = code;
            this.tradeNo = tradeNo;
            this.timeout = timeout;
        }

        /**
         * 打印收到的元金额并返回配置的响应。
         * @param paymentNo 支付单号
         * @param amount 元金额
         * @return 原始模拟响应
         * @throws IllegalStateException 配置为模拟超时
         */
        @Override
        public Response pay(String paymentNo, BigDecimal amount) {
            calls++;
            System.out.println("[strategy/v2] MockAlipayClient.pay ---> 第 " + calls
                    + " 次调用，amount=" + amount + " 元");
            if (timeout) {
                throw new IllegalStateException("支付宝模拟超时，支付结果未知");
            }
            return new Response(code, tradeNo);
        }
    }

    /**
     * 具名微信模拟客户端；直观展示元转分后的参数与调用次数。
     *
     * @author advance
     * @since 2026-10-27 18:35:37
     */
    private static final class MockWechatPayClient implements WechatPayClient {
        /** 返回的原始成功标识。 */
        private final boolean success;
        /** 返回的交易号。 */
        private final String transactionId;
        /** 是否模拟超时。 */
        private final boolean timeout;
        /** 当前实例调用次数，仅用于单线程测试观察。 */
        private int calls;

        /**
         * 配置本用例的固定响应。
         * @param success 成功标识
         * @param transactionId 渠道交易号
         * @param timeout 是否模拟超时
         */
        private MockWechatPayClient(boolean success, String transactionId, boolean timeout) {
            this.success = success;
            this.transactionId = transactionId;
            this.timeout = timeout;
        }

        /**
         * 打印分金额并返回配置的响应。
         * @param paymentNo 支付单号
         * @param amountInCents 分金额
         * @return 原始模拟响应
         * @throws IllegalStateException 配置为模拟超时
         */
        @Override
        public Response createTransaction(String paymentNo, long amountInCents) {
            calls++;
            System.out.println("[strategy/v2] MockWechatPayClient.createTransaction ---> 第 " + calls
                    + " 次调用，amountInCents=" + amountInCents + " 分");
            if (timeout) {
                throw new IllegalStateException("微信模拟超时，支付结果未知");
            }
            return new Response(success, transactionId);
        }
    }

    /**
     * 模拟支付宝空响应，不发起任何真实支付。
     *
     * @author advance
     * @since 2026-10-27 18:35:37
     */
    private static final class NullAlipayClient implements AlipayClient {
        /**
         * 返回空响应以观察调用方保护。
         * @param paymentNo 支付单号
         * @param amount 元金额
         * @return 固定返回 null
         */
        @Override
        public Response pay(String paymentNo, BigDecimal amount) {
            System.out.println("[strategy/v2] NullAlipayClient.pay ---> 返回空响应");
            return null;
        }
    }

    /**
     * 模拟微信空响应，不发起任何真实支付。
     *
     * @author advance
     * @since 2026-10-27 18:35:37
     */
    private static final class NullWechatPayClient implements WechatPayClient {
        /**
         * 返回空响应以观察调用方保护。
         * @param paymentNo 支付单号
         * @param amountInCents 分金额
         * @return 固定返回 null
         */
        @Override
        public Response createTransaction(String paymentNo, long amountInCents) {
            System.out.println("[strategy/v2] NullWechatPayClient.createTransaction ---> 返回空响应");
            return null;
        }
    }
}
