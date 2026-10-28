package com.advance.patterns.behavioral.observer.v4.event;

/**
 * 描述虚构案件已分派这一事实的不可变事件快照，不包含真实客户资料。
 *
 * @author advance
 * @since 2026-10-28 20:28:32
 */
public final class CaseAssignedEvent {

    /** 虚构案件编号。 */
    private final String caseId;

    /** 模拟经办人编号。 */
    private final String collectorId;

    /**
     * 固定本次分派事实，使所有观察者读取相同快照。
     *
     * @param caseId 虚构案件编号
     * @param collectorId 模拟经办人编号
     */
    public CaseAssignedEvent(String caseId, String collectorId) {
        this.caseId = caseId;
        this.collectorId = collectorId;
    }

    /**
     * 获取事件对应的案件。
     *
     * @return 虚构案件编号
     */
    public String getCaseId() {
        return caseId;
    }

    /**
     * 获取事件记录的经办人，不在通知时重新查询可变状态。
     *
     * @return 模拟经办人编号
     */
    public String getCollectorId() {
        return collectorId;
    }
}
