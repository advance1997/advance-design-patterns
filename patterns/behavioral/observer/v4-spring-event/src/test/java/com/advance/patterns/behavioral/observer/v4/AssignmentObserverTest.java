package com.advance.patterns.behavioral.observer.v4;

import com.advance.patterns.behavioral.observer.v4.observer.WorkbenchObserver;
import com.advance.patterns.behavioral.observer.v4.observer.AuditObserver;
import com.advance.patterns.behavioral.observer.v4.repository.AssignmentStore;
import com.advance.patterns.behavioral.observer.v4.service.AssignmentService;
import org.junit.jupiter.api.Test;
import com.advance.patterns.behavioral.observer.v4.config.ObserverConfig;
import com.advance.patterns.behavioral.observer.v4.support.FailingObserver;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
        try (AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ObserverConfig.class)) {
            AssignmentService service = context.getBean(AssignmentService.class);
            System.out.println("测试调用线程：" + Thread.currentThread().getName());
            service.assign("DEMO-001", "COLLECTOR-A");
            System.out.println("实际经办人：" + context.getBean(AssignmentStore.class).findCollector("DEMO-001"));
            System.out.println("工作台通知次数：" + context.getBean(WorkbenchObserver.class).getNotificationCount());
            System.out.println("日志通知次数：" + context.getBean(AuditObserver.class).getNotificationCount());
        }
    }

    /**
     * 输入 DEMO-001、COLLECTOR-A；在工作台和日志观察者之间加入 FailingObserver。
     * 期望：工作台接收 1 次后抛 IllegalStateException，消息为“模拟观察者处理失败”，日志为 0 次。
     * 分派记录仍为 COLLECTOR-A；已完成通知不会撤销，服务的“通知完成”日志不出现。
     */
    @Test
    void listenerFailureStopsSynchronousNotification() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(ObserverConfig.class, FailingObserver.class);
            context.refresh();
            AssignmentService service = context.getBean(AssignmentService.class);
            try {
                service.assign("DEMO-001", "COLLECTOR-A");
                System.out.println("实际结果：未抛出异常，请对照期望检查");
            } catch (IllegalStateException exception) {
                System.out.println("实际异常：" + exception.getMessage());
            }
            System.out.println("实际经办人：" + context.getBean(AssignmentStore.class).findCollector("DEMO-001"));
            System.out.println("工作台通知次数：" + context.getBean(WorkbenchObserver.class).getNotificationCount());
            System.out.println("日志通知次数：" + context.getBean(AuditObserver.class).getNotificationCount());
        }
    }
}
