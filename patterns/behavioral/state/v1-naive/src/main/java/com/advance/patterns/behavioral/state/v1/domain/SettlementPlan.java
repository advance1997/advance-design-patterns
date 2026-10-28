package com.advance.patterns.behavioral.state.v1.domain;

/**
 * 仅保存虚构方案状态的上下文数据，不执行真实审批或还款。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
public class SettlementPlan {

    /** 虚构方案编号。 */
    private final long id;

    /** 当前方案状态。 */
    private SettlementStatus status;

    /**
     * 创建初始状态为草稿的教学方案。
     *
     * @param id 虚构方案编号
     */
    public SettlementPlan(long id) {
        this.id = id;
        this.status = SettlementStatus.DRAFT;
    }

    /**
     * 获取方案编号。
     *
     * @return 虚构方案编号
     */
    public long getId() {
        return id;
    }

    /**
     * 获取当前状态，供服务选择对应行为。
     *
     * @return 当前状态
     */
    public SettlementStatus getStatus() {
        return status;
    }

    /**
     * 保存规则已判定的下一状态，仅供教学服务调用；生产写入在 v4 由事务和条件更新保护。
     *
     * @param status 规则允许的目标状态
     */
    public void changeStatus(SettlementStatus status) {
        this.status = status;
    }
}
