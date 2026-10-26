package com.advance.patterns.creational.factorymethod.v3.creator;

import com.advance.patterns.creational.factorymethod.v3.domain.*;
import com.advance.patterns.creational.factorymethod.v3.product.*;
import com.advance.patterns.creational.factorymethod.v3.creator.*;
import org.junit.jupiter.api.Test;

/**
 * FailurePropagationTest 的独立打印演示；运行完成不代表结果已自动判定正确。
 *
 * @author advance
 * @since 2026-10-26 23:26:13
 */
public class FailurePropagationTest {
    /**
     * 演示IllegalStateException，创建失败后不执行配送。
     * <p>期望结果：IllegalStateException，创建失败后不执行配送。</p>
     */
    @Test
    public void testCreationFailure() {
        System.out.println("\ntestCreationFailure ---> 期望：IllegalStateException，创建失败后不执行配送");
        try {
            Logistics logistics = new CreationFailureLogistics();
            logistics.planDelivery(new DeliveryRequest("ORDER-001", "Shanghai"));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示IllegalStateException，配送仅调用一次且不重试。
     * <p>期望结果：IllegalStateException，配送仅调用一次且不重试。</p>
     */
    @Test
    public void testDeliveryFailure() {
        System.out.println("\ntestDeliveryFailure ---> 期望：IllegalStateException，配送仅调用一次且不重试");
        try {
            Logistics logistics = new DeliveryFailureLogistics();
            logistics.planDelivery(new DeliveryRequest("ORDER-001", "Shanghai"));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 演示NullPointerException，工厂方法必须返回运输产品。
     * <p>期望结果：NullPointerException，工厂方法必须返回运输产品。</p>
     */
    @Test
    public void testNullProduct() {
        System.out.println("\ntestNullProduct ---> 期望：NullPointerException，工厂方法必须返回运输产品");
        try {
            Logistics logistics = new NullProductLogistics();
            logistics.planDelivery(new DeliveryRequest("ORDER-001", "Shanghai"));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 模拟CreationFailureLogistics场景的具名创建者。
     *
     * @author advance
     * @since 2026-10-26 23:26:13
     */
    private static class CreationFailureLogistics extends Logistics {
        /** @return 场景指定的运输产品 */
        @Override
        protected Transport createTransport() {
            System.out.println("CreationFailureLogistics.createTransport ---> 模拟创建失败");
            throw new IllegalStateException("模拟运输工具创建失败");
        }
    }

    /**
     * 模拟DeliveryFailureLogistics场景的具名创建者。
     *
     * @author advance
     * @since 2026-10-26 23:26:13
     */
    private static class DeliveryFailureLogistics extends Logistics {
        /** @return 场景指定的运输产品 */
        @Override
        protected Transport createTransport() {
            return new FailingTransport();
        }
    }

    /**
     * 模拟NullProductLogistics场景的具名创建者。
     *
     * @author advance
     * @since 2026-10-26 23:26:13
     */
    private static class NullProductLogistics extends Logistics {
        /** @return 场景指定的运输产品 */
        @Override
        protected Transport createTransport() {
            return null;
        }
    }

    /**
     * 模拟配送失败的产品。
     *
     * @author advance
     * @since 2026-10-26 23:26:13
     */
    private static class FailingTransport implements Transport {
        /**
         * 打印调用后抛出异常。
         * @param request 配送请求
         * @return 本场景不会正常返回
         */
        @Override
        public DeliveryReceipt deliver(DeliveryRequest request) {
            System.out.println("FailingTransport.deliver ---> 模拟配送失败，仅调用一次");
            throw new IllegalStateException("模拟运输服务不可用");
        }
    }
}
