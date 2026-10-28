package com.advance.patterns.structural.facade.v3.subsystem;

import com.advance.patterns.structural.facade.v3.domain.CollectionCase;
import java.util.Arrays;
import java.util.List;

/**
 * 模拟案件材料规则子系统。
 *
 * @author advance
 * @since 2026-10-28 00:00:00
 */
public class MaterialRuleService {
    /**
     * 按案件类型返回必需材料。
     *
     * @param collectionCase 案件信息
     * @return 必需材料名称
     */
    public List<String> getRequiredMaterials(CollectionCase collectionCase) {
        System.out.println("[外观/v3] MaterialRuleService.getRequiredMaterials ---> 获取诉讼材料要求");
        return Arrays.asList("借款合同", "还款流水");
    }
}
