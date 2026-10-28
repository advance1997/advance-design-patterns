package com.advance.patterns.structural.facade.v3.subsystem;

import com.advance.patterns.structural.facade.v3.domain.CollectionCase;

/**
 * 模拟案件基础信息子系统。
 *
 * @author advance
 * @since 2026-10-28 00:00:00
 */
public class CaseLookupService {
    /**
     * 从内存示例数据获取案件信息。
     *
     * @param caseId 虚构案件编号
     * @return 案件信息
     */
    public CollectionCase find(String caseId) {
        System.out.println("[外观/v3] CaseLookupService.find ---> 从内存示例数据读取案件");
        return new CollectionCase(caseId, "诉讼");
    }
}
