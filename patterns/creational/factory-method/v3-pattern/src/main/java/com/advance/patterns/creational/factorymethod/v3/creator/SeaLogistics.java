package com.advance.patterns.creational.factorymethod.v3.creator;

import com.advance.patterns.creational.factorymethod.v3.product.ShipTransport;
import com.advance.patterns.creational.factorymethod.v3.product.Transport;

/**
 * 海运物流创建者，负责创建船舶运输产品。
 *
 * @author advance
 * @since 2026-10-26 23:05:19
 */
public final class SeaLogistics extends Logistics {

    /**
     * 创建无状态的 SeaLogistics 实例；请求数据由每次方法调用传入。
     */
    public SeaLogistics() {
    }

    /**
     * 创建当前物流场景使用的运输产品。
     *
     * @return 当前场景的运输产品
     */
    @Override
    protected Transport createTransport() {
        System.out.println("[factory-method/v3] SeaLogistics ---> 调用 createTransport 创建运输产品");
        return new ShipTransport();
    }
}
