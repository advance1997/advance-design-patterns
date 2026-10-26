package com.advance.patterns.creational.factorymethod.v3.creator;

import com.advance.patterns.creational.factorymethod.v3.product.Transport;
import com.advance.patterns.creational.factorymethod.v3.product.TruckTransport;

/**
 * 公路物流创建者，负责创建卡车运输产品。
 *
 * @author advance
 * @since 2026-10-26 23:05:19
 */
public final class RoadLogistics extends Logistics {

    /**
     * 创建无状态的 RoadLogistics 实例；请求数据由每次方法调用传入。
     */
    public RoadLogistics() {
    }

    /**
     * 创建当前物流场景使用的运输产品。
     *
     * @return 当前场景的运输产品
     */
    @Override
    protected Transport createTransport() {
        System.out.println("[factory-method/v3] RoadLogistics ---> 调用 createTransport 创建运输产品");
        return new TruckTransport();
    }
}
