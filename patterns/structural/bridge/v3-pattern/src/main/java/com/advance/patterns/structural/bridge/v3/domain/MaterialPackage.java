package com.advance.patterns.structural.bridge.v3.domain;

/**
 * 桥接示例的案件数据，只保存材料包抽象实现需要的案件编号。
 *
 * @author advance
 * @since 2026-10-28 22:05:00
 */
public class MaterialPackage {
    private final String caseId;

    /**
     * 创建案件材料数据。
     *
     * @param caseId 虚构案件编号
     */
    public MaterialPackage(String caseId) {
        this.caseId = caseId;
    }

    /**
     * 获取案件编号。
     *
     * @return 案件编号
     */
    public String getCaseId() {
        return caseId;
    }
}
