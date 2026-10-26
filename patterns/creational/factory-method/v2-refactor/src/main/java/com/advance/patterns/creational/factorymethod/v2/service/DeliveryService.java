package com.advance.patterns.creational.factorymethod.v2.service;

import com.advance.patterns.creational.factorymethod.v2.domain.DeliveryMethod;
import com.advance.patterns.creational.factorymethod.v2.domain.DeliveryReceipt;
import com.advance.patterns.creational.factorymethod.v2.domain.DeliveryRequest;
import com.advance.patterns.creational.factorymethod.v2.factory.TransportFactory;
import com.advance.patterns.creational.factorymethod.v2.product.Transport;

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
        System.out.println("[factory-method/v2] DeliveryService.arrange ---> 开始执行");
        try {
            Objects.requireNonNull(request, "配送请求 request 不能为空");
            Transport transport = TransportFactory.create(method);
            return transport.deliver(request);
        } catch (RuntimeException failure) {
            System.out.println("[factory-method/v2] DeliveryService.arrange ---> 执行失败，异常类型：" + failure.getClass().getSimpleName()
                    + " | " + failure.getMessage());
            throw failure;
        }
    }
}
