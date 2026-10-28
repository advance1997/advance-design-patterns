package com.advance.patterns.structural.facade.v1;

import com.advance.patterns.structural.facade.v1.domain.CollectionCase;
import com.advance.patterns.structural.facade.v1.subsystem.CaseLookupService;
import com.advance.patterns.structural.facade.v1.subsystem.MaterialCheckService;
import com.advance.patterns.structural.facade.v1.subsystem.MaterialPackageAssembler;
import com.advance.patterns.structural.facade.v1.subsystem.MaterialRuleService;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * v1 由调用方直接组织多个材料子系统。
 *
 * @author advance
 * @since 2026-10-28 00:00:00
 */
public class MaterialPackageTest {
    /**
     * 期望路径：读取案件 → 获取材料要求 → 核对材料 → 生成预览。
     */
    @Test
    public void shouldPrepareMaterialPreviewByCallingSubsystems() {
        CaseLookupService caseLookupService = new CaseLookupService();
        MaterialRuleService materialRuleService = new MaterialRuleService();
        MaterialCheckService materialCheckService = new MaterialCheckService();
        MaterialPackageAssembler assembler = new MaterialPackageAssembler();

        System.out.println("期望路径：读取案件 → 获取材料要求 → 核对材料 → 生成预览");
        CollectionCase collectionCase = caseLookupService.find("DEMO-001");
        List<String> required = materialRuleService.getRequiredMaterials(collectionCase);
        List<String> collected = materialCheckService.check(collectionCase, required);
        System.out.println("材料预览：" + assembler.assemble(collectionCase, collected));
    }
}
