package com.advance.patterns.creational.registeredfactory.v1.domain;

/**
 * 不可变虚构案件，只保留教学编号与阶段，不存储身份、电话或欠款隐私。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public final class CaseFile {
    /** 虚构案件编号。 */
    private final String caseId;
    /** 上游确认的阶段。 */
    private final CaseStage stage;

    /**
     * 校验后保存案件。
     * @param caseId 非空教学编号
     * @param stage 非空阶段
     * @throws IllegalArgumentException 任一参数为空
     */
    public CaseFile(String caseId, CaseStage stage) {
        if (caseId == null || caseId.trim().isEmpty() || stage == null) {
            throw new IllegalArgumentException("caseId 和 stage 不能为空");
        }
        this.caseId = caseId;
        this.stage = stage;
    }

    /**
     * 获取教学编号。
     * @return 案件编号
     */
    public String getCaseId() {
        return caseId;
    }

    /**
     * 获取案件阶段。
     * @return 非空阶段
     */
    public CaseStage getStage() {
        return stage;
    }
}
