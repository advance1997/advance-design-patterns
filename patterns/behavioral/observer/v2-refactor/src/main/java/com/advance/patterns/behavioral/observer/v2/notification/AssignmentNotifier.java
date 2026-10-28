package com.advance.patterns.behavioral.observer.v2.notification;

import com.advance.patterns.behavioral.observer.v2.event.CaseAssignedEvent;
import com.advance.patterns.behavioral.observer.v2.observer.WorkbenchObserver;
import com.advance.patterns.behavioral.observer.v2.observer.AuditObserver;

/**
 * 提取通知职责的过渡组件，仍显式依赖每个具体接收者，尚无统一订阅机制。
 *
 * @author advance
 * @since 2026-10-28 20:28:32
 */
public class AssignmentNotifier {

    /** 工作台通知接收者。 */
    private final WorkbenchObserver workbench;

    /** 业务日志通知接收者。 */
    private final AuditObserver audit;

    /**
     * 固定两个接收者，新增接收者仍需修改此类。
     *
     * @param workbench 工作台处理对象
     * @param audit 业务日志处理对象
     */
    public AssignmentNotifier(WorkbenchObserver workbench, AuditObserver audit) {
        this.workbench = workbench;
        this.audit = audit;
    }

    /**
     * 直接调用两个不同的具体对象。
     *
     * @param event 已完成分派的快照
     */
    public void notifyAssigned(CaseAssignedEvent event) {
        System.out.println("[观察者/v2] AssignmentNotifier.notifyAssigned ---> 依次调用两个固定接收者");
        workbench.onAssigned(event);
        audit.onAssigned(event);
    }
}
