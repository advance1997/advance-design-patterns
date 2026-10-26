package com.advance.patterns.creational.factorymethod.v2.product;

import com.advance.patterns.creational.factorymethod.v2.domain.DeliveryReceipt;
import com.advance.patterns.creational.factorymethod.v2.domain.DeliveryRequest;

/**
 * 运输产品接口，定义统一的配送行为。
 *
 * @author advance
 * @since 2026-10-26 23:05:19
 */
public interface Transport {

    /**
     * 执行当前运输产品的模拟配送，返回统一配送结果。
     *
     * @param request 配送请求，不允许为 null
     * @return 配送执行结果
     * @throws NullPointerException 请求或配送方式为空时抛出
     */
    DeliveryReceipt deliver(DeliveryRequest request);
}
