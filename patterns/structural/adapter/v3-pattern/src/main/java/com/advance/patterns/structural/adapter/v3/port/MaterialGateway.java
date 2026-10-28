package com.advance.patterns.structural.adapter.v3.port;

import com.advance.patterns.structural.adapter.v3.domain.MaterialFile;

/**
 * 内部目标接口，以归集业务需要定义合同查询契约，不暴露平台参数与响应类型。
 *
 * @author advance
 * @since 2026-10-28 16:08:56
 */
public interface MaterialGateway {

    /**
     * 按内部案件编号查询合同；未找到统一表示为 found=false、fileKey 为空。
     *
     * @param caseId 内部虚构案件编号
     * @return 平台无关的材料结果
     * @throws IllegalStateException 平台响应状态无法解释时抛出，不伪装成材料未找到
     */
    MaterialFile fetchContract(long caseId);
}
