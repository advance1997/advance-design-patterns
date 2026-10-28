package com.advance.patterns.structural.facade.v1.domain;

/**
 * 催收案件的内存模拟数据。
 *
 * @author advance
 * @since 2026-10-28 00:00:00
 */
public class CollectionCase {
    private final String caseId;
    private final String caseType;

    /**
     * 创建案件模拟数据。
     *
     * @param caseId 虚构案件编号
     * @param caseType 案件类型
     */
    public CollectionCase(String caseId, String caseType) {
        this.caseId = caseId;
        this.caseType = caseType;
    }

    /**
     * 获取案件编号。
     *
     * @return 案件编号
     */
    public String getCaseId() { return caseId; }

    /**
     * 获取案件类型。
     *
     * @return 案件类型
     */
    public String getCaseType() { return caseType; }
}
