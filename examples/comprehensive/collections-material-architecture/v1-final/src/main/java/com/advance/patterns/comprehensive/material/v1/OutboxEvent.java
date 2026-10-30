package com.advance.patterns.comprehensive.material.v1;

/** Outbox 发布记录；它是事件数据，不是可执行的 GoF Command。 @author advance @since 2026-10-30 12:00:00 */
public class OutboxEvent {
    private final String eventId;
    private final String taskId;
    private boolean published;
    /** @param eventId 事件标识 @param taskId 关联任务 */
    public OutboxEvent(String eventId, String taskId) { this.eventId = eventId; this.taskId = taskId; }
    /** @return 事件标识 */ public String getEventId() { return eventId; }
    /** @return 任务标识 */ public String getTaskId() { return taskId; }
    /** @return 发布状态 */ public boolean isPublished() { return published; }
    /** 标记已经投递。 */ public void markPublished() { published = true; }
}
