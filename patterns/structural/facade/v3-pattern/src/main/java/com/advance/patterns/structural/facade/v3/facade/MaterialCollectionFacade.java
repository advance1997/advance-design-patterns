package com.advance.patterns.structural.facade.v3.facade;

import com.advance.patterns.structural.facade.v3.domain.CollectionCase;
import com.advance.patterns.structural.facade.v3.subsystem.CaseLookupService;
import com.advance.patterns.structural.facade.v3.subsystem.MaterialCheckService;
import com.advance.patterns.structural.facade.v3.subsystem.MaterialPackageAssembler;
import com.advance.patterns.structural.facade.v3.subsystem.MaterialRuleService;
import java.util.List;

/**
 * 为催收材料准备流程提供统一入口，并隐藏各子系统的调用顺序。
 *
 * @author advance
 * @since 2026-10-28 00:00:00
 */
public class MaterialCollectionFacade {
    private final CaseLookupService caseLookupService;
    private final MaterialRuleService materialRuleService;
    private final MaterialCheckService materialCheckService;
    private final MaterialPackageAssembler assembler;

    /**
     * 创建材料准备外观并组合所需子系统。
     *
     * @param caseLookupService 案件查询子系统
     * @param materialRuleService 材料规则子系统
     * @param materialCheckService 材料核对子系统
     * @param assembler 材料预览组装子系统
     */
    public MaterialCollectionFacade(CaseLookupService caseLookupService,
            MaterialRuleService materialRuleService, MaterialCheckService materialCheckService,
            MaterialPackageAssembler assembler) {
        this.caseLookupService = caseLookupService;
        this.materialRuleService = materialRuleService;
        this.materialCheckService = materialCheckService;
        this.assembler = assembler;
    }

    /**
     * 执行完整的案件材料准备流程。
     *
     * @param caseId 虚构案件编号
     * @return 材料预览文本
     */
    public String prepareForReview(String caseId) {
        System.out.println("[外观/v3] MaterialCollectionFacade.prepareForReview ---> 统一入口开始执行");
        CollectionCase collectionCase = caseLookupService.find(caseId);
        List<String> requiredMaterials = materialRuleService.getRequiredMaterials(collectionCase);
        List<String> collectedMaterials = materialCheckService.check(collectionCase, requiredMaterials);
        return assembler.assemble(collectionCase, collectedMaterials);
    }
}
