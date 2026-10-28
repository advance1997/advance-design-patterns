package com.advance.patterns.structural.facade.v2.service;

import com.advance.patterns.structural.facade.v2.domain.MaterialDraft;
import java.util.Arrays;
import java.util.List;

/**
 * v2 将案件读取、规则获取及材料核对集中到一个工作流服务。
 *
 * @author advance
 * @since 2026-10-28 00:00:00
 */
public class MaterialPreparationService {
    /**
     * 准备案件的材料中间结果；最终预览仍需由调用方另行组装。
     *
     * @param caseId 虚构案件编号
     * @return 待组装的材料中间结果
     */
    public MaterialDraft prepareDraft(String caseId) {
        System.out.println("[外观/v2] MaterialPreparationService.prepareDraft ---> 开始材料准备工作流");
        System.out.println("[外观/v2] CaseLookupService.find ---> 从内存示例数据读取案件");
        System.out.println("[外观/v2] MaterialRuleService.getRequiredMaterials ---> 获取诉讼必需材料");
        System.out.println("[外观/v2] MaterialCheckService.check ---> 模拟核对案件材料");
        List<String> collectedMaterials = Arrays.asList("借款合同", "还款流水");
        return new MaterialDraft(caseId, collectedMaterials);
    }
}
