package com.advance.patterns.behavioral.iterator.v3;

/**
 * 表示待分配的模拟催收案件。
 *
 * @author advance
 * @since 2026-10-29 09:17:03
 */
public class CaseInfo {
    /** 案件唯一编号，用于本例输出分案路径。 */
    private final long caseId;

    /**
     * 创建案件信息。
     *
     * @param caseId 模拟案件编号
     */
    public CaseInfo(long caseId) {
        this.caseId = caseId;
    }

    /**
     * 获取案件编号。
     *
     * @return 案件编号
     */
    public long getCaseId() {
        return caseId;
    }
}
