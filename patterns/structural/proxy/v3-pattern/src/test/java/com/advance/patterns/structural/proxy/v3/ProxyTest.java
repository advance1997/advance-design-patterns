package com.advance.patterns.structural.proxy.v3;

import com.advance.patterns.structural.proxy.v3.database.DemoDatabase;
import com.advance.patterns.structural.proxy.v3.jdbc.ConnectionContext;
import com.advance.patterns.structural.proxy.v3.repository.CaseRepository;
import com.advance.patterns.structural.proxy.v3.service.AssignmentService;
import com.advance.patterns.structural.proxy.v3.service.CaseAssignmentService;
import com.advance.patterns.structural.proxy.v3.tx.LocalTransactionManager;
import com.advance.patterns.structural.proxy.v3.proxy.JdkTransactionProxy;
import java.lang.reflect.Proxy;
import org.junit.jupiter.api.Test;

/**
 * 读取 H2 已提交状态的打印式测试，不使用断言，不用日志代替真实事务结果。
 *
 * @author advance
 * @since 2026-10-28 10:15:29
 */
public class ProxyTest {
    /**
     * testSuccess：对照调用路径与数据库实际状态。
     * <p>期望结果：独立连接读到 assignee=REVIEW-A, records=1；结束后无事务连接。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testSuccess() throws Exception {
        System.out.println("\ntestSuccess ---> 期望：独立连接读到 assignee=REVIEW-A, records=1；结束后无事务连接");
        try (DemoDatabase database = new DemoDatabase()) {
            ConnectionContext connections = new ConnectionContext();
            CaseRepository repository = new CaseRepository(database.getDataSource(), connections);
            AssignmentService target = new CaseAssignmentService(repository);
            LocalTransactionManager manager = new LocalTransactionManager(database.getDataSource(), connections);
            AssignmentService service = JdkTransactionProxy.create(target, manager);
            service.assign("DEMO-001", "REVIEW-A", false);
            System.out.println("实际数据库：" + database.snapshot("DEMO-001"));
            System.out.println("事务连接已清理：" + (connections.get() == null));
            System.out.println("管理器事务活动：" + manager.isActive());
        }
    }

    /**
     * testFailure：对照调用路径与数据库实际状态。
     * <p>期望结果：IllegalStateException；实际数据库 assignee=UNASSIGNED, records=0，说明回滚有效。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testFailure() throws Exception {
        System.out.println("\ntestFailure ---> 期望：IllegalStateException；实际数据库 assignee=UNASSIGNED, records=0，说明回滚有效");
        try (DemoDatabase database = new DemoDatabase()) {
            ConnectionContext connections = new ConnectionContext();
            CaseRepository repository = new CaseRepository(database.getDataSource(), connections);
            AssignmentService target = new CaseAssignmentService(repository);
            LocalTransactionManager manager = new LocalTransactionManager(database.getDataSource(), connections);
            AssignmentService service = JdkTransactionProxy.create(target, manager);
            try {
                service.assign("DEMO-001", "REVIEW-A", true);
                System.out.println("实际结果：未抛出异常，请对照期望检查");
            } catch (Exception exception) {
                System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
                System.out.println("实际异常信息：" + exception.getMessage());
            }
            System.out.println("实际数据库：" + database.snapshot("DEMO-001"));
            System.out.println("事务连接已清理：" + (connections.get() == null));
            System.out.println("管理器事务活动：" + manager.isActive());
        }
    }

    /**
     * testDatabaseConstraint：对照调用路径与数据库实际状态。
     * <p>期望结果：第二次插入触发数据库主键约束；assignee=REVIEW-A, records=1。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testDatabaseConstraint() throws Exception {
        System.out.println("\ntestDatabaseConstraint ---> 期望：第二次插入触发数据库主键约束；assignee=REVIEW-A, records=1");
        try (DemoDatabase database = new DemoDatabase()) {
            ConnectionContext connections = new ConnectionContext();
            CaseRepository repository = new CaseRepository(database.getDataSource(), connections);
            AssignmentService target = new CaseAssignmentService(repository);
            LocalTransactionManager manager = new LocalTransactionManager(database.getDataSource(), connections);
            AssignmentService service = JdkTransactionProxy.create(target, manager);
            service.assign("DEMO-001", "REVIEW-A", false);
            try {
                service.assign("DEMO-001", "REVIEW-B", false);
                System.out.println("实际结果：未抛出异常，请对照期望检查");
            } catch (Exception exception) {
                System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
                System.out.println("实际异常信息：" + exception.getMessage());
            }
            System.out.println("实际数据库：" + database.snapshot("DEMO-001"));
            System.out.println("事务连接已清理：" + (connections.get() == null));
            System.out.println("管理器事务活动：" + manager.isActive());
        }
    }

    /**
     * testBlankInput：对照调用路径与数据库实际状态。
     * <p>期望结果：IllegalArgumentException；数据库 UNASSIGNED / 0，无写入。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testBlankInput() throws Exception {
        System.out.println("\ntestBlankInput ---> 期望：IllegalArgumentException；数据库 UNASSIGNED / 0，无写入");
        try (DemoDatabase database = new DemoDatabase()) {
            ConnectionContext connections = new ConnectionContext();
            CaseRepository repository = new CaseRepository(database.getDataSource(), connections);
            AssignmentService target = new CaseAssignmentService(repository);
            LocalTransactionManager manager = new LocalTransactionManager(database.getDataSource(), connections);
            AssignmentService service = JdkTransactionProxy.create(target, manager);
            try {
                service.assign(" ", "REVIEW-A", false);
                System.out.println("实际结果：未抛出异常，请对照期望检查");
            } catch (Exception exception) {
                System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
                System.out.println("实际异常信息：" + exception.getMessage());
            }
            System.out.println("实际数据库：" + database.snapshot("DEMO-001"));
            System.out.println("事务连接已清理：" + (connections.get() == null));
            System.out.println("管理器事务活动：" + manager.isActive());
        }
    }

    /**
     * testMissingCase：对照调用路径与数据库实际状态。
     * <p>期望结果：IllegalArgumentException：案件不存在；原案件仍 UNASSIGNED / 0。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testMissingCase() throws Exception {
        System.out.println("\ntestMissingCase ---> 期望：IllegalArgumentException：案件不存在；原案件仍 UNASSIGNED / 0");
        try (DemoDatabase database = new DemoDatabase()) {
            ConnectionContext connections = new ConnectionContext();
            CaseRepository repository = new CaseRepository(database.getDataSource(), connections);
            AssignmentService target = new CaseAssignmentService(repository);
            LocalTransactionManager manager = new LocalTransactionManager(database.getDataSource(), connections);
            AssignmentService service = JdkTransactionProxy.create(target, manager);
            try {
                service.assign("MISSING", "REVIEW-A", false);
                System.out.println("实际结果：未抛出异常，请对照期望检查");
            } catch (Exception exception) {
                System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
                System.out.println("实际异常信息：" + exception.getMessage());
            }
            System.out.println("实际数据库：" + database.snapshot("DEMO-001"));
            System.out.println("事务连接已清理：" + (connections.get() == null));
            System.out.println("管理器事务活动：" + manager.isActive());
        }
    }

    /**
     * testFailureThenSuccess：对照调用路径与数据库实际状态。
     * <p>期望结果：同线程第一次失败回滚，第二次正常提交；DEMO-001 为 UNASSIGNED / 0，DEMO-002 为 REVIEW-B / 1。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testFailureThenSuccess() throws Exception {
        System.out.println("\ntestFailureThenSuccess ---> 期望：同线程第一次失败回滚，第二次正常提交；"
                + "DEMO-001 为 UNASSIGNED / 0，DEMO-002 为 REVIEW-B / 1");
        try (DemoDatabase database = new DemoDatabase()) {
            ConnectionContext connections = new ConnectionContext();
            CaseRepository repository = new CaseRepository(database.getDataSource(), connections);
            AssignmentService target = new CaseAssignmentService(repository);
            LocalTransactionManager manager = new LocalTransactionManager(database.getDataSource(), connections);
            AssignmentService service = JdkTransactionProxy.create(target, manager);
            try {
                service.assign("DEMO-001", "REVIEW-A", true);
                System.out.println("实际结果：未抛出异常，请对照期望检查");
            } catch (Exception exception) {
                System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
                System.out.println("实际异常信息：" + exception.getMessage());
            }
            System.out.println("失败后事务活动：" + manager.isActive());
            service.assign("DEMO-002", "REVIEW-B", false);
            System.out.println("第一笔：" + database.snapshot("DEMO-001"));
            System.out.println("第二笔：" + database.snapshot("DEMO-002"));
            System.out.println("事务连接已清理：" + (connections.get() == null));
            System.out.println("管理器事务活动：" + manager.isActive());
        }
    }

    /**
     * testProxyType：对照调用路径与数据库实际状态。
     * <p>期望结果：代理不是目标对象；JDK 生成代理为 true，代理 instanceof CaseAssignmentService 为 false。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testProxyType() throws Exception {
        System.out.println("\ntestProxyType ---> 期望：代理不是目标对象；"
                + "JDK 生成代理为 true，代理 instanceof CaseAssignmentService 为 false");
        try (DemoDatabase database = new DemoDatabase()) {
            ConnectionContext connections = new ConnectionContext();
            CaseRepository repository = new CaseRepository(database.getDataSource(), connections);
            AssignmentService target = new CaseAssignmentService(repository);
            LocalTransactionManager manager = new LocalTransactionManager(database.getDataSource(), connections);
            AssignmentService service = JdkTransactionProxy.create(target, manager);
            System.out.println("代理实现类：" + service.getClass().getName());
            System.out.println("代理与目标相同：" + (service == target));
            System.out.println("JDK 生成代理：" + Proxy.isProxyClass(service.getClass()));
            System.out.println("代理属于目标类：" + (service instanceof CaseAssignmentService));
            System.out.println("事务连接已清理：" + (connections.get() == null));
            System.out.println("管理器事务活动：" + manager.isActive());
        }
    }

    /**
     * testObjectMethods：对照调用路径与数据库实际状态。
     * <p>期望结果：代理 equals 自身为 true、equals 目标为 false；toString/hashCode 不开启事务。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testObjectMethods() throws Exception {
        System.out.println("\ntestObjectMethods ---> 期望：代理 equals 自身为 true、equals 目标为 false；toString/hashCode 不开启事务");
        try (DemoDatabase database = new DemoDatabase()) {
            ConnectionContext connections = new ConnectionContext();
            CaseRepository repository = new CaseRepository(database.getDataSource(), connections);
            AssignmentService target = new CaseAssignmentService(repository);
            LocalTransactionManager manager = new LocalTransactionManager(database.getDataSource(), connections);
            AssignmentService service = JdkTransactionProxy.create(target, manager);
            System.out.println("equals 自身：" + service.equals(service));
            System.out.println("equals 目标：" + service.equals(target));
            System.out.println("代理摘要：" + service.toString());
            System.out.println("身份哈希一致：" + (service.hashCode() == System.identityHashCode(service)));
            System.out.println("事务连接已清理：" + (connections.get() == null));
            System.out.println("管理器事务活动：" + manager.isActive());
        }
    }
}
