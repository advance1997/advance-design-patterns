package com.advance.patterns.structural.facade.v1.subsystem;

import com.advance.patterns.structural.facade.v1.domain.CollectionCase;

/**
 * 模拟读取案件基础信息的子系统。
 *
 * @author advance
 * @since 2026-10-28 00:00:00
 */
public class CaseLookupService {
    /**
     * 从内存示例数据中获取案件。
     *
     * @param caseId 虚构案件编号
     * @return 案件信息
     */
    public CollectionCase find(String caseId) {
        System.out.println("[外观/v1] CaseLookupService.find ---> 从内存示例数据读取案件");
        return new CollectionCase(caseId, "诉讼");
    }
}
