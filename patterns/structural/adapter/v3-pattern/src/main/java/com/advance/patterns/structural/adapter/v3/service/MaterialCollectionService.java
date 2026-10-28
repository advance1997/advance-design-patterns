package com.advance.patterns.structural.adapter.v3.service;

import com.advance.patterns.structural.adapter.v3.domain.MaterialFile;
import com.advance.patterns.structural.adapter.v3.port.MaterialGateway;

/**
 * 只依赖内部目标接口的合同归集入口，切换平台无需修改业务调用方法。
 *
 * @author advance
 * @since 2026-10-28 16:08:56
 */
public class MaterialCollectionService {

    /** 经适配后的材料查询端口，不依赖具体平台 SDK。 */
    private final MaterialGateway gateway;

    /**
     * 注入任意遵守统一契约的适配器。
     *
     * @param gateway 材料查询端口
     */
    public MaterialCollectionService(MaterialGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * 通过同一个业务方法查询合同。
     *
     * @param caseId 内部虚构案件编号
     * @return 统一合同材料结果
     */
    public MaterialFile collectContract(long caseId) {
        System.out.println("[适配器/v3] MaterialCollectionService.collectContract ---> 调用 MaterialGateway.fetchContract");
        return gateway.fetchContract(caseId);
    }
}
