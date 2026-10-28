package com.advance.patterns.structural.facade.v1.subsystem;

import com.advance.patterns.structural.facade.v1.domain.CollectionCase;
import java.util.Arrays;
import java.util.List;

/**
 * 模拟按案件类型整理材料要求的子系统。
 *
 * @author advance
 * @since 2026-10-28 00:00:00
 */
public class MaterialRuleService {
    /**
     * 获取该案件类型的必需材料清单。
     *
     * @param collectionCase 案件信息
     * @return 必需材料名称
     */
    public List<String> getRequiredMaterials(CollectionCase collectionCase) {
        System.out.println("[外观/v1] MaterialRuleService.getRequiredMaterials ---> 按案件类型整理必需材料");
        return Arrays.asList("借款合同", "还款流水");
    }
}
