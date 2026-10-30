package com.advance.patterns.comprehensive.material.v1;

/** 对调用方隐藏任务创建与 Outbox 发布步骤的统一入口。 @author advance @since 2026-10-30 12:00:00 */
public class MaterialFacade {
    private final MaterialTaskService taskService;
    private final OutboxPublisher outboxPublisher;
    /** @param taskService 任务服务 @param outboxPublisher 发件箱发布器 */
    public MaterialFacade(MaterialTaskService taskService, OutboxPublisher outboxPublisher) {
        this.taskService = taskService; this.outboxPublisher = outboxPublisher;
    }
    /** @param request 归集请求 @return 新建或已存在任务标识 */
    public String submit(MaterialRequest request) {
        System.out.println("[最终架构/Facade] MaterialFacade.submit ---> 接收材料归集请求");
        MaterialTask task = taskService.createIfAbsent(request);
        outboxPublisher.publishPending();
        return task.getTaskId();
    }
}
