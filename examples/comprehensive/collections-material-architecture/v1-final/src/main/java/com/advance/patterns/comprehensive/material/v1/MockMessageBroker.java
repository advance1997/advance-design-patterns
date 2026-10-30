package com.advance.patterns.comprehensive.material.v1;

import java.util.LinkedList;
import java.util.Queue;

/** 使用进程内队列模拟消息代理。 @author advance @since 2026-10-30 12:00:00 */
public class MockMessageBroker {
    private final Queue<String> taskIds = new LinkedList<String>();
    /** 投递任务消息。 @param taskId 归集任务标识 */
    public void send(String taskId) { taskIds.offer(taskId); System.out.println("[最终架构/Mock MQ] send ---> 投递 " + taskId); }
    /** @return 下一条任务标识，队列为空时为 null */ public String poll() { return taskIds.poll(); }
    /** 重新安排任务重试。 @param taskId 任务标识 */ public void retry(String taskId) { send(taskId); }
}
