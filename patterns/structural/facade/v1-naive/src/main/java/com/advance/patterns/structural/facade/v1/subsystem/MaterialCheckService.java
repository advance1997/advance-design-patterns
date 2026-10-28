package com.advance.patterns.structural.facade.v1.subsystem;

import com.advance.patterns.structural.facade.v1.domain.CollectionCase;
import java.util.List;

/**
 * 模拟检查案件材料是否齐备的子系统。
 *
 * @author advance
 * @since 2026-10-28 00:00:00
 */
public class MaterialCheckService {
    /**
     * 返回本案例中已经归集的材料。
     *
     * @param collectionCase 案件信息
     * @param requiredMaterials 必需材料清单
     * @return 已归集材料名称
     */
    public List<String> check(CollectionCase collectionCase, List<String> requiredMaterials) {
        System.out.println("[外观/v1] MaterialCheckService.check ---> 模拟核对案件材料");
        return requiredMaterials;
    }
}
