package com.advance.patterns.creational.factorymethod.v2.factory;

import com.advance.patterns.creational.factorymethod.v2.domain.*;
import com.advance.patterns.creational.factorymethod.v2.product.*;
import com.advance.patterns.creational.factorymethod.v2.service.*;
import org.junit.jupiter.api.Test;

/**
 * TransportFactoryTest 的独立打印演示；运行完成不代表结果已自动判定正确。
 *
 * @author advance
 * @since 2026-10-26 23:26:13
 */
public class TransportFactoryTest {
    /**
     * 演示工厂返回 TruckTransport。
     * <p>期望结果：工厂返回 TruckTransport。</p>
     */
    @Test
    public void testCreateRoad() {
        System.out.println("\ntestCreateRoad ---> 期望：工厂返回 TruckTransport");
        Transport transport = TransportFactory.create(DeliveryMethod.ROAD);
        System.out.println("实际产品类名：" + transport.getClass().getSimpleName());
    }

    /**
     * 演示工厂返回 ShipTransport。
     * <p>期望结果：工厂返回 ShipTransport。</p>
     */
    @Test
    public void testCreateSea() {
        System.out.println("\ntestCreateSea ---> 期望：工厂返回 ShipTransport");
        Transport transport = TransportFactory.create(DeliveryMethod.SEA);
        System.out.println("实际产品类名：" + transport.getClass().getSimpleName());
    }

    /**
     * 演示NullPointerException，工厂拒绝空配送方式。
     * <p>期望结果：NullPointerException，工厂拒绝空配送方式。</p>
     */
    @Test
    public void testNullMethod() {
        System.out.println("\ntestNullMethod ---> 期望：NullPointerException，工厂拒绝空配送方式");
        try {
            TransportFactory.create(null);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }
}
