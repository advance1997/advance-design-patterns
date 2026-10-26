package com.advance.patterns.creational.factorymethod.v3.creator;

import com.advance.patterns.creational.factorymethod.v3.domain.*;
import com.advance.patterns.creational.factorymethod.v3.product.Transport;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 验证FailurePropagationTest对应的正常路径、边界条件及异常行为。
 *
 * @author advance
 * @since 2026-10-26 23:05:19
 */
class FailurePropagationTest {
    /** 当前实例使用的new依赖或测试数据。 */
    private final DeliveryRequest request = new DeliveryRequest("ORDER-FAIL", "Shanghai");

    /**
     * 验证工厂创建失败时将同一异常对象传递给调用方。
     */
    @Test
    void creationFailurePropagatesUnchanged() {
        var expected = new IllegalStateException("模拟运输工具创建失败");
        Logistics logistics = new Logistics() {
            /**
             * 创建当前物流场景使用的运输产品。
             *
             * @return 当前场景的运输产品
             */
            protected Transport createTransport() {
                System.out.println("[factory-method/v3] FailurePropagationTest.createTransport ---> 模拟创建失败并抛出异常");
                throw expected;
            }
        };
        assertSame(expected, assertThrows(IllegalStateException.class, () -> logistics.planDelivery(request)));
    }

    /**
     * 验证配送失败只调用一次产品，原样抛出异常且不生成成功回执。
     */
    @Test
    void deliveryFailurePropagatesWithoutRetryOrSuccessReceipt() {
        var expected = new IllegalStateException("模拟运输服务不可用");
        var calls = new ArrayList<String>();
        Logistics logistics = new Logistics() {
            /**
             * 创建当前物流场景使用的运输产品。
             *
             * @return 当前场景的运输产品
             */
            protected Transport createTransport() {
                calls.add("create");
                return input -> {
                    calls.add("deliver");
                    System.out.println("[factory-method/v3] FailurePropagationTest.deliver ---> 模拟配送失败并抛出异常");
                    throw expected;
                };
            }
        };
        assertSame(expected, assertThrows(IllegalStateException.class, () -> logistics.planDelivery(request)));
        assertEquals(List.of("create", "deliver"), calls);
    }

    /**
     * 验证工厂返回空产品时立即拒绝执行配送。
     */
    @Test
    void nullProductIsRejectedBeforeDelivery() {
        Logistics logistics = new Logistics() {
            /**
             * 创建当前物流场景使用的运输产品。
             *
             * @return 当前场景的运输产品
             */
            protected Transport createTransport() { return null; }
        };
        assertEquals("工厂方法 createTransport 必须返回非空运输产品",
                assertThrows(NullPointerException.class, () -> logistics.planDelivery(request)).getMessage());
    }
}
