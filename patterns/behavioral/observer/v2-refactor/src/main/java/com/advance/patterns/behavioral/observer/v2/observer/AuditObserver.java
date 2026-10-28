package com.advance.patterns.behavioral.observer.v2.observer;

import com.advance.patterns.behavioral.observer.v2.event.CaseAssignedEvent;

/**
 * 接收分派事实后记录模拟业务日志；只打印并计数，不访问外部系统。
 *
 * @author advance
 * @since 2026-10-28 20:28:32
 */
public class AuditObserver {

    /** 本实例收到通知的次数，仅供单线程教学观察。 */
    private int notificationCount;

    /**
     * 接收分派快照并执行自身职责，不负责调用其他观察者。
     *
     * @param event 本次案件分派的不可变快照
     */
    public void onAssigned(CaseAssignedEvent event) {
        notificationCount++;
        System.out.println("[观察者/v2] AuditObserver.onAssigned ---> 记录模拟业务日志，caseId="
                + event.getCaseId() + "，collectorId=" + event.getCollectorId()
                + "，线程=" + Thread.currentThread().getName());
    }

    /**
     * 获取已接收次数，供测试直接打印。
     *
     * @return 本实例接收通知的次数
     */
    public int getNotificationCount() {
        return notificationCount;
    }
}
