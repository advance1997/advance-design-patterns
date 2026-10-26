package com.advance.patterns.creational.factorymethod.v3.creator;

import com.advance.patterns.creational.factorymethod.v3.domain.*;
import com.advance.patterns.creational.factorymethod.v3.product.Transport;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FailurePropagationTest {
    private final DeliveryRequest request = new DeliveryRequest("ORDER-FAIL", "Shanghai");

    @Test
    void creationFailurePropagatesUnchanged() {
        var expected = new IllegalStateException("模拟运输工具创建失败");
        Logistics logistics = new Logistics() {
            protected Transport createTransport() {
                System.out.println("[factory-method/v3] TEST creator ---> throw creation failure");
                throw expected;
            }
        };
        assertSame(expected, assertThrows(IllegalStateException.class, () -> logistics.planDelivery(request)));
    }

    @Test
    void deliveryFailurePropagatesWithoutRetryOrSuccessReceipt() {
        var expected = new IllegalStateException("模拟运输服务不可用");
        var calls = new ArrayList<String>();
        Logistics logistics = new Logistics() {
            protected Transport createTransport() {
                calls.add("create");
                return input -> {
                    calls.add("deliver");
                    System.out.println("[factory-method/v3] TEST transport ---> throw delivery failure");
                    throw expected;
                };
            }
        };
        assertSame(expected, assertThrows(IllegalStateException.class, () -> logistics.planDelivery(request)));
        assertEquals(List.of("create", "deliver"), calls);
    }

    @Test
    void nullProductIsRejectedBeforeDelivery() {
        Logistics logistics = new Logistics() {
            protected Transport createTransport() { return null; }
        };
        assertEquals("factory method must return a transport",
                assertThrows(NullPointerException.class, () -> logistics.planDelivery(request)).getMessage());
    }
}
