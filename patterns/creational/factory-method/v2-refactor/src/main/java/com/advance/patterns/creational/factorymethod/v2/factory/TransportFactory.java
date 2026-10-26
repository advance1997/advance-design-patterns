package com.advance.patterns.creational.factorymethod.v2.factory;

import com.advance.patterns.creational.factorymethod.v2.domain.DeliveryMethod;
import com.advance.patterns.creational.factorymethod.v2.product.ShipTransport;
import com.advance.patterns.creational.factorymethod.v2.product.Transport;
import com.advance.patterns.creational.factorymethod.v2.product.TruckTransport;

import java.util.Objects;

/**
 * 简单工厂，按照配送方式创建对应运输产品。
 *
 * @author advance
 * @since 2026-10-26 23:05:19
 */
public final class TransportFactory {

    /**
     * 创建 TransportFactory 实例并初始化依赖或业务数据。
     */
    private TransportFactory() {
    }

    /**
     * 根据配送方式创建运输产品；不支持 null 方式。
     *
     * @param method 配送方式，不允许为 null
     * @return 当前场景的运输产品
     */
    public static Transport create(DeliveryMethod method) {
        System.out.println("[factory-method/v2] TransportFactory.create ---> 开始执行");
        try {
            Objects.requireNonNull(method, "配送方式 method 不能为空");
            return switch (method) {
                case ROAD -> new TruckTransport();
                case SEA -> new ShipTransport();
            };
        } catch (RuntimeException failure) {
            System.out.println("[factory-method/v2] TransportFactory.create ---> 执行失败，异常类型：" + failure.getClass().getSimpleName()
                    + " | " + failure.getMessage());
            throw failure;
        }
    }
}
