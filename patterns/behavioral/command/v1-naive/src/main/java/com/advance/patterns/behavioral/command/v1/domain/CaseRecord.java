package com.advance.patterns.behavioral.command.v1.domain;

/**
 * 催收案件的内存模拟状态。
 *
 * @author advance
 * @since 2026-10-29 00:20:42
 */
public class CaseRecord {
    private final long caseId;
    private String callStatus;
    private String assigneeId;

    /**
     * 创建案件记录。
     *
     * @param caseId 案件编号
     * @param assigneeId 当前作业员
     */
    public CaseRecord(long caseId, String assigneeId) {
        this.caseId = caseId;
        this.assigneeId = assigneeId;
        this.callStatus = "ACTIVE";
    }

    /**
     * 暂停案件外呼。
     */
    public void pauseCall() { callStatus = "PAUSED"; }

    /**
     * 更新案件作业员。
     *
     * @param assigneeId 新作业员
     */
    public void reassign(String assigneeId) { this.assigneeId = assigneeId; }

    @Override
    public String toString() {
        return "案件=" + caseId + "，外呼状态=" + callStatus + "，作业员=" + assigneeId;
    }
}
