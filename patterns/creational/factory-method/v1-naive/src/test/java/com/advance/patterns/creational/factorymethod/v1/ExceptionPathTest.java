package com.advance.patterns.creational.factorymethod.v1;

import com.advance.patterns.creational.factorymethod.v1.domain.*;
import com.advance.patterns.creational.factorymethod.v1.product.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class ExceptionPathTest {
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    void invalidOrderIdThrows(String value) {
        var failure = assertThrows(IllegalArgumentException.class,
                () -> new DeliveryRequest(value, "Shanghai"));
        assertEquals("orderId must not be blank", failure.getMessage());
        System.out.println("[factory-method/v1] TEST ---> 已捕获预期异常: " + failure);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    void invalidDestinationThrows(String value) {
        var failure = assertThrows(IllegalArgumentException.class,
                () -> new DeliveryRequest("ORDER-FAIL", value));
        assertEquals("destination must not be blank", failure.getMessage());
        System.out.println("[factory-method/v1] TEST ---> 已捕获预期异常: " + failure);
    }

    @Test
    void missingRequestFailsAtEntry() {
        var failure = assertThrows(NullPointerException.class, () -> new com.advance.patterns.creational.factorymethod.v1.service.DeliveryService().arrange(DeliveryMethod.ROAD, null));
        assertEquals("request must not be null", failure.getMessage());
    }

    @Test
    void productsRejectNullEvenWhenCalledDirectly() {
        assertEquals("request must not be null",
                assertThrows(NullPointerException.class, () -> new ShipTransport().deliver(null)).getMessage());
        assertEquals("request must not be null",
                assertThrows(NullPointerException.class, () -> new TruckTransport().deliver(null)).getMessage());
    }

    @Test
    void missingDeliveryMethodThrows() {
        var failure = assertThrows(NullPointerException.class,
                () -> new com.advance.patterns.creational.factorymethod.v1.service.DeliveryService().arrange(null,
                        new DeliveryRequest("ORDER-FAIL", "Shanghai")));
        assertEquals("method must not be null", failure.getMessage());
    }
}
