package com.advance.patterns.creational.registeredfactory.v3.handler;

import com.advance.patterns.creational.registeredfactory.v3.domain.CaseFile;
import com.advance.patterns.creational.registeredfactory.v3.domain.CaseStage;
import org.springframework.stereotype.Component;

/**
 * 后期人工复核处理器；无单次案件共享状态，仅打印模拟路径。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
@Component
public final class LateReviewHandler implements CollectionHandler {
    /**
     * 声明本处理器负责的阶段。
     * @return LATE
     */
    @Override
    public CaseStage getStage() {
        return CaseStage.LATE;
    }

    /**
     * 校验后模拟生成复核任务，不联系客户。
     * @param caseFile 虚构案件
     * @return LATE_REVIEW
     * @throws IllegalArgumentException 案件为空或阶段不匹配
     */
    @Override
    public String handle(CaseFile caseFile) {
        if (caseFile == null || caseFile.getStage() != getStage()) {
            throw new IllegalArgumentException("LateReviewHandler 仅支持 LATE 案件");
        }
        System.out.println("[registered-factory/v3] LateReviewHandler.handle ---> 模拟人工复核，caseId="
                + caseFile.getCaseId());
        return "LATE_REVIEW";
    }
}
