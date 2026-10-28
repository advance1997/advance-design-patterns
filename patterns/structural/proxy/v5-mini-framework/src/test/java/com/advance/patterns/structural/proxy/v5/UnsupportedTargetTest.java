package com.advance.patterns.structural.proxy.v5;

import com.advance.patterns.structural.proxy.v5.config.BusinessConfig;
import com.advance.patterns.structural.proxy.v5.tx.MiniTransactional;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 验证迷你框架的接口代理限制，不把无法代理的 public 事务方法静默当作成功。
 *
 * @author advance
 * @since 2026-10-28 10:15:29
 */
public class UnsupportedTargetTest {
    /**
     * testNoInterfaceTarget：对照调用路径与数据库实际状态。
     * <p>期望结果：容器启动失败，原因包含 MiniTransactional 方法必须通过接口暴露。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testNoInterfaceTarget() throws Exception {
        System.out.println("\ntestNoInterfaceTarget ---> 期望：容器启动失败，原因包含 MiniTransactional 方法必须通过接口暴露");
        try {
            try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
                context.register(BusinessConfig.class, NoInterfaceTarget.class);
                context.refresh();
            }
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (Exception exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 故意未实现任何接口的事务目标，不属于迷你框架支持范围。
     *
     * @author advance
     * @since 2026-10-28 10:15:29
     */
    public static final class NoInterfaceTarget {
        /**
         * 不应被调用；容器必须先拒绝无法创建 JDK 接口代理的定义。
         */
        @MiniTransactional
        public void review() {
            System.out.println("NoInterfaceTarget.review ---> 不应进入此方法");
        }
    }
}
