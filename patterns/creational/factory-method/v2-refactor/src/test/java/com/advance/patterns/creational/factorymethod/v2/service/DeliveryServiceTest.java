package com.advance.patterns.creational.factorymethod.v2.service;

import com.advance.patterns.creational.factorymethod.v2.domain.*;
import com.advance.patterns.creational.factorymethod.v2.product.*;
import com.advance.patterns.creational.factorymethod.v2.service.*;
import org.junit.jupiter.api.Test;

/**
 * DeliveryServiceTest 的独立打印演示；运行完成不代表结果已自动判定正确。
 *
 * @author advance
 * @since 2026-10-26 23:26:13
 */
public class DeliveryServiceTest {
    /**
     * 演示使用卡车 TRUCK，订单为 ORDER-001，目的地为 Shanghai，输出入口、创建与配送路径。
     * <p>期望结果：使用卡车 TRUCK，订单为 ORDER-001，目的地为 Shanghai，输出入口、创建与配送路径。</p>
     */
    @Test
    public void testRoadDelivery() {
        System.out.println("\ntestRoadDelivery ---> 期望：使用卡车 TRUCK，订单为 ORDER-001，目的地为 Shanghai，输出入口、创建与配送路径");
        DeliveryRequest request = new DeliveryRequest("ORDER-001", "Shanghai");
        DeliveryReceipt receipt = new DeliveryService().arrange(DeliveryMethod.ROAD, request);
        System.out.println("实际配送结果：" + receipt);
    }

    /**
     * 演示使用船舶 SHIP，订单为 ORDER-001，目的地为 Shanghai，输出入口、创建与配送路径。
     * <p>期望结果：使用船舶 SHIP，订单为 ORDER-001，目的地为 Shanghai，输出入口、创建与配送路径。</p>
     */
    @Test
    public void testSeaDelivery() {
        System.out.println("\ntestSeaDelivery ---> 期望：使用船舶 SHIP，订单为 ORDER-001，目的地为 Shanghai，输出入口、创建与配送路径");
        DeliveryRequest request = new DeliveryRequest("ORDER-001", "Shanghai");
        DeliveryReceipt receipt = new DeliveryService().arrange(DeliveryMethod.SEA, request);
        System.out.println("实际配送结果：" + receipt);
    }
}
