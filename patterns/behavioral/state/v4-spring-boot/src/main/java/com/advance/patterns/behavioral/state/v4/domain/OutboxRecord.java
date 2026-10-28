package com.advance.patterns.behavioral.state.v4.domain;

/**
 * 与状态更新同事务写入的待投递事件，字段即完整教学消息，不包含真实客户资料。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
public class OutboxRecord {

    /** 服务端生成的唯一事件编号。 */
    private String eventId;

    /** 虚构方案编号。 */
    private long planId;

    /** 迁移后的方案版本。 */
    private long planVersion;

    /** 触发迁移的事件。 */
    private SettlementEvent event;

    /** 迁移前状态。 */
    private SettlementStatus fromStatus;

    /** 迁移后状态。 */
    private SettlementStatus toStatus;

    /** 投递标记 NEW 或 SENT。 */
    private String deliveryStatus;

    /**
     * 提供框架映射所需的无参构造方法。
     */
    public OutboxRecord() {
    }

    /**
     * 获取服务端生成的唯一事件编号。
     *
     * @return 服务端生成的唯一事件编号
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * 设置服务端生成的唯一事件编号，仅用于请求或持久化映射。
     *
     * @param eventId 服务端生成的唯一事件编号
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
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
     * 获取迁移后的方案版本。
     *
     * @return 迁移后的方案版本
     */
    public long getPlanVersion() {
        return planVersion;
    }

    /**
     * 设置迁移后的方案版本，仅用于请求或持久化映射。
     *
     * @param planVersion 迁移后的方案版本
     */
    public void setPlanVersion(long planVersion) {
        this.planVersion = planVersion;
    }

    /**
     * 获取触发迁移的事件。
     *
     * @return 触发迁移的事件
     */
    public SettlementEvent getEvent() {
        return event;
    }

    /**
     * 设置触发迁移的事件，仅用于请求或持久化映射。
     *
     * @param event 触发迁移的事件
     */
    public void setEvent(SettlementEvent event) {
        this.event = event;
    }

    /**
     * 获取迁移前状态。
     *
     * @return 迁移前状态
     */
    public SettlementStatus getFromStatus() {
        return fromStatus;
    }

    /**
     * 设置迁移前状态，仅用于请求或持久化映射。
     *
     * @param fromStatus 迁移前状态
     */
    public void setFromStatus(SettlementStatus fromStatus) {
        this.fromStatus = fromStatus;
    }

    /**
     * 获取迁移后状态。
     *
     * @return 迁移后状态
     */
    public SettlementStatus getToStatus() {
        return toStatus;
    }

    /**
     * 设置迁移后状态，仅用于请求或持久化映射。
     *
     * @param toStatus 迁移后状态
     */
    public void setToStatus(SettlementStatus toStatus) {
        this.toStatus = toStatus;
    }

    /**
     * 获取投递标记 NEW 或 SENT。
     *
     * @return 投递标记 NEW 或 SENT
     */
    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    /**
     * 设置投递标记 NEW 或 SENT，仅用于请求或持久化映射。
     *
     * @param deliveryStatus 投递标记 NEW 或 SENT
     */
    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
