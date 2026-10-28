package com.advance.patterns.behavioral.observer.v1.service;

import com.advance.patterns.behavioral.observer.v1.event.CaseAssignedEvent;
import com.advance.patterns.behavioral.observer.v1.repository.AssignmentStore;
import com.advance.patterns.behavioral.observer.v1.observer.WorkbenchObserver;
import com.advance.patterns.behavioral.observer.v1.observer.AuditObserver;

/**
 * 分派服务直接调用两个具体接收者，新增后续动作需要修改主业务。
 *
 * @author advance
 * @since 2026-10-28 20:28:32
 */
public class AssignmentService {

    /** 不具备事务的教学内存存储。 */
    private final AssignmentStore store;

    /** 工作台接收者。 */
    private final WorkbenchObserver workbench;

    /** 业务日志接收者。 */
    private final AuditObserver audit;

    /**
     * 构造案件分派服务。
     *
     * @param store 教学内存存储
     * @param workbench 工作台接收者
     * @param audit 业务日志接收者
     */
    public AssignmentService(AssignmentStore store, WorkbenchObserver workbench, AuditObserver audit) {
        this.store = store;
        this.workbench = workbench;
        this.audit = audit;
    }

    /**
     * 先保存分派，再同步通知；通知异常不会自动回滚内存记录。
     *
     * @param caseId 虚构案件编号
     * @param collectorId 模拟经办人编号
     */
    public void assign(String caseId, String collectorId) {
        System.out.println("[观察者/v1] AssignmentService.assign ---> 开始分派，线程="
                + Thread.currentThread().getName());
        store.save(caseId, collectorId);
        CaseAssignedEvent event = new CaseAssignedEvent(caseId, collectorId);
        workbench.onAssigned(event);
        audit.onAssigned(event);
        System.out.println("[观察者/v1] AssignmentService.assign ---> 通知完成，分派方法返回");
    }
}
