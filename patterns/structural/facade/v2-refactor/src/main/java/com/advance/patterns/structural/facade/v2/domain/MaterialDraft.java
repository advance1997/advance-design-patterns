package com.advance.patterns.structural.facade.v2.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 案件核对后、尚未组装成材料预览的中间结果。
 *
 * @author advance
 * @since 2026-10-28 00:00:00
 */
public class MaterialDraft {
    private final String caseId;
    private final List<String> collectedMaterials;

    /**
     * 创建材料整理中间结果。
     *
     * @param caseId 虚构案件编号
     * @param collectedMaterials 已归集材料
     */
    public MaterialDraft(String caseId, List<String> collectedMaterials) {
        this.caseId = caseId;
        this.collectedMaterials = new ArrayList<String>(collectedMaterials);
    }

    /**
     * 获取案件编号。
     *
     * @return 案件编号
     */
    public String getCaseId() { return caseId; }

    /**
     * 获取已归集材料。
     *
     * @return 不可修改的材料清单
     */
    public List<String> getCollectedMaterials() {
        return Collections.unmodifiableList(collectedMaterials);
    }
}
