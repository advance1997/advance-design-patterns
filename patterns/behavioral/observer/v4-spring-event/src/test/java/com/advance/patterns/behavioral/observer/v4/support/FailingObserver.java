package com.advance.patterns.behavioral.observer.v4.support;

import com.advance.patterns.behavioral.observer.v4.event.CaseAssignedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

/**
 * 只用于观察同步通知异常传播的具名测试观察者，不进入正常业务装配。
 *
 * @author advance
 * @since 2026-10-28 20:28:32
 */
public class FailingObserver {

    /**
     * 在工作台与日志接收者之间模拟通知失败。
     *
     * @param event 本次分派事件
     * @throws IllegalStateException 固定抛出教学异常以观察同步广播边界
     */
    @EventListener
    @Order(15)
    public void onAssigned(CaseAssignedEvent event) {
        System.out.println("[观察者/v4] FailingObserver.onAssigned ---> 模拟监听器失败，caseId="
                + event.getCaseId());
        throw new IllegalStateException("模拟观察者处理失败");
    }
}
