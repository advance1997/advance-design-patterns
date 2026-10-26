package com.advance.patterns.creational.factorymethod.v3.product;

import com.advance.patterns.creational.factorymethod.v3.domain.DeliveryReceipt;
import com.advance.patterns.creational.factorymethod.v3.domain.DeliveryRequest;

public interface Transport {

    DeliveryReceipt deliver(DeliveryRequest request);
}
