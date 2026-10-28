package com.advance.patterns.structural.proxy.v4;

import com.advance.patterns.structural.proxy.v4.config.JdkTransactionConfig;
import com.advance.patterns.structural.proxy.v4.database.DemoDatabase;
import com.advance.patterns.structural.proxy.v4.service.AssignmentService;
import com.advance.patterns.structural.proxy.v4.service.CaseAssignmentService;
import org.springframework.aop.framework.Advised;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import java.lang.reflect.Proxy;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Spring JDK事务语义对照；每个用例启动独立容器和 H2 数据库。
 *
 * @author advance
 * @since 2026-10-28 10:15:29
 */
public class SpringTransactionTest {
    /**
     * 对照正常的跨 Bean REQUIRED 参与路径。
     * <p>期望结果：数据库 REVIEW-A / 1；内层加入外层，最终只进行一次物理提交。</p>
     * @throws Exception 数据库操作异常
     */
    @Test
    public void testRequiredSuccess() throws Exception {
        System.out.println("\ntestRequiredSuccess ---> 期望：内层加入同一事务，最终仅提交一次，REVIEW-A / 1");
        try (AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(JdkTransactionConfig.class)) {
            AssignmentService service = context.getBean(AssignmentService.class);
            service.assignWithAudit("DEMO-001", "REVIEW-A");
            System.out.println("实际数据库：" + context.getBean(DemoDatabase.class).snapshot("DEMO-001"));
        }
    }

