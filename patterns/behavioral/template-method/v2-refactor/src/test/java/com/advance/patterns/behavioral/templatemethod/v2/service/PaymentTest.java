package com.advance.patterns.behavioral.templatemethod.v2.service;

import com.advance.patterns.behavioral.templatemethod.v2.domain.PaymentCommand;
import com.advance.patterns.behavioral.templatemethod.v2.domain.PaymentResult;
import com.advance.patterns.behavioral.templatemethod.v2.gateway.AlipayClient;
import com.advance.patterns.behavioral.templatemethod.v2.gateway.WechatPayClient;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

/**
 * PaymentTest 打印演示；每个场景独立准备数据，人工对照期望值。
 *
 * @author advance
 * @since 2026-10-26 23:26:13
 */
public class PaymentTest {
    /**
     * 演示 testAlipaySuccess 对应的支付场景。
     * <p>期望结果：渠道 ALIPAY，流水 ALI-001；客户端金额 12.34 元；路径为前置、渠道、成功、结束。</p>
     */
    @Test
    public void testAlipaySuccess() {
        System.out.println("\ntestAlipaySuccess ---> 期望：渠道 ALIPAY，流水 ALI-001；客户端金额 12.34 元；路径为前置、渠道、成功、结束");
        AlipayClient client = new MockAlipayClient();
        AlipayPayment payment = new AlipayPayment(client);
        PaymentCommand command = new PaymentCommand("PAY-001", new BigDecimal("12.34"));
        PaymentResult result = payment.pay(command);
        System.out.println("实际支付结果：" + result);
    }

