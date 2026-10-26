package com.advance.patterns.creational.factorymethod.v2.factory;

import com.advance.patterns.creational.factorymethod.v2.domain.DeliveryMethod;
import com.advance.patterns.creational.factorymethod.v2.product.ShipTransport;
import com.advance.patterns.creational.factorymethod.v2.product.TruckTransport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 验证TransportFactoryTest对应的正常路径、边界条件及异常行为。
 *
 * @author advance
 * @since 2026-10-26 23:05:19
 */
class TransportFactoryTest {

    /**
     * 验证简单工厂为公路和海运分别创建匹配的具体产品。
     */
    @Test
    void shouldCreateProductForEachDeliveryMethod() {
        assertInstanceOf(TruckTransport.class, TransportFactory.create(DeliveryMethod.ROAD));
        assertInstanceOf(ShipTransport.class, TransportFactory.create(DeliveryMethod.SEA));
    }

    /**
     * 验证简单工厂拒绝空配送方式。
     */
    @Test
    void shouldRejectMissingDeliveryMethod() {
        assertThrows(NullPointerException.class, () -> TransportFactory.create(null));
    }
}