    /**
     * testProxyType：对照调用路径与数据库实际状态。
     * <p>期望结果：JDK 代理为 true；代理属于目标实现类为 false。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testProxyType() throws Exception {
        System.out.println("\ntestProxyType ---> 期望：JDK 代理为 true；代理属于目标实现类为 false");
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                JdkTransactionConfig.class)) {
            DemoDatabase database = context.getBean(DemoDatabase.class);
            AssignmentService service = context.getBean(AssignmentService.class);
            System.out.println("代理类：" + service.getClass().getName());
            System.out.println("JDK 代理：" + Proxy.isProxyClass(service.getClass()));
            System.out.println("代理属于目标实现类：" + (service instanceof CaseAssignmentService));
            System.out.println("Spring 事务活动：" + TransactionSynchronizationManager.isActualTransactionActive());
            System.out.println("线程仍绑定数据源：" + TransactionSynchronizationManager.hasResource(database.getDataSource()));
        }
    }

    /**
     * testSuccess：对照调用路径与数据库实际状态。
     * <p>期望结果：数据库 REVIEW-A / 1，事务结束后无线程绑定。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testSuccess() throws Exception {
        System.out.println("\ntestSuccess ---> 期望：数据库 REVIEW-A / 1，事务结束后无线程绑定");
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                JdkTransactionConfig.class)) {
            DemoDatabase database = context.getBean(DemoDatabase.class);
            AssignmentService service = context.getBean(AssignmentService.class);
            service.assign("DEMO-001", "REVIEW-A", false);
            System.out.println("实际数据库：" + database.snapshot("DEMO-001"));
            System.out.println("Spring 事务活动：" + TransactionSynchronizationManager.isActualTransactionActive());
            System.out.println("线程仍绑定数据源：" + TransactionSynchronizationManager.hasResource(database.getDataSource()));
        }
    }

    /**
     * testRuntimeRollback：对照调用路径与数据库实际状态。
     * <p>期望结果：IllegalStateException 原样传播；数据库 UNASSIGNED / 0。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testRuntimeRollback() throws Exception {
        System.out.println("\ntestRuntimeRollback ---> 期望：IllegalStateException 原样传播；数据库 UNASSIGNED / 0");
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                JdkTransactionConfig.class)) {
            DemoDatabase database = context.getBean(DemoDatabase.class);
            AssignmentService service = context.getBean(AssignmentService.class);
            try {
                service.assign("DEMO-001", "REVIEW-A", true);
                System.out.println("实际结果：未抛出异常，请对照期望检查");
            } catch (Exception exception) {
                System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
                System.out.println("实际异常信息：" + exception.getMessage());
            }
            System.out.println("实际数据库：" + database.snapshot("DEMO-001"));
            System.out.println("Spring 事务活动：" + TransactionSynchronizationManager.isActualTransactionActive());
            System.out.println("线程仍绑定数据源：" + TransactionSynchronizationManager.hasResource(database.getDataSource()));
        }
    }

    /**
     * testCheckedDefaultCommit：对照调用路径与数据库实际状态。
     * <p>期望结果：AssignmentCheckedException；默认不回滚受检异常，数据库 REVIEW-A / 0。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testCheckedDefaultCommit() throws Exception {
        System.out.println("\ntestCheckedDefaultCommit ---> 期望：AssignmentCheckedException；默认不回滚受检异常，数据库 REVIEW-A / 0");
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                JdkTransactionConfig.class)) {
            DemoDatabase database = context.getBean(DemoDatabase.class);
            AssignmentService service = context.getBean(AssignmentService.class);
            try {
                service.assignChecked("DEMO-001", "REVIEW-A");
                System.out.println("实际结果：未抛出异常，请对照期望检查");
            } catch (Exception exception) {
                System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
                System.out.println("实际异常信息：" + exception.getMessage());
            }
            System.out.println("实际数据库：" + database.snapshot("DEMO-001"));
            System.out.println("Spring 事务活动：" + TransactionSynchronizationManager.isActualTransactionActive());
            System.out.println("线程仍绑定数据源：" + TransactionSynchronizationManager.hasResource(database.getDataSource()));
        }
    }

    /**
     * testCheckedExplicitRollback：对照调用路径与数据库实际状态。
     * <p>期望结果：AssignmentCheckedException；rollbackFor 生效，数据库 UNASSIGNED / 0。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testCheckedExplicitRollback() throws Exception {
        System.out.println("\ntestCheckedExplicitRollback ---> 期望：AssignmentCheckedException；"
                + "rollbackFor 生效，数据库 UNASSIGNED / 0");
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                JdkTransactionConfig.class)) {
            DemoDatabase database = context.getBean(DemoDatabase.class);
            AssignmentService service = context.getBean(AssignmentService.class);
            try {
                service.assignCheckedRollback("DEMO-001", "REVIEW-A");
                System.out.println("实际结果：未抛出异常，请对照期望检查");
            } catch (Exception exception) {
                System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
                System.out.println("实际异常信息：" + exception.getMessage());
            }
            System.out.println("实际数据库：" + database.snapshot("DEMO-001"));
            System.out.println("Spring 事务活动：" + TransactionSynchronizationManager.isActualTransactionActive());
            System.out.println("线程仍绑定数据源：" + TransactionSynchronizationManager.hasResource(database.getDataSource()));
        }
    }

    /**
     * testSelfInvocation：对照调用路径与数据库实际状态。
     * <p>期望结果：未注解入口 this 调用绕过代理；IllegalStateException，数据库 REVIEW-A / 0。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testSelfInvocation() throws Exception {
        System.out.println("\ntestSelfInvocation ---> 期望：未注解入口 this 调用绕过代理；IllegalStateException，数据库 REVIEW-A / 0");
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                JdkTransactionConfig.class)) {
            DemoDatabase database = context.getBean(DemoDatabase.class);
            AssignmentService service = context.getBean(AssignmentService.class);
            try {
                service.assignViaSelf("DEMO-001", "REVIEW-A");
                System.out.println("实际结果：未抛出异常，请对照期望检查");
            } catch (Exception exception) {
                System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
                System.out.println("实际异常信息：" + exception.getMessage());
            }
            System.out.println("实际数据库：" + database.snapshot("DEMO-001"));
            System.out.println("Spring 事务活动：" + TransactionSynchronizationManager.isActualTransactionActive());
            System.out.println("线程仍绑定数据源：" + TransactionSynchronizationManager.hasResource(database.getDataSource()));
        }
    }

    /**
     * testRequiredRollbackOnly：对照调用路径与数据库实际状态。
     * <p>期望结果：UnexpectedRollbackException；外层虽捕获内层失败仍不能提交，数据库 UNASSIGNED / 0。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testRequiredRollbackOnly() throws Exception {
        System.out.println("\ntestRequiredRollbackOnly ---> 期望：UnexpectedRollbackException；"
                + "外层虽捕获内层失败仍不能提交，数据库 UNASSIGNED / 0");
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                JdkTransactionConfig.class)) {
            DemoDatabase database = context.getBean(DemoDatabase.class);
            AssignmentService service = context.getBean(AssignmentService.class);
            try {
                service.assignCatchingInnerFailure("DEMO-001", "REVIEW-A");
                System.out.println("实际结果：未抛出异常，请对照期望检查");
            } catch (Exception exception) {
                System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
                System.out.println("实际异常信息：" + exception.getMessage());
            }
            System.out.println("实际数据库：" + database.snapshot("DEMO-001"));
            System.out.println("Spring 事务活动：" + TransactionSynchronizationManager.isActualTransactionActive());
            System.out.println("线程仍绑定数据源：" + TransactionSynchronizationManager.hasResource(database.getDataSource()));
        }
    }

    /**
     * testDatabaseConstraint：对照调用路径与数据库实际状态。
     * <p>期望结果：DuplicateKeyException；第二次分派回滚，原记录 REVIEW-A / 1 保留。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testDatabaseConstraint() throws Exception {
        System.out.println("\ntestDatabaseConstraint ---> 期望：DuplicateKeyException；第二次分派回滚，原记录 REVIEW-A / 1 保留");
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                JdkTransactionConfig.class)) {
            DemoDatabase database = context.getBean(DemoDatabase.class);
            AssignmentService service = context.getBean(AssignmentService.class);
            service.assign("DEMO-001", "REVIEW-A", false);
            try {
                service.assign("DEMO-001", "REVIEW-B", false);
                System.out.println("实际结果：未抛出异常，请对照期望检查");
            } catch (Exception exception) {
                System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
                System.out.println("实际异常信息：" + exception.getMessage());
            }
            System.out.println("实际数据库：" + database.snapshot("DEMO-001"));
            System.out.println("Spring 事务活动：" + TransactionSynchronizationManager.isActualTransactionActive());
            System.out.println("线程仍绑定数据源：" + TransactionSynchronizationManager.hasResource(database.getDataSource()));
        }
    }

    /**
     * testFailureThenSuccess：对照调用路径与数据库实际状态。
     * <p>期望结果：同线程失败后可正常开启下一笔；第一笔 UNASSIGNED / 0，第二笔 REVIEW-B / 1。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testFailureThenSuccess() throws Exception {
        System.out.println("\ntestFailureThenSuccess ---> 期望：同线程失败后可正常开启下一笔；第一笔 UNASSIGNED / 0，第二笔 REVIEW-B / 1");
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                JdkTransactionConfig.class)) {
            DemoDatabase database = context.getBean(DemoDatabase.class);
            AssignmentService service = context.getBean(AssignmentService.class);
            try {
                service.assign("DEMO-001", "REVIEW-A", true);
                System.out.println("实际结果：未抛出异常，请对照期望检查");
            } catch (Exception exception) {
                System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
                System.out.println("实际异常信息：" + exception.getMessage());
            }
            service.assign("DEMO-002", "REVIEW-B", false);
            System.out.println("第一笔：" + database.snapshot("DEMO-001"));
            System.out.println("第二笔：" + database.snapshot("DEMO-002"));
            System.out.println("Spring 事务活动：" + TransactionSynchronizationManager.isActualTransactionActive());
            System.out.println("线程仍绑定数据源：" + TransactionSynchronizationManager.hasResource(database.getDataSource()));
        }
    }

    /**
     * testBlankInput：对照调用路径与数据库实际状态。
     * <p>期望结果：IllegalArgumentException；原案件仍为 UNASSIGNED / 0。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testBlankInput() throws Exception {
        System.out.println("\ntestBlankInput ---> 期望：IllegalArgumentException；原案件仍为 UNASSIGNED / 0");
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                JdkTransactionConfig.class)) {
            DemoDatabase database = context.getBean(DemoDatabase.class);
            AssignmentService service = context.getBean(AssignmentService.class);
            try {
                service.assign("DEMO-001", " ", false);
                System.out.println("实际结果：未抛出异常，请对照期望检查");
            } catch (Exception exception) {
                System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
                System.out.println("实际异常信息：" + exception.getMessage());
            }
            System.out.println("实际数据库：" + database.snapshot("DEMO-001"));
            System.out.println("Spring 事务活动：" + TransactionSynchronizationManager.isActualTransactionActive());
            System.out.println("线程仍绑定数据源：" + TransactionSynchronizationManager.hasResource(database.getDataSource()));
        }
    }

    /**
     * testMissingCase：对照调用路径与数据库实际状态。
     * <p>期望结果：IllegalArgumentException；原案件仍为 UNASSIGNED / 0。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testMissingCase() throws Exception {
        System.out.println("\ntestMissingCase ---> 期望：IllegalArgumentException；原案件仍为 UNASSIGNED / 0");
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                JdkTransactionConfig.class)) {
            DemoDatabase database = context.getBean(DemoDatabase.class);
            AssignmentService service = context.getBean(AssignmentService.class);
            try {
                service.assign("MISSING", "REVIEW-A", false);
                System.out.println("实际结果：未抛出异常，请对照期望检查");
            } catch (Exception exception) {
                System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
                System.out.println("实际异常信息：" + exception.getMessage());
            }
            System.out.println("实际数据库：" + database.snapshot("DEMO-001"));
            System.out.println("Spring 事务活动：" + TransactionSynchronizationManager.isActualTransactionActive());
            System.out.println("线程仍绑定数据源：" + TransactionSynchronizationManager.hasResource(database.getDataSource()));
        }
    }

    /**
     * testFinalMethod：对照调用路径与数据库实际状态。
     * <p>期望结果：final 方法内事务活动为 true，JDK 接口代理不依赖覆盖目标方法。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testFinalMethod() throws Exception {
        System.out.println("\ntestFinalMethod ---> 期望：final 方法内事务活动为 true，JDK 接口代理不依赖覆盖目标方法");
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                JdkTransactionConfig.class)) {
            DemoDatabase database = context.getBean(DemoDatabase.class);
            AssignmentService service = context.getBean(AssignmentService.class);
            System.out.println("final 方法内事务活动：" + service.finalTransactionActive());
            System.out.println("Spring 事务活动：" + TransactionSynchronizationManager.isActualTransactionActive());
            System.out.println("线程仍绑定数据源：" + TransactionSynchronizationManager.hasResource(database.getDataSource()));
        }
    }

    /**
     * testRawTargetBypass：对照调用路径与数据库实际状态。
     * <p>期望结果：直接调用 Advised 中的原始目标绕过事务代理；IllegalStateException，数据库 REVIEW-A / 0。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testRawTargetBypass() throws Exception {
        System.out.println("\ntestRawTargetBypass ---> 期望：直接调用 Advised 中的原始目标绕过事务代理；"
                + "IllegalStateException，数据库 REVIEW-A / 0");
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                JdkTransactionConfig.class)) {
            DemoDatabase database = context.getBean(DemoDatabase.class);
            AssignmentService service = context.getBean(AssignmentService.class);
            AssignmentService target = (AssignmentService) ((Advised) service).getTargetSource().getTarget();
            try {
                target.assign("DEMO-001", "REVIEW-A", true);
                System.out.println("实际结果：未抛出异常，请对照期望检查");
            } catch (Exception exception) {
                System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
                System.out.println("实际异常信息：" + exception.getMessage());
            }
            System.out.println("实际数据库：" + database.snapshot("DEMO-001"));
            System.out.println("Spring 事务活动：" + TransactionSynchronizationManager.isActualTransactionActive());
            System.out.println("线程仍绑定数据源：" + TransactionSynchronizationManager.hasResource(database.getDataSource()));
        }
    }
}
