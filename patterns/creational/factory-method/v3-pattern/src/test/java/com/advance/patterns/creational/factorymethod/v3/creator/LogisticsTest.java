package com.advance.patterns.creational.factorymethod.v3.creator;

import com.advance.patterns.creational.factorymethod.v3.domain.DeliveryReceipt;
import com.advance.patterns.creational.factorymethod.v3.domain.DeliveryRequest;
import com.advance.patterns.creational.factorymethod.v3.product.Transport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 验证LogisticsTest对应的正常路径、边界条件及异常行为。
 *
 * @author advance
 * @since 2026-10-26 23:05:19
 */
class LogisticsTest {

    /**
     * 验证公路创建者通过工厂方法提供卡车产品。
     */
    @Test
    void roadCreatorShouldUseTruckProduct() {
        Logistics logistics = new RoadLogistics();

        DeliveryReceipt receipt = logistics.planDelivery(
                new DeliveryRequest("ORDER-3001", "Beijing")
        );

        assertEquals(new DeliveryReceipt("ORDER-3001", "TRUCK", "Beijing"), receipt);
    }

    /**
     * 验证海运创建者通过工厂方法提供船舶产品。
     */
    @Test
    void seaCreatorShouldUseShipProduct() {
        Logistics logistics = new SeaLogistics();

        DeliveryReceipt receipt = logistics.planDelivery(
                new DeliveryRequest("ORDER-3002", "Hamburg")
        );

        assertEquals(new DeliveryReceipt("ORDER-3002", "SHIP", "Hamburg"), receipt);
    }

    /**
     * 验证新增空运创建者和产品即可复用稳定流程，无需修改现有创建者。
     */
    @Test
    void newTransportCanBeAddedWithoutChangingExistingCreators() {
        Logistics airLogistics = new Logistics() {
            /**
             * 创建当前物流场景使用的运输产品。
             *
             * @return 当前场景的运输产品
             */
            @Override
            protected Transport createTransport() {
                return request -> new DeliveryReceipt(
                        request.orderId(),
                        "AIRPLANE",
                        request.destination()
                );
            }
        };

        DeliveryReceipt receipt = airLogistics.planDelivery(
                new DeliveryRequest("ORDER-3003", "New York")
        );

        assertEquals(new DeliveryReceipt("ORDER-3003", "AIRPLANE", "New York"), receipt);
    }

    /**
     * 验证稳定配送流程在调用工厂之前拒绝空请求。
     */
    @Test
    void stableProcessShouldRejectMissingRequest() {
        Logistics logistics = new RoadLogistics();

        assertThrows(NullPointerException.class, () -> logistics.planDelivery(null));
    }
}
