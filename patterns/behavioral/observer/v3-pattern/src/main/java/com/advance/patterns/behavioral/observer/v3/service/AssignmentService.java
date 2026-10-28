package com.advance.patterns.behavioral.observer.v3.service;

import com.advance.patterns.behavioral.observer.v3.event.CaseAssignedEvent;
import com.advance.patterns.behavioral.observer.v3.repository.AssignmentStore;
import com.advance.patterns.behavioral.observer.v3.event.AssignmentEventPublisher;

/**
 * 分派服务只更新内存状态并发布事实，不知道订阅者数量和具体实现。
 *
 * @author advance
 * @since 2026-10-28 20:28:32
 */
public class AssignmentService {

    /** 不具备事务的教学内存存储。 */
    private final AssignmentStore store;

    /** 统一事件发布者。 */
    private final AssignmentEventPublisher publisher;

    /**
     * 构造案件分派服务。
     *
     * @param store 教学内存存储
     * @param publisher 统一事件发布者
     */
    public AssignmentService(AssignmentStore store, AssignmentEventPublisher publisher) {
        this.store = store;
        this.publisher = publisher;
    }

    /**
     * 先保存分派，再同步通知；通知异常不会自动回滚内存记录。
     *
     * @param caseId 虚构案件编号
     * @param collectorId 模拟经办人编号
     */
    public void assign(String caseId, String collectorId) {
        System.out.println("[观察者/v3] AssignmentService.assign ---> 开始分派，线程="
                + Thread.currentThread().getName());
        store.save(caseId, collectorId);
        CaseAssignedEvent event = new CaseAssignedEvent(caseId, collectorId);
        publisher.publish(event);
        System.out.println("[观察者/v3] AssignmentService.assign ---> 通知完成，分派方法返回");
    }
}
