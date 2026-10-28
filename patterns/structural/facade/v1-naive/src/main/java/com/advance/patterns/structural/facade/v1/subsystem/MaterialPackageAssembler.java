package com.advance.patterns.structural.facade.v1.subsystem;

import com.advance.patterns.structural.facade.v1.domain.CollectionCase;
import java.util.List;

/**
 * 模拟整理材料预览文本的子系统。
 *
 * @author advance
 * @since 2026-10-28 00:00:00
 */
public class MaterialPackageAssembler {
    /**
     * 根据案件及已归集材料生成内存预览结果。
     *
     * @param collectionCase 案件信息
     * @param materials 已归集材料
     * @return 材料预览文本
     */
    public String assemble(CollectionCase collectionCase, List<String> materials) {
        System.out.println("[外观/v1] MaterialPackageAssembler.assemble ---> 生成内存材料预览");
        return "案件=" + collectionCase.getCaseId() + "，材料=" + materials;
    }
}
