package com.advance.patterns.structural.facade.v3.domain;

/**
 * 用于材料归集流程的案件基础信息。
 *
 * @author advance
 * @since 2026-10-28 00:00:00
 */
public class CollectionCase {
    private final String caseId;
    private final String caseType;

    /**
     * 创建案件信息。
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
