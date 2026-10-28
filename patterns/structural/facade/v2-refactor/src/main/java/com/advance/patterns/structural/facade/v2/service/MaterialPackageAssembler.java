package com.advance.patterns.structural.facade.v2.service;

import com.advance.patterns.structural.facade.v2.domain.MaterialDraft;

/**
 * 将材料工作流的中间结果整理成预览文本。
 *
 * @author advance
 * @since 2026-10-28 00:00:00
 */
public class MaterialPackageAssembler {
    /**
     * 生成内存材料预览。
     *
     * @param draft 材料中间结果
     * @return 材料预览文本
     */
    public String assemble(MaterialDraft draft) {
        System.out.println("[外观/v2] MaterialPackageAssembler.assemble ---> 生成内存材料预览");
        return "案件=" + draft.getCaseId() + "，材料=" + draft.getCollectedMaterials();
    }
}
