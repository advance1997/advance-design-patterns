package com.advance.patterns.behavioral.state.v4.domain;

/**
 * 和解方案状态；完成与驳回是本教学流程的终态。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
public enum SettlementStatus {

    /** 草稿。 */
    DRAFT,

    /** 待审批。 */
    PENDING_APPROVAL,

    /** 待法核。 */
    PENDING_LEGAL,

    /** 履约中。 */
    PERFORMING,

    /** 已完成。 */
    COMPLETED,

    /** 已驳回。 */
    REJECTED;
}
