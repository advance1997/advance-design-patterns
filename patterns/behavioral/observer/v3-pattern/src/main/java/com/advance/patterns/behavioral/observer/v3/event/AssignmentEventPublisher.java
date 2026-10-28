package com.advance.patterns.behavioral.observer.v3.event;

import com.advance.patterns.behavioral.observer.v3.observer.AssignmentObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 单线程同步事件发布者，管理观察者并广播事件；不支持异步、重试或持久化。
 *
 * @author advance
 * @since 2026-10-28 20:28:32
 */
public class AssignmentEventPublisher {

    /** 当前注册的观察者，按注册顺序接收通知。 */
    private final List<AssignmentObserver> observers = new ArrayList<AssignmentObserver>();

    /**
     * 注册观察者；同一对象重复注册不增加第二次订阅。
     *
     * @param observer 具体观察者
     * @throws NullPointerException 观察者为空时抛出
     */
    public void register(AssignmentObserver observer) {
        Objects.requireNonNull(observer, "observer 不能为空");
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * 取消指定观察者后续的通知，不撤销已完成的处理。
     *
     * @param observer 待取消的观察者
     */
    public void unregister(AssignmentObserver observer) {
        observers.remove(observer);
    }

    /**
     * 在调用线程中按注册顺序广播；异常直接向调用者传播并停止后续通知。
     * 使用订阅列表快照，本轮中的订阅变更从下一轮起生效。
     *
     * @param event 同一份不可变分派快照
     */
    public void publish(CaseAssignedEvent event) {
        System.out.println("[观察者/v3] AssignmentEventPublisher.publish ---> 同步广播，线程="
                + Thread.currentThread().getName());
        List<AssignmentObserver> snapshot = new ArrayList<AssignmentObserver>(observers);
        for (AssignmentObserver observer : snapshot) {
            System.out.println("[观察者/v3] AssignmentEventPublisher.publish ---> 通知 "
                    + observer.getClass().getSimpleName());
            observer.onAssigned(event);
        }
        System.out.println("[观察者/v3] AssignmentEventPublisher.publish ---> 所有观察者处理完成");
    }
}
