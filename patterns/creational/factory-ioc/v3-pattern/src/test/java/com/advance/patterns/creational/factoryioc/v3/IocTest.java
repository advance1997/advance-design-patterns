package com.advance.patterns.creational.factoryioc.v3;

import com.advance.patterns.creational.factoryioc.v3.config.ReviewConfig;
import com.advance.patterns.creational.factoryioc.v3.client.MockAgencyClient;
import com.advance.patterns.creational.factoryioc.v3.service.ReviewService;
import com.advance.patterns.creational.factoryioc.v3.factory.AgencyClientFactoryBean;
import com.advance.patterns.creational.factoryioc.v3.domain.ReviewSession;
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
     * <p>期望结果：两次调用均返回 REVIEW_PENDING；上下文关闭时释放客户端，每次调用创建并关闭不同会话。</p>
     */
    @Test
    public void testReview() {
        System.out.println("\ntestReview ---> 期望：两次调用均返回 REVIEW_PENDING；上下文关闭时释放客户端，每次调用创建并关闭不同会话");
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

    /**
     * testFactoryBeanIdentity：直接调用观察业务边界。
     * <p>期望结果：agencyClient 返回客户端，&amp;agencyClient 返回 AgencyClientFactoryBean；产品引用相同为 true。</p>
     */
    @Test
    public void testFactoryBeanIdentity() {
        System.out.println("\ntestFactoryBeanIdentity ---> 期望：agencyClient 返回客户端，"
                + "&agencyClient 返回 AgencyClientFactoryBean；产品引用相同为 true");
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ReviewConfig.class)) {
            AgencyClientFactoryBean factory =
                    context.getBean("&agencyClient", AgencyClientFactoryBean.class);
            MockAgencyClient product = context.getBean("agencyClient", MockAgencyClient.class);
            System.out.println("工厂类型：" + factory.getClass().getSimpleName());
            System.out.println("产品类型：" + product.getClass().getSimpleName());
            System.out.println("同一产品：" + (factory.getObject() == product));
        }
    }

    /**
     * testPrototypeSessions：直接调用观察业务边界。
     * <p>期望结果：直接获取两次 prototype 会话，引用相同为 false；由测试主动关闭。</p>
     */
    @Test
    public void testPrototypeSessions() {
        System.out.println("\ntestPrototypeSessions ---> 期望：直接获取两次 prototype 会话，引用相同为 false；由测试主动关闭");
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ReviewConfig.class)) {
            try (ReviewSession first = context.getBean(ReviewSession.class);
                    ReviewSession second = context.getBean(ReviewSession.class)) {
                System.out.println("两次会话引用相同：" + (first == second));
            }
        }
    }

    /**
     * testClosedSession：直接调用观察业务边界。
     * <p>期望结果：会话关闭后 bind 抛出 IllegalStateException。</p>
     */
    @Test
    public void testClosedSession() {
        System.out.println("\ntestClosedSession ---> 期望：会话关闭后 bind 抛出 IllegalStateException");
        try {
            ReviewSession session = new ReviewSession();
            session.close();
            session.bind("DEMO-001");
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testFactoryAfterDestroy：直接调用观察业务边界。
     * <p>期望结果：工厂销毁后 getObject 抛出 IllegalStateException，不重建客户端。</p>
     */
    @Test
    public void testFactoryAfterDestroy() {
        System.out.println("\ntestFactoryAfterDestroy ---> 期望：工厂销毁后 getObject 抛出 IllegalStateException，不重建客户端");
        try {
            AgencyClientFactoryBean factory;
            try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                    ReviewConfig.class)) {
                factory = context.getBean("&agencyClient", AgencyClientFactoryBean.class);
            }
            factory.getObject();
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testFailureClosesSession：直接调用观察业务边界。
     * <p>期望结果：提前关闭客户端后查询失败；仍打印 ReviewSession.close，证明异常路径释放会话。</p>
     */
    @Test
    public void testFailureClosesSession() {
        System.out.println("\ntestFailureClosesSession ---> 期望：提前关闭客户端后查询失败；仍打印 ReviewSession.close，证明异常路径释放会话");
        try {
            try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                    ReviewConfig.class)) {
                ReviewService service = context.getBean(ReviewService.class);
                context.getBean(MockAgencyClient.class).close();
                service.review("DEMO-001");
            }
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }
}
