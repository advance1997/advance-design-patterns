package com.advance.patterns.creational.factorymethod.v3.creator;

import com.advance.patterns.creational.factorymethod.v3.domain.DeliveryReceipt;
import com.advance.patterns.creational.factorymethod.v3.domain.DeliveryRequest;
import com.advance.patterns.creational.factorymethod.v3.product.Transport;

import java.util.Objects;

public abstract class Logistics {

    /**
     * 稳定的业务流程只依赖 Product 抽象，不关心实际创建的运输工具。
     */
    public final DeliveryReceipt planDelivery(DeliveryRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        Transport transport = Objects.requireNonNull(
                createTransport(),
                "factory method must return a transport"
        );
        return transport.deliver(request);
    }

    /**
     * Factory Method：由子类决定实例化哪一种 Product。
     */
    protected abstract Transport createTransport();
}
