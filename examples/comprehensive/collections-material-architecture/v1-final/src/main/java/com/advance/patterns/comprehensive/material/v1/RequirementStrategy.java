package com.advance.patterns.comprehensive.material.v1;

import java.util.List;

/**
 * 为特定归集场景选择所需材料。
 *
 * @author advance
 * @since 2026-10-30 12:00:00
 */
public interface RequirementStrategy {
    /** @return 此策略支持的场景编码 */
    String scene();
    /**
     * 创建场景材料清单。
     *
     * @param caseId 案件标识
     * @return 本次任务需要获取的材料
     */
    List<MaterialRequirement> requirements(String caseId);
}
