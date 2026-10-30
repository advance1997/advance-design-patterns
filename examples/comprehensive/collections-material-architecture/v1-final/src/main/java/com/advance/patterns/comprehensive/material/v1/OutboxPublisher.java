package com.advance.patterns.comprehensive.material.v1;

/** 将数据库事务中记录的 Outbox 事件转发到模拟 MQ。 @author advance @since 2026-10-30 12:00:00 */
public class OutboxPublisher {
    private final MockMaterialTaskRepository repository;
    private final MockMessageBroker broker;
    /** @param repository 模拟数据库 @param broker 模拟消息代理 */
    public OutboxPublisher(MockMaterialTaskRepository repository, MockMessageBroker broker) {
        this.repository = repository; this.broker = broker;
    }
    /** 发布待处理事件；事件重复投递由消费端认领逻辑兜底。 */
    public void publishPending() {
        for (OutboxEvent event : repository.pendingOutbox()) {
            System.out.println("[最终架构/Outbox] publishPending ---> 发布 " + event.getEventId());
            broker.send(event.getTaskId()); repository.markPublished(event.getEventId());
        }
    }
}
