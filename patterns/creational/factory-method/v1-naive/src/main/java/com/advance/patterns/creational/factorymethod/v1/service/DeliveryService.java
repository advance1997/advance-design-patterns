package com.advance.patterns.creational.factorymethod.v1.service;

import com.advance.patterns.creational.factorymethod.v1.domain.DeliveryMethod;
import com.advance.patterns.creational.factorymethod.v1.domain.DeliveryReceipt;
import com.advance.patterns.creational.factorymethod.v1.domain.DeliveryRequest;
import com.advance.patterns.creational.factorymethod.v1.product.ShipTransport;
import com.advance.patterns.creational.factorymethod.v1.product.Transport;
import com.advance.patterns.creational.factorymethod.v1.product.TruckTransport;

import java.util.Objects;

public final class DeliveryService {

    public DeliveryReceipt arrange(DeliveryMethod method, DeliveryRequest request) {
        Objects.requireNonNull(method, "method must not be null");
        Objects.requireNonNull(request, "request must not be null");

        // V1 的问题：业务服务必须知道并直接创建每一种具体运输工具。
        Transport transport = switch (method) {
            case ROAD -> new TruckTransport();
            case SEA -> new ShipTransport();
        };
        return transport.deliver(request);
    }
}
