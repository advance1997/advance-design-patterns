package com.advance.patterns.creational.registeredfactory.v1.service;

import com.advance.patterns.creational.registeredfactory.v1.domain.CaseFile;
import com.advance.patterns.creational.registeredfactory.v1.domain.CaseStage;
import com.advance.patterns.creational.registeredfactory.v1.handler.EarlyReviewHandler;
import com.advance.patterns.creational.registeredfactory.v1.handler.LateReviewHandler;

/**
 * 依赖具体处理器的服务：由 Spring 注入，但业务仍维护阶段分支。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public final class CollectionService {
    /** 早期复核处理器。 */
    private final EarlyReviewHandler early;
    /** 后期复核处理器。 */
    private final LateReviewHandler late;

    /**
     * 注入服务所需依赖。
     * @param early 早期处理器
     * @param late 后期处理器
     * @throws IllegalArgumentException 依赖为空
     */
    public CollectionService(EarlyReviewHandler early, LateReviewHandler late) {
        if (early == null || late == null) {
            throw new IllegalArgumentException("服务依赖不能为空");
        }
        this.early = early;
        this.late = late;
    }

    /**
     * 根据阶段安排模拟人工复核，禁止静默降级到另一阶段。
     * @param caseFile 虚构案件
     * @return 处理器返回的复核标识
     * @throws IllegalArgumentException 案件为空或阶段不支持
     */
    public String process(CaseFile caseFile) {
        System.out.println("[registered-factory/v1] CollectionService.process ---> 安排模拟人工复核");
        if (caseFile == null) {
            throw new IllegalArgumentException("caseFile 不能为空");
        }
        if (caseFile.getStage() == CaseStage.EARLY) {
            return early.handle(caseFile);
        }
        if (caseFile.getStage() == CaseStage.LATE) {
            return late.handle(caseFile);
        }
        throw new IllegalArgumentException("阶段未注册处理器：" + caseFile.getStage());
    }
}
