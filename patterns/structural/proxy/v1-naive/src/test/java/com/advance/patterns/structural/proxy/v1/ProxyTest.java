package com.advance.patterns.structural.proxy.v1;

import com.advance.patterns.structural.proxy.v1.database.DemoDatabase;
import com.advance.patterns.structural.proxy.v1.jdbc.ConnectionContext;
import com.advance.patterns.structural.proxy.v1.repository.CaseRepository;
import com.advance.patterns.structural.proxy.v1.service.AssignmentService;
import com.advance.patterns.structural.proxy.v1.service.CaseAssignmentService;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
            AssignmentService service = target;
            service.assign("DEMO-001", "REVIEW-A", false);
            System.out.println("实际数据库：" + database.snapshot("DEMO-001"));
            System.out.println("事务连接已清理：" + (connections.get() == null));
        }
    }

    /**
     * testFailure：对照调用路径与数据库实际状态。
     * <p>期望结果：IllegalStateException；实际数据库 assignee=REVIEW-A, records=0，说明无事务留下部分更新。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testFailure() throws Exception {
        System.out.println("\ntestFailure ---> 期望：IllegalStateException；"
                + "实际数据库 assignee=REVIEW-A, records=0，说明无事务留下部分更新");
        try (DemoDatabase database = new DemoDatabase()) {
            ConnectionContext connections = new ConnectionContext();
            CaseRepository repository = new CaseRepository(database.getDataSource(), connections);
            AssignmentService target = new CaseAssignmentService(repository);
            AssignmentService service = target;
            try {
                service.assign("DEMO-001", "REVIEW-A", true);
                System.out.println("实际结果：未抛出异常，请对照期望检查");
            } catch (Exception exception) {
                System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
                System.out.println("实际异常信息：" + exception.getMessage());
            }
            System.out.println("实际数据库：" + database.snapshot("DEMO-001"));
            System.out.println("事务连接已清理：" + (connections.get() == null));
        }
    }

    /**
     * testDatabaseConstraint：对照调用路径与数据库实际状态。
     * <p>期望结果：第二次插入触发数据库主键约束；assignee=REVIEW-B, records=1。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testDatabaseConstraint() throws Exception {
        System.out.println("\ntestDatabaseConstraint ---> 期望：第二次插入触发数据库主键约束；assignee=REVIEW-B, records=1");
        try (DemoDatabase database = new DemoDatabase()) {
            ConnectionContext connections = new ConnectionContext();
            CaseRepository repository = new CaseRepository(database.getDataSource(), connections);
            AssignmentService target = new CaseAssignmentService(repository);
            AssignmentService service = target;
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
            AssignmentService service = target;
            try {
                service.assign(" ", "REVIEW-A", false);
                System.out.println("实际结果：未抛出异常，请对照期望检查");
            } catch (Exception exception) {
                System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
                System.out.println("实际异常信息：" + exception.getMessage());
            }
            System.out.println("实际数据库：" + database.snapshot("DEMO-001"));
            System.out.println("事务连接已清理：" + (connections.get() == null));
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
            AssignmentService service = target;
            try {
                service.assign("MISSING", "REVIEW-A", false);
                System.out.println("实际结果：未抛出异常，请对照期望检查");
            } catch (Exception exception) {
                System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
                System.out.println("实际异常信息：" + exception.getMessage());
            }
            System.out.println("实际数据库：" + database.snapshot("DEMO-001"));
            System.out.println("事务连接已清理：" + (connections.get() == null));
        }
    }

    /**
     * testReflectionDoesNotAddTransaction：对照调用路径与数据库实际状态。
     * <p>期望结果：Class 获取 Method 后反射调用；外层 InvocationTargetException，原因为 IllegalStateException；
     * 数据库 REVIEW-A / 0，反射本身不提供事务。</p>
     * @throws Exception 数据库或反射操作异常
     */
    @Test
    public void testReflectionDoesNotAddTransaction() throws Exception {
        System.out.println("\ntestReflectionDoesNotAddTransaction ---> 期望：Class 获取 Method 后反射调用；"
                + "外层 InvocationTargetException，原因为 IllegalStateException；数据库 REVIEW-A / 0，反射本身不提供事务");
        try (DemoDatabase database = new DemoDatabase()) {
            ConnectionContext connections = new ConnectionContext();
            AssignmentService target = new CaseAssignmentService(new CaseRepository(database.getDataSource(),
                    connections));
            Class<?> contract = AssignmentService.class;
            Method reflected = contract.getMethod("assign", String.class, String.class, boolean.class);
            try {
                reflected.invoke(target, "DEMO-001", "REVIEW-A", true);
                System.out.println("实际结果：未抛出异常，请对照期望检查");
            } catch (InvocationTargetException exception) {
                System.out.println("反射包装类型：" + exception.getClass().getSimpleName());
                System.out.println("目标异常类型：" + exception.getCause().getClass().getSimpleName());
            }
            System.out.println("实际数据库：" + database.snapshot("DEMO-001"));
        }
    }
}
