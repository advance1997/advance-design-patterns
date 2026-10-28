package com.advance.patterns.structural.bridge.v1.domain;

/**
 * 催收材料包抽象对象，保存虚构案件编号和场景。
 *
 * @author advance
 * @since 2026-10-28 22:05:00
 */
public class MaterialPackage {
    private final String caseId;
    private final String scene;

    /**
     * 创建案件材料包数据。
     *
     * @param caseId 虚构案件编号
     * @param scene 材料包场景
     */
    public MaterialPackage(String caseId, String scene) {
        this.caseId = caseId;
        this.scene = scene;
    }

    /**
     * 获取案件编号。
     *
     * @return 案件编号
     */
    public String getCaseId() { return caseId; }

    /**
     * 获取材料包场景。
     *
     * @return 材料包场景
     */
    public String getScene() { return scene; }
}
