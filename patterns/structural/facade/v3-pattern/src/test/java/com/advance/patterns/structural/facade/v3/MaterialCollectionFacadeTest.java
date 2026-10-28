package com.advance.patterns.structural.facade.v3;

import com.advance.patterns.structural.facade.v3.facade.MaterialCollectionFacade;
import com.advance.patterns.structural.facade.v3.subsystem.CaseLookupService;
import com.advance.patterns.structural.facade.v3.subsystem.MaterialCheckService;
import com.advance.patterns.structural.facade.v3.subsystem.MaterialPackageAssembler;
import com.advance.patterns.structural.facade.v3.subsystem.MaterialRuleService;
import org.junit.jupiter.api.Test;

/**
 * v3 调用方只需要使用材料准备外观。
 *
 * @author advance
 * @since 2026-10-28 00:00:00
 */
public class MaterialCollectionFacadeTest {
    /**
     * 期望路径：调用方调用一次外观入口，外观依次完成四个子系统步骤。
     */
    @Test
    public void shouldPrepareMaterialPackageThroughFacade() {
        MaterialCollectionFacade facade = new MaterialCollectionFacade(
                new CaseLookupService(), new MaterialRuleService(),
                new MaterialCheckService(), new MaterialPackageAssembler());

        System.out.println("期望：客户端只调用 MaterialCollectionFacade.prepareForReview");
        System.out.println("材料预览：" + facade.prepareForReview("DEMO-001"));
    }
}
