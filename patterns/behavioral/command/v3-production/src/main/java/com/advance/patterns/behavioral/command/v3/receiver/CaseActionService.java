package com.advance.patterns.behavioral.command.v3.receiver;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 生产案例中的业务接收者，以内存案件记录模拟领域服务。
 *
 * @author advance
 * @since 2026-10-29 00:20:42
 */
public class CaseActionService {
    private final Map<Long, CaseRecord> cases = new LinkedHashMap<Long, CaseRecord>();

    /**
     * 准备两条用于演示暂停外呼和重新分案的案件。
     */
    public CaseActionService() {
        cases.put(10001L, new CaseRecord(10001L, "ACTIVE", "USER_A"));
        cases.put(10002L, new CaseRecord(10002L, "ACTIVE", "USER_B"));
    }

    /**
     * 按案件编号暂停外呼。
     *
     * @param caseId 案件编号
     * @param reason 暂停原因
     */
    public void pauseCall(long caseId, String reason) {
        CaseRecord record = getCase(caseId);
        if ("PAUSED".equals(record.callStatus)) {
            throw new IllegalStateException("案件已经暂停外呼");
        }
        record.callStatus = "PAUSED";
        System.out.println("[命令/v3] CaseActionService.pauseCall ---> 案件=" + caseId + "，原因=" + reason);
    }

    /**
     * 将案件重新分配给指定作业员。
     *
     * @param caseId 案件编号
     * @param assigneeId 目标作业员
     */
    public void reassign(long caseId, String assigneeId) {
        if (assigneeId == null || assigneeId.trim().isEmpty()) {
            throw new IllegalArgumentException("作业员不能为空");
        }
        CaseRecord record = getCase(caseId);
        record.assigneeId = assigneeId;
        System.out.println("[命令/v3] CaseActionService.reassign ---> 案件=" + caseId + "，新作业员=" + assigneeId);
    }

    /**
     * 获取案件当前状态快照。
     *
     * @param caseId 案件编号
     * @return 案件状态文本
     */
    public String stateOf(long caseId) {
        CaseRecord record = getCase(caseId);
        return "案件=" + caseId + "，外呼状态=" + record.callStatus + "，作业员=" + record.assigneeId;
    }

    private CaseRecord getCase(long caseId) {
        CaseRecord record = cases.get(caseId);
        if (record == null) {
            throw new IllegalArgumentException("案件不存在：" + caseId);
        }
        return record;
    }

    private static class CaseRecord {
        private final long caseId;
        private String callStatus;
        private String assigneeId;

        private CaseRecord(long caseId, String callStatus, String assigneeId) {
            this.caseId = caseId;
            this.callStatus = callStatus;
            this.assigneeId = assigneeId;
        }
    }
}
