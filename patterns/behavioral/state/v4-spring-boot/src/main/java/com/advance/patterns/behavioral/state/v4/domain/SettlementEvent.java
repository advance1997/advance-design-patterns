package com.advance.patterns.behavioral.state.v4.domain;

/**
 * 驱动状态迁移的业务事件；调用方提交事件，不直接指定目标状态。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
public enum SettlementEvent {

    /** 提交审批。 */
    SUBMIT,

    /** 审批通过。 */
    APPROVE,

    /** 审批或法核驳回。 */
    REJECT,

    /** 法核通过。 */
    LEGAL_PASS,

    /** 模拟确认履约完成。 */
    CONFIRM_COMPLETION;
}
