package com.advance.patterns.behavioral.observer.v4.config;

import com.advance.patterns.behavioral.observer.v4.observer.WorkbenchObserver;
import com.advance.patterns.behavioral.observer.v4.observer.AuditObserver;
import com.advance.patterns.behavioral.observer.v4.repository.AssignmentStore;
import com.advance.patterns.behavioral.observer.v4.service.AssignmentService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 注册 Spring 事件组件，使用默认同步广播器，不配置线程池、异步监听或事务监听。
 *
 * @author advance
 * @since 2026-10-28 20:28:32
 */
@Configuration
public class ObserverConfig {

    /**
     * 创建当前上下文独立的教学存储。
     *
     * @return 内存存储
     */
    @Bean
    public AssignmentStore assignmentStore() {
        return new AssignmentStore();
    }

    /**
     * 注册带有 EventListener 方法的工作台观察者。
     *
     * @return 模拟工作台观察者
     */
    @Bean
    public WorkbenchObserver workbenchObserver() {
        return new WorkbenchObserver();
    }

    /**
     * 注册带有 EventListener 方法的业务日志观察者。
     *
     * @return 模拟业务日志观察者
     */
    @Bean
    public AuditObserver auditObserver() {
        return new AuditObserver();
    }

    /**
     * 由 Spring 注入事件发布接口，不向服务注入任何具体观察者。
     *
     * @param store 教学存储
     * @param publisher 容器提供的事件发布接口
     * @return 案件分派服务
     */
    @Bean
    public AssignmentService assignmentService(AssignmentStore store, ApplicationEventPublisher publisher) {
        return new AssignmentService(store, publisher);
    }
}
