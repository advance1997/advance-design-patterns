package com.advance.patterns.comprehensive.material.v1;

import java.util.ArrayList;
import java.util.List;

/** 保存任务整体状态及逐项执行记录。 @author advance @since 2026-10-30 12:00:00 */
public class MaterialTask {
    /** 流程状态字段不代表使用状态模式。 */
    public enum Status { QUEUED, RUNNING, RETRY_WAIT, INCOMPLETE, PACKAGING, COMPLETED, FAILED }
    private final String taskId;
    private final MaterialRequest request;
    private final String ruleVersion;
    private final List<MaterialTaskItem> items = new ArrayList<MaterialTaskItem>();
    private Status status = Status.QUEUED;
    private int version;
    private int retryCount;
    private String objectKey;
    /**
     * 创建任务并冻结本次材料要求。
     * @param taskId 任务标识 @param request 原始请求 @param requirements 当前规则版本的材料要求
     */
    public MaterialTask(String taskId, MaterialRequest request, List<MaterialRequirement> requirements) {
        this.taskId = taskId; this.request = request;
        this.ruleVersion = "RULE-" + request.getScene() + "-V1";
        for (MaterialRequirement requirement : requirements) { items.add(new MaterialTaskItem(requirement)); }
    }
    /** @return 任务标识 */ public String getTaskId() { return taskId; }
    /** @return 原始请求 */ public MaterialRequest getRequest() { return request; }
    /** @return 创建任务时冻结的教学规则版本 */ public String getRuleVersion() { return ruleVersion; }
    /** @return 任务材料明细 */ public List<MaterialTaskItem> getItems() { return items; }
    /** @return 当前状态 */ public Status getStatus() { return status; }
    /** @return 乐观锁版本号 */ public int getVersion() { return version; }
    /** @return 有限重试次数 */ public int getRetryCount() { return retryCount; }
    /** @return 最终对象存储键 */ public String getObjectKey() { return objectKey; }
    /** 更新任务状态并递增模拟版本号。 */ public void setStatus(Status status) { this.status = status; version++; }
    /** 记录一次已安排的重试。 */ public void incrementRetryCount() { retryCount++; version++; }
    /** 设置已提交的最终对象键。 */ public void setObjectKey(String objectKey) { this.objectKey = objectKey; }
}
