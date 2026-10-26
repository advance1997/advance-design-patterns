package com.advance.patterns.creational.factorymethod.v2;

import com.advance.patterns.creational.factorymethod.v2.domain.*;
import com.advance.patterns.creational.factorymethod.v2.product.*;
import com.advance.patterns.creational.factorymethod.v2.service.*;
import org.junit.jupiter.api.Test;

/**
 * ExceptionPathTest 的独立打印演示；运行完成不代表结果已自动判定正确。
 *
 * @author advance
 * @since 2026-10-26 23:26:13
 */
public class ExceptionPathTest {
    /**
     * 演示IllegalArgumentException，订单标识不能为空白。
     * <p>期望结果：IllegalArgumentException，订单标识不能为空白。</p>
     */
    @Test
    public void testBlankOrderId() {
        System.out.println("\ntestBlankOrderId ---> 期望：IllegalArgumentException，订单标识不能为空白");
        try {
            new DeliveryRequest(" ", "Shanghai");
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示IllegalArgumentException，订单标识不能为空白。
     * <p>期望结果：IllegalArgumentException，订单标识不能为空白。</p>
     */
    @Test
    public void testNullOrderId() {
        System.out.println("\ntestNullOrderId ---> 期望：IllegalArgumentException，订单标识不能为空白");
        try {
            new DeliveryRequest(null, "Shanghai");
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示IllegalArgumentException，目的地不能为空白。
     * <p>期望结果：IllegalArgumentException，目的地不能为空白。</p>
     */
    @Test
    public void testBlankDestination() {
        System.out.println("\ntestBlankDestination ---> 期望：IllegalArgumentException，目的地不能为空白");
        try {
            new DeliveryRequest("ORDER-001", " ");
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示IllegalArgumentException，目的地不能为空白。
     * <p>期望结果：IllegalArgumentException，目的地不能为空白。</p>
     */
    @Test
    public void testNullDestination() {
        System.out.println("\ntestNullDestination ---> 期望：IllegalArgumentException，目的地不能为空白");
        try {
            new DeliveryRequest("ORDER-001", null);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示NullPointerException，配送请求不能为空，不进入配送。
     * <p>期望结果：NullPointerException，配送请求不能为空，不进入配送。</p>
     */
    @Test
    public void testNullRequest() {
        System.out.println("\ntestNullRequest ---> 期望：NullPointerException，配送请求不能为空，不进入配送");
        try {
            new DeliveryService().arrange(DeliveryMethod.ROAD, null);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示NullPointerException，船舶产品拒绝空请求。
     * <p>期望结果：NullPointerException，船舶产品拒绝空请求。</p>
     */
    @Test
    public void testNullShipRequest() {
        System.out.println("\ntestNullShipRequest ---> 期望：NullPointerException，船舶产品拒绝空请求");
        try {
            new ShipTransport().deliver(null);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示NullPointerException，卡车产品拒绝空请求。
     * <p>期望结果：NullPointerException，卡车产品拒绝空请求。</p>
     */
    @Test
    public void testNullTruckRequest() {
        System.out.println("\ntestNullTruckRequest ---> 期望：NullPointerException，卡车产品拒绝空请求");
        try {
            new TruckTransport().deliver(null);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示NullPointerException，配送方式不能为空。
     * <p>期望结果：NullPointerException，配送方式不能为空。</p>
     */
    @Test
    public void testNullMethod() {
        System.out.println("\ntestNullMethod ---> 期望：NullPointerException，配送方式不能为空");
        try {
            new DeliveryService().arrange(null, new DeliveryRequest("ORDER-001", "Shanghai"));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }
}
