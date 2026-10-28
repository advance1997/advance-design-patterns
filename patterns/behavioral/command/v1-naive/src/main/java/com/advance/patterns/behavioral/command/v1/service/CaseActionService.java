package com.advance.patterns.behavioral.command.v1.service;

import com.advance.patterns.behavioral.command.v1.domain.CaseRecord;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 对内存案件数据执行具体业务动作。
 *
 * @author advance
 * @since 2026-10-29 00:20:42
 */
public class CaseActionService {
    private final Map<Long, CaseRecord> cases = new LinkedHashMap<Long, CaseRecord>();

    /**
     * 初始化虚构案件。
     */
    public CaseActionService() {
        cases.put(10001L, new CaseRecord(10001L, "USER_A"));
        cases.put(10002L, new CaseRecord(10002L, "USER_B"));
    }

    /**
     * 暂停指定案件的外呼。
     *
     * @param caseId 案件编号
     * @param reason 暂停原因
     */
    public void pauseCall(long caseId, String reason) {
        CaseRecord record = getCase(caseId);
        record.pauseCall();
        System.out.println("[命令/v1] CaseActionService.pauseCall ---> 暂停案件外呼，原因=" + reason);
    }

    /**
     * 将案件重新分配给指定作业员。
     *
     * @param caseId 案件编号
     * @param assigneeId 新作业员
     */
    public void reassign(long caseId, String assigneeId) {
        getCase(caseId).reassign(assigneeId);
        System.out.println("[命令/v1] CaseActionService.reassign ---> 重新分案，作业员=" + assigneeId);
    }

    /**
     * 获取案件状态的可读快照。
     *
     * @param caseId 案件编号
     * @return 案件状态文本
     */
    public String stateOf(long caseId) { return getCase(caseId).toString(); }

    private CaseRecord getCase(long caseId) {
        CaseRecord record = cases.get(caseId);
        if (record == null) {
            throw new IllegalArgumentException("案件不存在：" + caseId);
        }
        return record;
    }
}
