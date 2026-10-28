package com.advance.patterns.behavioral.observer.v3;

import com.advance.patterns.behavioral.observer.v3.observer.WorkbenchObserver;
import com.advance.patterns.behavioral.observer.v3.observer.AuditObserver;
import com.advance.patterns.behavioral.observer.v3.repository.AssignmentStore;
import com.advance.patterns.behavioral.observer.v3.service.AssignmentService;
import org.junit.jupiter.api.Test;
import com.advance.patterns.behavioral.observer.v3.event.AssignmentEventPublisher;
import com.advance.patterns.behavioral.observer.v3.support.FailingObserver;

/**
 * 仅以直接打印验证通知解耦、订阅变化及同步失败边界，不使用断言框架。
 *
 * @author advance
 * @since 2026-10-28 20:28:32
 */
class AssignmentObserverTest {

    /**
     * 输入 DEMO-001、COLLECTOR-A。
     * 期望：先保存分派，再依次执行 WorkbenchObserver、AuditObserver，最后服务返回。
     * 期望内存经办人为 COLLECTOR-A，工作台通知次数为 1，日志通知次数为 1；通知与调用线程一致。
     */
    @Test
    void notifyTwoRecipients() {
        AssignmentStore store = new AssignmentStore();
        WorkbenchObserver workbench = new WorkbenchObserver();
        AuditObserver audit = new AuditObserver();
        AssignmentEventPublisher publisher = new AssignmentEventPublisher();
        publisher.register(workbench);
        publisher.register(audit);
        AssignmentService service = new AssignmentService(store, publisher);
        service.assign("DEMO-001", "COLLECTOR-A");
        System.out.println("实际经办人：" + store.findCollector("DEMO-001"));
        System.out.println("工作台通知次数：" + workbench.getNotificationCount());
        System.out.println("日志通知次数：" + audit.getNotificationCount());
    }

    /**
     * 先分派 DEMO-001 给 COLLECTOR-A，再取消 AuditObserver，分派 DEMO-002 给 COLLECTOR-B。
     * 期望：第一次两个观察者都收到；第二次只有工作台收到；累计工作台 2 次、日志 1 次。
     * 取消订阅不撤销第一次通知，服务代码和发布方法均无需修改。
     */
    @Test
    void unsubscribeOnlyAffectsLaterEvents() {
        AssignmentStore store = new AssignmentStore();
        WorkbenchObserver workbench = new WorkbenchObserver();
        AuditObserver audit = new AuditObserver();
        AssignmentEventPublisher publisher = new AssignmentEventPublisher();
        publisher.register(workbench);
        publisher.register(audit);
        AssignmentService service = new AssignmentService(store, publisher);
        service.assign("DEMO-001", "COLLECTOR-A");
        publisher.unregister(audit);
        service.assign("DEMO-002", "COLLECTOR-B");
        System.out.println("工作台累计通知次数：" + workbench.getNotificationCount());
        System.out.println("日志累计通知次数：" + audit.getNotificationCount());
    }

    /**
     * 输入 DEMO-001、COLLECTOR-A；在工作台和日志观察者之间加入 FailingObserver。
     * 期望：工作台接收 1 次后抛 IllegalStateException，消息为“模拟观察者处理失败”，日志为 0 次。
     * 分派记录仍为 COLLECTOR-A；已完成通知不会撤销，服务的“通知完成”日志不出现。
     */
    @Test
    void listenerFailureStopsSynchronousNotification() {
        AssignmentStore store = new AssignmentStore();
        WorkbenchObserver workbench = new WorkbenchObserver();
        AuditObserver audit = new AuditObserver();
        AssignmentEventPublisher publisher = new AssignmentEventPublisher();
        publisher.register(workbench);
        publisher.register(new FailingObserver());
        publisher.register(audit);
        AssignmentService service = new AssignmentService(store, publisher);
        try {
            service.assign("DEMO-001", "COLLECTOR-A");
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (IllegalStateException exception) {
            System.out.println("实际异常：" + exception.getMessage());
        }
        System.out.println("实际经办人：" + store.findCollector("DEMO-001"));
        System.out.println("工作台通知次数：" + workbench.getNotificationCount());
        System.out.println("日志通知次数：" + audit.getNotificationCount());
    }
}
