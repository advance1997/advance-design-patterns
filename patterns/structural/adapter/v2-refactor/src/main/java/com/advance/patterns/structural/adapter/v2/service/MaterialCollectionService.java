package com.advance.patterns.structural.adapter.v2.service;

import com.advance.patterns.structural.adapter.v2.domain.MaterialFile;
import com.advance.patterns.structural.adapter.v2.reader.AlphaMaterialReader;
import com.advance.patterns.structural.adapter.v2.reader.BetaMaterialReader;

/**
 * 过渡服务：外部字段转换已移走，但仍依赖两个具体 Reader 和不同方法。
 *
 * @author advance
 * @since 2026-10-28 16:08:56
 */
public class MaterialCollectionService {

    /** Alpha 平台专用读取器。 */
    private final AlphaMaterialReader alpha;

    /** Beta 平台专用读取器。 */
    private final BetaMaterialReader beta;

    /**
     * 注入两个具体平台读取器。
     *
     * @param alpha Alpha 专用读取器
     * @param beta Beta 专用读取器
     */
    public MaterialCollectionService(AlphaMaterialReader alpha, BetaMaterialReader beta) {
        this.alpha = alpha;
        this.beta = beta;
    }

    /**
     * 调用 Alpha 专用方法，业务仍感知平台区别。
     *
     * @param caseId 内部虚构案件编号
     * @return 统一合同材料结果
     */
    public MaterialFile collectFromAlpha(long caseId) {
        System.out.println("[适配器/v2] MaterialCollectionService.collectFromAlpha ---> 调用 AlphaMaterialReader");
        return alpha.readContract(caseId);
    }

    /**
     * 调用 Beta 专用方法，业务仍感知平台区别。
     *
     * @param caseId 内部虚构案件编号
     * @return 统一合同材料结果
     */
    public MaterialFile collectFromBeta(long caseId) {
        System.out.println("[适配器/v2] MaterialCollectionService.collectFromBeta ---> 调用 BetaMaterialReader");
        return beta.loadContract(caseId);
    }
}
