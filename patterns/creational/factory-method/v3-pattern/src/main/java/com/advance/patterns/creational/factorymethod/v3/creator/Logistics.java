package com.advance.patterns.creational.factorymethod.v3.creator;

import com.advance.patterns.creational.factorymethod.v3.domain.DeliveryReceipt;
import com.advance.patterns.creational.factorymethod.v3.domain.DeliveryRequest;
import com.advance.patterns.creational.factorymethod.v3.product.Transport;

import java.util.Objects;

/**
 * 物流创建者，固定配送流程并将产品创建交给子类。
 *
 * @author advance
 * @since 2026-10-26 23:05:19
 */
public abstract class Logistics {

    /**
     * 创建无状态的 Logistics 实例；请求数据由每次方法调用传入。
     */
    public Logistics() {
    }

    /**
     * 执行稳定配送流程，调用工厂方法并委托产品配送。
     *
     * @param request 配送请求，不允许为 null
     * @return 配送执行结果
     * @throws NullPointerException 请求或配送方式为空时抛出
     */
    public final DeliveryReceipt planDelivery(DeliveryRequest request) {
        System.out.println("[factory-method/v3] Logistics.planDelivery ---> 开始执行");
        try {
            Objects.requireNonNull(request, "配送请求 request 不能为空");
            Transport transport = Objects.requireNonNull(
                    createTransport(),
                    "工厂方法 createTransport 必须返回非空运输产品"
            );
            return transport.deliver(request);
        } catch (RuntimeException failure) {
            System.out.println("[factory-method/v3] Logistics.planDelivery ---> 执行失败，异常类型：" + failure.getClass().getSimpleName()
                    + " | " + failure.getMessage());
            throw failure;
        }
    }

    /**
     * 创建当前物流场景使用的运输产品。
     *
     * @return 当前场景的运输产品
     */
    protected abstract Transport createTransport();
}
