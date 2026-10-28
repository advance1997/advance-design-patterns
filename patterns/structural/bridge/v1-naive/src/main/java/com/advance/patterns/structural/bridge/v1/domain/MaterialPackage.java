package com.advance.patterns.structural.bridge.v1.domain;

/**
 * 催收材料包抽象对象，保存虚构案件编号和场景。
 *
 * @author advance
 * @since 2026-10-28 22:05:00
 */
public class MaterialPackage {
    private final String caseId; private final String scene;
    public MaterialPackage(String caseId,String scene){this.caseId=caseId;this.scene=scene;}
    public String getCaseId(){return caseId;} public String getScene(){return scene;}
}