    /**
     * 演示 testAlipayRejected 对应的支付场景。
     * <p>期望结果：PaymentException，渠道拒绝后执行失败和结束步骤，不执行成功步骤。</p>
     */
    @Test
    public void testAlipayRejected() {
        System.out.println("\ntestAlipayRejected ---> 期望：PaymentException，渠道拒绝后执行失败和结束步骤，不执行成功步骤");
        try {
            AlipayClient client = new RejectedAlipayClient();
            AlipayPayment payment = new AlipayPayment(client);
            PaymentCommand command = new PaymentCommand("PAY-001", new BigDecimal("12.34"));
            PaymentResult result = payment.pay(command);
            System.out.println("实际支付结果：" + result);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示 testAlipayTimeout 对应的支付场景。
     * <p>期望结果：IllegalStateException，模拟网关超时，只调用一次，不自动重试。</p>
     */
    @Test
    public void testAlipayTimeout() {
        System.out.println("\ntestAlipayTimeout ---> 期望：IllegalStateException，模拟网关超时，只调用一次，不自动重试");
        try {
            AlipayClient client = new TimeoutAlipayClient();
            AlipayPayment payment = new AlipayPayment(client);
            PaymentCommand command = new PaymentCommand("PAY-001", new BigDecimal("12.34"));
            PaymentResult result = payment.pay(command);
            System.out.println("实际支付结果：" + result);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示 testAlipayMalformed 对应的支付场景。
     * <p>期望结果：PaymentException，缺少有效交易号不能视为成功。</p>
     */
    @Test
    public void testAlipayMalformed() {
        System.out.println("\ntestAlipayMalformed ---> 期望：PaymentException，缺少有效交易号不能视为成功");
        try {
            AlipayClient client = new MalformedAlipayClient();
            AlipayPayment payment = new AlipayPayment(client);
            PaymentCommand command = new PaymentCommand("PAY-001", new BigDecimal("12.34"));
            PaymentResult result = payment.pay(command);
            System.out.println("实际支付结果：" + result);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示 testAlipayNullResponse 对应的支付场景。
     * <p>期望结果：PaymentException，空渠道响应被拒绝。</p>
     */
    @Test
    public void testAlipayNullResponse() {
        System.out.println("\ntestAlipayNullResponse ---> 期望：PaymentException，空渠道响应被拒绝");
        try {
            AlipayClient client = new NullAlipayClient();
            AlipayPayment payment = new AlipayPayment(client);
            PaymentCommand command = new PaymentCommand("PAY-001", new BigDecimal("12.34"));
            PaymentResult result = payment.pay(command);
            System.out.println("实际支付结果：" + result);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示 testAlipayBlankNumber 对应的支付场景。
     * <p>期望结果：IllegalArgumentException，空白单号被拒绝，不应出现客户端调用输出。</p>
     */
    @Test
    public void testAlipayBlankNumber() {
        System.out.println("\ntestAlipayBlankNumber ---> 期望：IllegalArgumentException，空白单号被拒绝，不应出现客户端调用输出");
        try {
            AlipayPayment payment = new AlipayPayment(new MockAlipayClient());
            payment.pay(new PaymentCommand(" ", BigDecimal.ONE));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示 testAlipayZeroAmount 对应的支付场景。
     * <p>期望结果：IllegalArgumentException，零金额被拒绝，不应出现客户端调用输出。</p>
     */
    @Test
    public void testAlipayZeroAmount() {
        System.out.println("\ntestAlipayZeroAmount ---> 期望：IllegalArgumentException，零金额被拒绝，不应出现客户端调用输出");
        try {
            AlipayPayment payment = new AlipayPayment(new MockAlipayClient());
            payment.pay(new PaymentCommand("PAY-001", BigDecimal.ZERO));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示 testAlipayNegativeAmount 对应的支付场景。
     * <p>期望结果：IllegalArgumentException，负数金额被拒绝。</p>
     */
    @Test
    public void testAlipayNegativeAmount() {
        System.out.println("\ntestAlipayNegativeAmount ---> 期望：IllegalArgumentException，负数金额被拒绝");
        try {
            AlipayPayment payment = new AlipayPayment(new MockAlipayClient());
            payment.pay(new PaymentCommand("PAY-001", new BigDecimal("-1")));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示 testAlipayFractionalCent 对应的支付场景。
     * <p>期望结果：IllegalArgumentException，超过两位小数被拒绝。</p>
     */
    @Test
    public void testAlipayFractionalCent() {
        System.out.println("\ntestAlipayFractionalCent ---> 期望：IllegalArgumentException，超过两位小数被拒绝");
        try {
            AlipayPayment payment = new AlipayPayment(new MockAlipayClient());
            payment.pay(new PaymentCommand("PAY-001", new BigDecimal("1.001")));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示 testAlipayNullAmount 对应的支付场景。
     * <p>期望结果：IllegalArgumentException，空金额被拒绝。</p>
     */
    @Test
    public void testAlipayNullAmount() {
        System.out.println("\ntestAlipayNullAmount ---> 期望：IllegalArgumentException，空金额被拒绝");
        try {
            AlipayPayment payment = new AlipayPayment(new MockAlipayClient());
            payment.pay(new PaymentCommand("PAY-001", null));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示 testAlipayNullCommand 对应的支付场景。
     * <p>期望结果：NullPointerException，空支付命令被拒绝。</p>
     */
    @Test
    public void testAlipayNullCommand() {
        System.out.println("\ntestAlipayNullCommand ---> 期望：NullPointerException，空支付命令被拒绝");
        try {
            AlipayPayment payment = new AlipayPayment(new MockAlipayClient());
            payment.pay(null);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示 testWechatSuccess 对应的支付场景。
     * <p>期望结果：渠道 WECHAT_PAY，流水 WX-001；客户端金额 1234 分；路径为前置、渠道、成功、结束。</p>
     */
    @Test
    public void testWechatSuccess() {
        System.out.println("\ntestWechatSuccess ---> 期望：渠道 WECHAT_PAY，流水 WX-001；客户端金额 1234 分；路径为前置、渠道、成功、结束");
        WechatPayClient client = new MockWechatPayClient();
        WechatPayment payment = new WechatPayment(client);
        PaymentCommand command = new PaymentCommand("PAY-001", new BigDecimal("12.34"));
        PaymentResult result = payment.pay(command);
        System.out.println("实际支付结果：" + result);
    }

    /**
     * 演示 testWechatRejected 对应的支付场景。
     * <p>期望结果：PaymentException，渠道拒绝后执行失败和结束步骤，不执行成功步骤。</p>
     */
    @Test
    public void testWechatRejected() {
        System.out.println("\ntestWechatRejected ---> 期望：PaymentException，渠道拒绝后执行失败和结束步骤，不执行成功步骤");
        try {
            WechatPayClient client = new RejectedWechatPayClient();
            WechatPayment payment = new WechatPayment(client);
            PaymentCommand command = new PaymentCommand("PAY-001", new BigDecimal("12.34"));
            PaymentResult result = payment.pay(command);
            System.out.println("实际支付结果：" + result);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示 testWechatTimeout 对应的支付场景。
     * <p>期望结果：IllegalStateException，模拟网关超时，只调用一次，不自动重试。</p>
     */
    @Test
    public void testWechatTimeout() {
        System.out.println("\ntestWechatTimeout ---> 期望：IllegalStateException，模拟网关超时，只调用一次，不自动重试");
        try {
            WechatPayClient client = new TimeoutWechatPayClient();
            WechatPayment payment = new WechatPayment(client);
            PaymentCommand command = new PaymentCommand("PAY-001", new BigDecimal("12.34"));
            PaymentResult result = payment.pay(command);
            System.out.println("实际支付结果：" + result);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示 testWechatMalformed 对应的支付场景。
     * <p>期望结果：PaymentException，缺少有效交易号不能视为成功。</p>
     */
    @Test
    public void testWechatMalformed() {
        System.out.println("\ntestWechatMalformed ---> 期望：PaymentException，缺少有效交易号不能视为成功");
        try {
            WechatPayClient client = new MalformedWechatPayClient();
            WechatPayment payment = new WechatPayment(client);
            PaymentCommand command = new PaymentCommand("PAY-001", new BigDecimal("12.34"));
            PaymentResult result = payment.pay(command);
            System.out.println("实际支付结果：" + result);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示 testWechatNullResponse 对应的支付场景。
     * <p>期望结果：PaymentException，空渠道响应被拒绝。</p>
     */
    @Test
    public void testWechatNullResponse() {
        System.out.println("\ntestWechatNullResponse ---> 期望：PaymentException，空渠道响应被拒绝");
        try {
            WechatPayClient client = new NullWechatPayClient();
            WechatPayment payment = new WechatPayment(client);
            PaymentCommand command = new PaymentCommand("PAY-001", new BigDecimal("12.34"));
            PaymentResult result = payment.pay(command);
            System.out.println("实际支付结果：" + result);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示 testWechatBlankNumber 对应的支付场景。
     * <p>期望结果：IllegalArgumentException，空白单号被拒绝，不应出现客户端调用输出。</p>
     */
    @Test
    public void testWechatBlankNumber() {
        System.out.println("\ntestWechatBlankNumber ---> 期望：IllegalArgumentException，空白单号被拒绝，不应出现客户端调用输出");
        try {
            WechatPayment payment = new WechatPayment(new MockWechatPayClient());
            payment.pay(new PaymentCommand(" ", BigDecimal.ONE));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示 testWechatZeroAmount 对应的支付场景。
     * <p>期望结果：IllegalArgumentException，零金额被拒绝，不应出现客户端调用输出。</p>
     */
    @Test
    public void testWechatZeroAmount() {
        System.out.println("\ntestWechatZeroAmount ---> 期望：IllegalArgumentException，零金额被拒绝，不应出现客户端调用输出");
        try {
            WechatPayment payment = new WechatPayment(new MockWechatPayClient());
            payment.pay(new PaymentCommand("PAY-001", BigDecimal.ZERO));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示 testWechatNegativeAmount 对应的支付场景。
     * <p>期望结果：IllegalArgumentException，负数金额被拒绝。</p>
     */
    @Test
    public void testWechatNegativeAmount() {
        System.out.println("\ntestWechatNegativeAmount ---> 期望：IllegalArgumentException，负数金额被拒绝");
        try {
            WechatPayment payment = new WechatPayment(new MockWechatPayClient());
            payment.pay(new PaymentCommand("PAY-001", new BigDecimal("-1")));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示 testWechatFractionalCent 对应的支付场景。
     * <p>期望结果：IllegalArgumentException，超过两位小数被拒绝。</p>
     */
    @Test
    public void testWechatFractionalCent() {
        System.out.println("\ntestWechatFractionalCent ---> 期望：IllegalArgumentException，超过两位小数被拒绝");
        try {
            WechatPayment payment = new WechatPayment(new MockWechatPayClient());
            payment.pay(new PaymentCommand("PAY-001", new BigDecimal("1.001")));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示 testWechatNullAmount 对应的支付场景。
     * <p>期望结果：IllegalArgumentException，空金额被拒绝。</p>
     */
    @Test
    public void testWechatNullAmount() {
        System.out.println("\ntestWechatNullAmount ---> 期望：IllegalArgumentException，空金额被拒绝");
        try {
            WechatPayment payment = new WechatPayment(new MockWechatPayClient());
            payment.pay(new PaymentCommand("PAY-001", null));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示 testWechatNullCommand 对应的支付场景。
     * <p>期望结果：NullPointerException，空支付命令被拒绝。</p>
     */
    @Test
    public void testWechatNullCommand() {
        System.out.println("\ntestWechatNullCommand ---> 期望：NullPointerException，空支付命令被拒绝");
        try {
            WechatPayment payment = new WechatPayment(new MockWechatPayClient());
            payment.pay(null);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 渠道 ALIPAY，流水 ALI-001；客户端金额 12.34 元；路径为前置、渠道、成功、结束。
     *
     * @author advance
     * @since 2026-10-26 23:26:13
     */
    private static class MockAlipayClient implements AlipayClient {
        /**
         * 打印客户端收到的参数并返回本场景响应。
         * @param paymentNo 支付单号
         * @param amount 以元计价的金额
         * @return 本场景的模拟响应，超时场景直接抛出异常
         */
        @Override
        public Response pay(String paymentNo, BigDecimal amount) {
            System.out.println("MockAlipayClient.pay ---> 实际单号：" + paymentNo
                    + "，实际金额（元）：" + amount);
            return new Response("10000", "ALI-001");
        }
    }

    /**
     * PaymentException，渠道拒绝后执行失败和结束步骤，不执行成功步骤。
     *
     * @author advance
     * @since 2026-10-26 23:26:13
     */
    private static class RejectedAlipayClient implements AlipayClient {
        /**
         * 打印客户端收到的参数并返回本场景响应。
         * @param paymentNo 支付单号
         * @param amount 以元计价的金额
         * @return 本场景的模拟响应，超时场景直接抛出异常
         */
        @Override
        public Response pay(String paymentNo, BigDecimal amount) {
            System.out.println("RejectedAlipayClient.pay ---> 实际单号：" + paymentNo
                    + "，实际金额（元）：" + amount);
            return new Response("40004", null);
        }
    }

    /**
     * IllegalStateException，模拟网关超时，只调用一次，不自动重试。
     *
     * @author advance
     * @since 2026-10-26 23:26:13
     */
    private static class TimeoutAlipayClient implements AlipayClient {
        /**
         * 打印客户端收到的参数并返回本场景响应。
         * @param paymentNo 支付单号
         * @param amount 以元计价的金额
         * @return 本场景的模拟响应，超时场景直接抛出异常
         */
        @Override
        public Response pay(String paymentNo, BigDecimal amount) {
            System.out.println("TimeoutAlipayClient.pay ---> 实际单号：" + paymentNo
                    + "，实际金额（元）：" + amount);
            throw new IllegalStateException("模拟网关超时");
        }
    }

    /**
     * PaymentException，缺少有效交易号不能视为成功。
     *
     * @author advance
     * @since 2026-10-26 23:26:13
     */
    private static class MalformedAlipayClient implements AlipayClient {
        /**
         * 打印客户端收到的参数并返回本场景响应。
         * @param paymentNo 支付单号
         * @param amount 以元计价的金额
         * @return 本场景的模拟响应，超时场景直接抛出异常
         */
        @Override
        public Response pay(String paymentNo, BigDecimal amount) {
            System.out.println("MalformedAlipayClient.pay ---> 实际单号：" + paymentNo
                    + "，实际金额（元）：" + amount);
            return new Response("10000", " ");
        }
    }

    /**
     * PaymentException，空渠道响应被拒绝。
     *
     * @author advance
     * @since 2026-10-26 23:26:13
     */
    private static class NullAlipayClient implements AlipayClient {
        /**
         * 打印客户端收到的参数并返回本场景响应。
         * @param paymentNo 支付单号
         * @param amount 以元计价的金额
         * @return 本场景的模拟响应，超时场景直接抛出异常
         */
        @Override
        public Response pay(String paymentNo, BigDecimal amount) {
            System.out.println("NullAlipayClient.pay ---> 实际单号：" + paymentNo
                    + "，实际金额（元）：" + amount);
            return null;
        }
    }

    /**
     * 渠道 WECHAT_PAY，流水 WX-001；客户端金额 1234 分；路径为前置、渠道、成功、结束。
     *
     * @author advance
     * @since 2026-10-26 23:26:13
     */
    private static class MockWechatPayClient implements WechatPayClient {
        /**
         * 打印客户端收到的参数并返回本场景响应。
         * @param paymentNo 支付单号
         * @param amountInCents 以分计价的金额
         * @return 本场景的模拟响应，超时场景直接抛出异常
         */
        @Override
        public Response createTransaction(String paymentNo, long amountInCents) {
            System.out.println("MockWechatPayClient.createTransaction ---> 实际单号：" + paymentNo
                    + "，实际金额（分）：" + amountInCents);
            return new Response(true, "WX-001");
        }
    }

    /**
     * PaymentException，渠道拒绝后执行失败和结束步骤，不执行成功步骤。
     *
     * @author advance
     * @since 2026-10-26 23:26:13
     */
    private static class RejectedWechatPayClient implements WechatPayClient {
        /**
         * 打印客户端收到的参数并返回本场景响应。
         * @param paymentNo 支付单号
         * @param amountInCents 以分计价的金额
         * @return 本场景的模拟响应，超时场景直接抛出异常
         */
        @Override
        public Response createTransaction(String paymentNo, long amountInCents) {
            System.out.println("RejectedWechatPayClient.createTransaction ---> 实际单号：" + paymentNo
                    + "，实际金额（分）：" + amountInCents);
            return new Response(false, null);
        }
    }

    /**
     * IllegalStateException，模拟网关超时，只调用一次，不自动重试。
     *
     * @author advance
     * @since 2026-10-26 23:26:13
     */
    private static class TimeoutWechatPayClient implements WechatPayClient {
        /**
         * 打印客户端收到的参数并返回本场景响应。
         * @param paymentNo 支付单号
         * @param amountInCents 以分计价的金额
         * @return 本场景的模拟响应，超时场景直接抛出异常
         */
        @Override
        public Response createTransaction(String paymentNo, long amountInCents) {
            System.out.println("TimeoutWechatPayClient.createTransaction ---> 实际单号：" + paymentNo
                    + "，实际金额（分）：" + amountInCents);
            throw new IllegalStateException("模拟网关超时");
        }
    }

    /**
     * PaymentException，缺少有效交易号不能视为成功。
     *
     * @author advance
     * @since 2026-10-26 23:26:13
     */
    private static class MalformedWechatPayClient implements WechatPayClient {
        /**
         * 打印客户端收到的参数并返回本场景响应。
         * @param paymentNo 支付单号
         * @param amountInCents 以分计价的金额
         * @return 本场景的模拟响应，超时场景直接抛出异常
         */
        @Override
        public Response createTransaction(String paymentNo, long amountInCents) {
            System.out.println("MalformedWechatPayClient.createTransaction ---> 实际单号：" + paymentNo
                    + "，实际金额（分）：" + amountInCents);
            return new Response(true, " ");
        }
    }

    /**
     * PaymentException，空渠道响应被拒绝。
     *
     * @author advance
     * @since 2026-10-26 23:26:13
     */
    private static class NullWechatPayClient implements WechatPayClient {
        /**
         * 打印客户端收到的参数并返回本场景响应。
         * @param paymentNo 支付单号
         * @param amountInCents 以分计价的金额
         * @return 本场景的模拟响应，超时场景直接抛出异常
         */
        @Override
        public Response createTransaction(String paymentNo, long amountInCents) {
            System.out.println("NullWechatPayClient.createTransaction ---> 实际单号：" + paymentNo
                    + "，实际金额（分）：" + amountInCents);
            return null;
        }
    }

}
