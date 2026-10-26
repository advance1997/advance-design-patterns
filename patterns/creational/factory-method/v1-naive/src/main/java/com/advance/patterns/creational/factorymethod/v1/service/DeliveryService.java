package com.advance.patterns.creational.factorymethod.v1.service;

import com.advance.patterns.creational.factorymethod.v1.domain.DeliveryMethod;
import com.advance.patterns.creational.factorymethod.v1.domain.DeliveryReceipt;
import com.advance.patterns.creational.factorymethod.v1.domain.DeliveryRequest;
import com.advance.patterns.creational.factorymethod.v1.product.ShipTransport;
import com.advance.patterns.creational.factorymethod.v1.product.Transport;
import com.advance.patterns.creational.factorymethod.v1.product.TruckTransport;

import java.util.Objects;

/**
 * 配送应用服务，协调运输工具选择与配送执行。
 *
 * @author advance
 * @since 2026-10-26 23:05:19
 */
public final class DeliveryService {

    /**
     * 创建无状态的 DeliveryService 实例；请求数据由每次方法调用传入。
     */
    public DeliveryService() {
    }

    /**
     * 校验配送参数，选择运输产品并执行配送。
     *
     * @param method 配送方式，不允许为 null
     * @param request 配送请求，不允许为 null
     * @return 配送执行结果
     * @throws NullPointerException 请求或配送方式为空时抛出
     */
    public DeliveryReceipt arrange(DeliveryMethod method, DeliveryRequest request) {
        System.out.println("[factory-method/v1] DeliveryService.arrange ---> 开始执行");
        try {
            Objects.requireNonNull(method, "配送方式 method 不能为空");
            Objects.requireNonNull(request, "配送请求 request 不能为空");

            // V1 的问题：业务服务必须知道并直接创建每一种具体运输工具。
            Transport transport;
            switch (method) {
                case ROAD:
                    transport = new TruckTransport();
                    break;
                case SEA:
                    transport = new ShipTransport();
                    break;
                default:
                    throw new IllegalArgumentException("不支持的配送方式：" + method);
            }
            return transport.deliver(request);
        } catch (RuntimeException failure) {
            System.out.println("[factory-method/v1] DeliveryService.arrange ---> 执行失败，异常类型：" + failure.getClass().getSimpleName()
                    + " | " + failure.getMessage());
            throw failure;
        }
    }
}
