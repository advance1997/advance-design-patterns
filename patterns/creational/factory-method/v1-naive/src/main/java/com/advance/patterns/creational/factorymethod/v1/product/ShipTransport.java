package com.advance.patterns.creational.factorymethod.v1.product;

import com.advance.patterns.creational.factorymethod.v1.domain.DeliveryReceipt;
import com.advance.patterns.creational.factorymethod.v1.domain.DeliveryRequest;

/**
 * 海运产品实现，使用船舶完成模拟配送。
 *
 * @author advance
 * @since 2026-10-26 23:05:19
 */
public final class ShipTransport implements Transport {

    /**
     * 创建无状态的 ShipTransport 实例；请求数据由每次方法调用传入。
     */
    public ShipTransport() {
    }

    /**
     * 执行当前运输产品的模拟配送，返回统一配送结果。
     *
     * @param request 配送请求，不允许为 null
     * @return 配送执行结果
     * @throws NullPointerException 请求或配送方式为空时抛出
     */
    @Override
    public DeliveryReceipt deliver(DeliveryRequest request) {
        System.out.println("[factory-method/v1] ShipTransport.deliver ---> 开始执行");
        try {
            java.util.Objects.requireNonNull(request, "配送请求 request 不能为空");
            return new DeliveryReceipt(request.getOrderId(), "SHIP", request.getDestination());
        } catch (RuntimeException failure) {
            System.out.println("[factory-method/v1] ShipTransport.deliver ---> 执行失败，异常类型：" + failure.getClass().getSimpleName()
                    + " | " + failure.getMessage());
            throw failure;
        }
    }
}
