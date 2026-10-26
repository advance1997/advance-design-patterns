package com.advance.patterns.creational.factorymethod.v1.product;

import com.advance.patterns.creational.factorymethod.v1.domain.DeliveryReceipt;
import com.advance.patterns.creational.factorymethod.v1.domain.DeliveryRequest;

public interface Transport {

    DeliveryReceipt deliver(DeliveryRequest request);
}
