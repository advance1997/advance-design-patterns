package com.advance.patterns.behavioral.state.v4.domain;

/**
 * 状态迁移成功响应；事件已入库不等于已可靠投递到外部系统。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
public class TransitionResult {

    /** 虚构方案编号。 */
    private long planId;

    /** 成功迁移后的状态。 */
    private SettlementStatus status;

    /** 成功迁移后的版本。 */
    private long version;

    /** 已同事务写入的 Outbox 事件编号。 */
    private String eventId;

    /**
     * 提供框架映射所需的无参构造方法。
     */
    public TransitionResult() {
    }

    /**
     * 获取虚构方案编号。
     *
     * @return 虚构方案编号
     */
    public long getPlanId() {
        return planId;
    }

    /**
     * 设置虚构方案编号，仅用于请求或持久化映射。
     *
     * @param planId 虚构方案编号
     */
    public void setPlanId(long planId) {
        this.planId = planId;
    }

    /**
     * 获取成功迁移后的状态。
     *
     * @return 成功迁移后的状态
     */
    public SettlementStatus getStatus() {
        return status;
    }

    /**
     * 设置成功迁移后的状态，仅用于请求或持久化映射。
     *
     * @param status 成功迁移后的状态
     */
    public void setStatus(SettlementStatus status) {
        this.status = status;
    }

    /**
     * 获取成功迁移后的版本。
     *
     * @return 成功迁移后的版本
     */
    public long getVersion() {
        return version;
    }

    /**
     * 设置成功迁移后的版本，仅用于请求或持久化映射。
     *
     * @param version 成功迁移后的版本
     */
    public void setVersion(long version) {
        this.version = version;
    }

    /**
     * 获取已同事务写入的 Outbox 事件编号。
     *
     * @return 已同事务写入的 Outbox 事件编号
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * 设置已同事务写入的 Outbox 事件编号，仅用于请求或持久化映射。
     *
     * @param eventId 已同事务写入的 Outbox 事件编号
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
