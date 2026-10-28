package com.advance.patterns.behavioral.command.v2.receiver;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 命令接收者，负责实际修改内存案件状态。
 *
 * @author advance
 * @since 2026-10-29 00:20:42
 */
public class CaseActionService {
    private final Map<Long, String> callStatuses = new LinkedHashMap<Long, String>();
    private final Map<Long, String> assignees = new LinkedHashMap<Long, String>();

    /**
     * 初始化虚构案件状态。
     */
    public CaseActionService() {
        callStatuses.put(10001L, "ACTIVE");
        callStatuses.put(10002L, "ACTIVE");
        assignees.put(10001L, "USER_A");
        assignees.put(10002L, "USER_B");
    }

    /**
     * 暂停案件外呼。
     *
     * @param caseId 案件编号
     * @param reason 暂停原因
     */
    public void pauseCall(long caseId, String reason) {
        requireCase(caseId);
        callStatuses.put(caseId, "PAUSED");
        System.out.println("[命令/v2] CaseActionService.pauseCall ---> 暂停外呼，原因=" + reason);
    }

    /**
     * 恢复已暂停案件的外呼。
     *
     * @param caseId 案件编号
     */
    public void resumeCall(long caseId) {
        requireCase(caseId);
        if (!"PAUSED".equals(callStatuses.get(caseId))) {
            throw new IllegalStateException("案件当前未暂停外呼");
        }
        callStatuses.put(caseId, "ACTIVE");
        System.out.println("[命令/v2] CaseActionService.resumeCall ---> 恢复案件外呼");
    }

    /**
     * 重新分配案件。
     *
     * @param caseId 案件编号
     * @param assigneeId 新作业员
     */
    public void reassign(long caseId, String assigneeId) {
        requireCase(caseId);
        assignees.put(caseId, assigneeId);
        System.out.println("[命令/v2] CaseActionService.reassign ---> 重新分案，作业员=" + assigneeId);
    }

    /**
     * 获取当前案件状态快照。
     *
     * @param caseId 案件编号
     * @return 案件状态文本
     */
    public String stateOf(long caseId) {
        requireCase(caseId);
        return "案件=" + caseId + "，外呼状态=" + callStatuses.get(caseId)
                + "，作业员=" + assignees.get(caseId);
    }

    private void requireCase(long caseId) {
        if (!callStatuses.containsKey(caseId)) {
            throw new IllegalArgumentException("案件不存在：" + caseId);
        }
    }
}
