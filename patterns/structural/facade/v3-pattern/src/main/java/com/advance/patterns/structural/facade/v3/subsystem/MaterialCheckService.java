package com.advance.patterns.structural.facade.v3.subsystem;

import com.advance.patterns.structural.facade.v3.domain.CollectionCase;
import java.util.List;

/**
 * 模拟案件材料核对子系统。
 *
 * @author advance
 * @since 2026-10-28 00:00:00
 */
public class MaterialCheckService {
    /**
     * 模拟材料齐备核对并返回已归集材料。
     *
     * @param collectionCase 案件信息
     * @param requiredMaterials 必需材料
     * @return 已归集材料
     */
    public List<String> check(CollectionCase collectionCase, List<String> requiredMaterials) {
        System.out.println("[外观/v3] MaterialCheckService.check ---> 模拟核对案件材料");
        return requiredMaterials;
    }
}
