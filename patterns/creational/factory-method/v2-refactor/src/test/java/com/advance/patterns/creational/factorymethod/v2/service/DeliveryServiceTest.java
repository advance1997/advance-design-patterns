package com.advance.patterns.creational.factorymethod.v2.service;

import com.advance.patterns.creational.factorymethod.v2.domain.DeliveryMethod;
import com.advance.patterns.creational.factorymethod.v2.domain.DeliveryReceipt;
import com.advance.patterns.creational.factorymethod.v2.domain.DeliveryRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 验证DeliveryServiceTest对应的正常路径、边界条件及异常行为。
 *
 * @author advance
 * @since 2026-10-26 23:05:19
 */
class DeliveryServiceTest {

    /** 当前实例使用的new依赖或测试数据。 */
    private final DeliveryService service = new DeliveryService();

    /**
     * 验证配送服务委托简单工厂创建的产品执行配送。
     */
    @Test
    void shouldDelegateDeliveryToFactoryProduct() {
        DeliveryReceipt receipt = service.arrange(
                DeliveryMethod.SEA,
                new DeliveryRequest("ORDER-2001", "Singapore")
        );

        assertEquals(new DeliveryReceipt("ORDER-2001", "SHIP", "Singapore"), receipt);
    }
}
