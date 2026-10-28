package com.advance.patterns.creational.registeredfactory.v1.handler;

import com.advance.patterns.creational.registeredfactory.v1.domain.CaseFile;
import com.advance.patterns.creational.registeredfactory.v1.domain.CaseStage;

/**
 * 早期人工复核处理器；无单次案件共享状态，仅打印模拟路径。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public final class EarlyReviewHandler implements CollectionHandler {
    /**
     * 声明本处理器负责的阶段。
     * @return EARLY
     */
    @Override
    public CaseStage getStage() {
        return CaseStage.EARLY;
    }

    /**
     * 校验后模拟生成复核任务，不联系客户。
     * @param caseFile 虚构案件
     * @return EARLY_REVIEW
     * @throws IllegalArgumentException 案件为空或阶段不匹配
     */
    @Override
    public String handle(CaseFile caseFile) {
        if (caseFile == null || caseFile.getStage() != getStage()) {
            throw new IllegalArgumentException("EarlyReviewHandler 仅支持 EARLY 案件");
        }
        System.out.println("[registered-factory/v1] EarlyReviewHandler.handle ---> 模拟人工复核，caseId="
                + caseFile.getCaseId());
        return "EARLY_REVIEW";
    }
}
