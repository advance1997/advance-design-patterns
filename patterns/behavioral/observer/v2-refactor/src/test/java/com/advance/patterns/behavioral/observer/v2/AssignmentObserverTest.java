package com.advance.patterns.behavioral.observer.v2;

import com.advance.patterns.behavioral.observer.v2.observer.WorkbenchObserver;
import com.advance.patterns.behavioral.observer.v2.observer.AuditObserver;
import com.advance.patterns.behavioral.observer.v2.repository.AssignmentStore;
import com.advance.patterns.behavioral.observer.v2.service.AssignmentService;
import org.junit.jupiter.api.Test;
import com.advance.patterns.behavioral.observer.v2.notification.AssignmentNotifier;

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
        AssignmentService service = new AssignmentService(store, new AssignmentNotifier(workbench, audit));
        service.assign("DEMO-001", "COLLECTOR-A");
        System.out.println("实际经办人：" + store.findCollector("DEMO-001"));
        System.out.println("工作台通知次数：" + workbench.getNotificationCount());
        System.out.println("日志通知次数：" + audit.getNotificationCount());
    }
}
