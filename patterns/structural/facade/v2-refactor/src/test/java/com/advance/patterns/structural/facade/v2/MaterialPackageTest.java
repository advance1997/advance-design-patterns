package com.advance.patterns.structural.facade.v2;

import com.advance.patterns.structural.facade.v2.domain.MaterialDraft;
import com.advance.patterns.structural.facade.v2.service.MaterialPackageAssembler;
import com.advance.patterns.structural.facade.v2.service.MaterialPreparationService;
import org.junit.jupiter.api.Test;

/**
 * v2 虽然集中工作流，但调用方仍须另行完成材料预览组装。
 *
 * @author advance
 * @since 2026-10-28 00:00:00
 */
public class MaterialPackageTest {
    /**
     * 期望路径：工作流准备中间结果 → 调用方再调用组装器生成预览。
     */
    @Test
    public void shouldPrepareDraftThenAssemblePreview() {
        MaterialPreparationService preparationService = new MaterialPreparationService();
        MaterialPackageAssembler assembler = new MaterialPackageAssembler();

        System.out.println("期望路径：工作流准备中间结果 → 调用方再调用组装器生成预览");
        MaterialDraft draft = preparationService.prepareDraft("DEMO-001");
        System.out.println("材料预览：" + assembler.assemble(draft));
    }
}
