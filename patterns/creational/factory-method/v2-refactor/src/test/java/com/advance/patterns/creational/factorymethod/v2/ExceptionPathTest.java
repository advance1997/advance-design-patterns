package com.advance.patterns.creational.factorymethod.v2;

import com.advance.patterns.creational.factorymethod.v2.domain.*;
import com.advance.patterns.creational.factorymethod.v2.product.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 验证ExceptionPathTest对应的正常路径、边界条件及异常行为。
 *
 * @author advance
 * @since 2026-10-26 23:05:19
 */
class ExceptionPathTest {
    /**
     * 验证空、空白或 null 订单号会抛出参数异常，并保留明确的字段提示。
     *
     * @param value 待校验的输入值
     */
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    void invalidOrderIdThrows(String value) {
        var failure = assertThrows(IllegalArgumentException.class,
                () -> new DeliveryRequest(value, "Shanghai"));
        assertEquals("订单标识 orderId 不能为空白", failure.getMessage());
        System.out.println("[factory-method/v2] ExceptionPathTest ---> 已捕获预期异常: " + failure);
    }

    /**
     * 验证空、空白或 null 目的地在请求构造阶段被拒绝。
     *
     * @param value 待校验的输入值
     */
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    void invalidDestinationThrows(String value) {
        var failure = assertThrows(IllegalArgumentException.class,
                () -> new DeliveryRequest("ORDER-FAIL", value));
        assertEquals("目的地 destination 不能为空白", failure.getMessage());
        System.out.println("[factory-method/v2] ExceptionPathTest ---> 已捕获预期异常: " + failure);
    }

    /**
     * 验证空请求在配送入口被拒绝，尚未进入实际配送。
     */
    @Test
    void missingRequestFailsAtEntry() {
        var failure = assertThrows(NullPointerException.class, () -> new com.advance.patterns.creational.factorymethod.v2.service.DeliveryService().arrange(DeliveryMethod.ROAD, null));
        assertEquals("配送请求 request 不能为空", failure.getMessage());
    }

    /**
     * 验证直接调用运输产品时也会检查空请求，避免依赖上层校验。
     */
    @Test
    void productsRejectNullEvenWhenCalledDirectly() {
        assertEquals("配送请求 request 不能为空",
                assertThrows(NullPointerException.class, () -> new ShipTransport().deliver(null)).getMessage());
        assertEquals("配送请求 request 不能为空",
                assertThrows(NullPointerException.class, () -> new TruckTransport().deliver(null)).getMessage());
    }

    /**
     * 验证没有指定配送方式时无法选择运输产品。
     */
    @Test
    void missingDeliveryMethodThrows() {
        var failure = assertThrows(NullPointerException.class,
                () -> new com.advance.patterns.creational.factorymethod.v2.service.DeliveryService().arrange(null,
                        /**
                         * 验证 DeliveryRequest 场景下的执行结果与异常约束。
                         *
                         * @param "ORDER-FAIL" 本步骤使用的"ORDER-FAIL"参数
                         * @param "Shanghai" 本步骤使用的"Shanghai"参数
                         * @return 当前步骤的处理结果
                         */
                        new DeliveryRequest("ORDER-FAIL", "Shanghai")));
        assertEquals("配送方式 method 不能为空", failure.getMessage());
    }
}
