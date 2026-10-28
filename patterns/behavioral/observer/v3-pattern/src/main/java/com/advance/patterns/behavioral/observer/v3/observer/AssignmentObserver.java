package com.advance.patterns.behavioral.observer.v3.observer;

import com.advance.patterns.behavioral.observer.v3.event.CaseAssignedEvent;

/**
 * 统一观察者契约，发布者只依赖该接口，不了解每个接收者的业务职责。
 *
 * @author advance
 * @since 2026-10-28 20:28:32
 */
public interface AssignmentObserver {

    /**
     * 接收同一分派事件，完成当前观察者自己的操作。
     *
     * @param event 已发生的案件分派事实
     */
    void onAssigned(CaseAssignedEvent event);
}
