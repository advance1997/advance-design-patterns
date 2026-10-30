package com.advance.patterns.comprehensive.material.v1;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** 以内存集合模拟任务、明细和 Outbox 的数据库事务存储。 @author advance @since 2026-10-30 12:00:00 */
public class MockMaterialTaskRepository {
    private final Map<String, MaterialTask> tasks = new LinkedHashMap<String, MaterialTask>();
    private final Map<String, String> requestIndex = new LinkedHashMap<String, String>();
    private final Map<String, OutboxEvent> outbox = new LinkedHashMap<String, OutboxEvent>();
    private int sequence;
    /**
     * 按租户与请求键幂等创建任务，并原子模拟记录 Outbox。
     * @param request 业务请求 @param requirements 已解析的规则清单 @return 新建或已存在任务
     */
    public synchronized MaterialTask createIfAbsent(MaterialRequest request, List<MaterialRequirement> requirements) {
        String requestId = request.getTenantId() + ":" + request.getRequestKey();
        String existingId = requestIndex.get(requestId);
        if (existingId != null) {
            System.out.println("[最终架构/Mock DB] createIfAbsent ---> 幂等命中，复用任务 " + existingId);
            return tasks.get(existingId);
        }
        String taskId = "TASK-" + (++sequence);
        MaterialTask task = new MaterialTask(taskId, request, requirements);
        tasks.put(taskId, task); requestIndex.put(requestId, taskId);
        outbox.put("EVENT-" + taskId, new OutboxEvent("EVENT-" + taskId, taskId));
        System.out.println("[最终架构/Mock DB] createIfAbsent ---> 保存任务、材料明细与 Outbox");
        return task;
    }
    /** @param taskId 任务标识 @return 已保存任务，未找到返回 null */
    public synchronized MaterialTask find(String taskId) { return tasks.get(taskId); }
    /** @return 尚未标记发布的事件 */
    public synchronized List<OutboxEvent> pendingOutbox() {
        List<OutboxEvent> result = new ArrayList<OutboxEvent>();
        for (OutboxEvent event : outbox.values()) { if (!event.isPublished()) { result.add(event); } }
        return result;
    }
    /** 标记事件已发布，教学模拟真实数据库条件更新。 */
    public synchronized void markPublished(String eventId) { outbox.get(eventId).markPublished(); }
    /**
     * 按状态和版本条件认领，演示消息重复时的幂等边界。
     * @param taskId 任务标识 @param expectedVersion 消费者读取版本 @return 是否认领成功
     */
    public synchronized boolean claim(String taskId, int expectedVersion) {
        MaterialTask task = tasks.get(taskId);
        if (task == null || task.getVersion() != expectedVersion
                || !(task.getStatus() == MaterialTask.Status.QUEUED || task.getStatus() == MaterialTask.Status.RETRY_WAIT)) {
            System.out.println("[最终架构/Mock DB] claim ---> 状态或版本不匹配，拒绝重复执行"); return false;
        }
        task.setStatus(MaterialTask.Status.RUNNING);
        System.out.println("[最终架构/Mock DB] claim ---> 条件认领成功，version=" + task.getVersion());
        return true;
    }
}
