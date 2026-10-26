package com.advance.patterns.creational.factorymethod.v2.service;

import com.advance.patterns.creational.factorymethod.v2.domain.DeliveryMethod;
import com.advance.patterns.creational.factorymethod.v2.domain.DeliveryReceipt;
import com.advance.patterns.creational.factorymethod.v2.domain.DeliveryRequest;
import com.advance.patterns.creational.factorymethod.v2.factory.TransportFactory;
import com.advance.patterns.creational.factorymethod.v2.product.Transport;

import java.util.Objects;

public final class DeliveryService {

    public DeliveryReceipt arrange(DeliveryMethod method, DeliveryRequest request) {
        System.out.println("[factory-method/v2] DeliveryService.arrange ---> start");
        try {
            Objects.requireNonNull(request, "request must not be null");
            Transport transport = TransportFactory.create(method);
            return transport.deliver(request);
        } catch (RuntimeException failure) {
            System.out.println("[factory-method/v2] DeliveryService.arrange ---> failure: " + failure.getClass().getSimpleName()
                    + " | " + failure.getMessage());
            throw failure;
        }
    }
}
