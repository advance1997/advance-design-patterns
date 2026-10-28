package com.advance.patterns.creational.registeredfactory.v2.domain;

/**
 * 虚构催收案件的人工处理阶段；阶段由上游给定，不推断个人信用或催收合法性。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public enum CaseStage {
    /** 早期案件人工核对。 */
    EARLY,
    /** 后期案件人工复核。 */
    LATE,
    /** 预留法务人工审核，不执行自动法律行动。 */
    LEGAL_REVIEW
}
