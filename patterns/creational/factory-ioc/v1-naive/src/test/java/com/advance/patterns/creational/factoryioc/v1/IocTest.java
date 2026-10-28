package com.advance.patterns.creational.factoryioc.v1;

import com.advance.patterns.creational.factoryioc.v1.config.ReviewConfig;
import com.advance.patterns.creational.factoryioc.v1.client.MockAgencyClient;
import com.advance.patterns.creational.factoryioc.v1.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 真实 Spring 容器的生命周期与依赖边界演示；打印结果供人工对照。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public class IocTest {
    /**
     * testReview：直接调用观察业务边界。
     * <p>期望结果：两次调用均返回 REVIEW_PENDING；上下文关闭时释放客户端。</p>
     */
    @Test
    public void testReview() {
        System.out.println("\ntestReview ---> 期望：两次调用均返回 REVIEW_PENDING；上下文关闭时释放客户端");
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ReviewConfig.class)) {
            ReviewService service = context.getBean(ReviewService.class);
            System.out.println("第一次结果：" + service.review("DEMO-001"));
            System.out.println("第二次结果：" + service.review("DEMO-002"));
        }
    }

    /**
     * testBeanFactory：直接调用观察业务边界。
     * <p>期望结果：BeanFactory 是容器访问接口；同一上下文两次获取客户端，引用相同为 true。</p>
     */
    @Test
    public void testBeanFactory() {
        System.out.println("\ntestBeanFactory ---> 期望：BeanFactory 是容器访问接口；同一上下文两次获取客户端，引用相同为 true");
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ReviewConfig.class)) {
            BeanFactory factory = context;
            Object first = factory.getBean("agencyClient");
            Object second = factory.getBean("agencyClient");
            System.out.println("同一容器客户端引用相同：" + (first == second));
        }
    }

    /**
     * testBlankCase：直接调用观察业务边界。
     * <p>期望结果：空编号抛出 IllegalArgumentException，不执行模拟查询。</p>
     */
    @Test
    public void testBlankCase() {
        System.out.println("\ntestBlankCase ---> 期望：空编号抛出 IllegalArgumentException，不执行模拟查询");
        try {
            try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                    ReviewConfig.class)) {
                context.getBean(ReviewService.class).review(" ");
            }
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testClosedClient：直接调用观察业务边界。
     * <p>期望结果：上下文关闭后使用已取得客户端，IllegalStateException。</p>
     */
    @Test
    public void testClosedClient() {
        System.out.println("\ntestClosedClient ---> 期望：上下文关闭后使用已取得客户端，IllegalStateException");
        try {
            MockAgencyClient client;
            try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                    ReviewConfig.class)) {
                client = context.getBean(MockAgencyClient.class);
            }
            client.review("DEMO-001");
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }
}
