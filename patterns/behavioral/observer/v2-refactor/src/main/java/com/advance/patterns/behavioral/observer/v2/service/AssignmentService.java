package com.advance.patterns.behavioral.observer.v2.service;

import com.advance.patterns.behavioral.observer.v2.event.CaseAssignedEvent;
import com.advance.patterns.behavioral.observer.v2.repository.AssignmentStore;
import com.advance.patterns.behavioral.observer.v2.notification.AssignmentNotifier;

/**
 * 将具体通知集中到通知组件，业务职责变清晰，但通知组件仍依赖具体接收者。
 *
 * @author advance
 * @since 2026-10-28 20:28:32
 */
public class AssignmentService {

    /** 不具备事务的教学内存存储。 */
    private final AssignmentStore store;

    /** 固定通知组件。 */
    private final AssignmentNotifier notifier;

    /**
     * 构造案件分派服务。
     *
     * @param store 教学内存存储
     * @param notifier 固定通知组件
     */
    public AssignmentService(AssignmentStore store, AssignmentNotifier notifier) {
        this.store = store;
        this.notifier = notifier;
    }

    /**
     * 先保存分派，再同步通知；通知异常不会自动回滚内存记录。
     *
     * @param caseId 虚构案件编号
     * @param collectorId 模拟经办人编号
     */
    public void assign(String caseId, String collectorId) {
        System.out.println("[观察者/v2] AssignmentService.assign ---> 开始分派，线程="
                + Thread.currentThread().getName());
        store.save(caseId, collectorId);
        CaseAssignedEvent event = new CaseAssignedEvent(caseId, collectorId);
        notifier.notifyAssigned(event);
        System.out.println("[观察者/v2] AssignmentService.assign ---> 通知完成，分派方法返回");
    }
}
