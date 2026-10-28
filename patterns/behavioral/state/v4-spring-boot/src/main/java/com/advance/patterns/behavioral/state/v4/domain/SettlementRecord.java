package com.advance.patterns.behavioral.state.v4.domain;

/**
 * 数据库方案快照；服务通过条件 SQL 更新，不直接保存客户端指定状态。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
public class SettlementRecord {

    /** 虚构方案编号。 */
    private long id;

    /** 数据库当前状态。 */
    private SettlementStatus status;

    /** 数据库乐观锁版本。 */
    private long version;

    /**
     * 提供框架映射所需的无参构造方法。
     */
    public SettlementRecord() {
    }

    /**
     * 获取虚构方案编号。
     *
     * @return 虚构方案编号
     */
    public long getId() {
        return id;
    }

    /**
     * 设置虚构方案编号，仅用于请求或持久化映射。
     *
     * @param id 虚构方案编号
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * 获取数据库当前状态。
     *
     * @return 数据库当前状态
     */
    public SettlementStatus getStatus() {
        return status;
    }

    /**
     * 设置数据库当前状态，仅用于请求或持久化映射。
     *
     * @param status 数据库当前状态
     */
    public void setStatus(SettlementStatus status) {
        this.status = status;
    }

    /**
     * 获取数据库乐观锁版本。
     *
     * @return 数据库乐观锁版本
     */
    public long getVersion() {
        return version;
    }

    /**
     * 设置数据库乐观锁版本，仅用于请求或持久化映射。
     *
     * @param version 数据库乐观锁版本
     */
    public void setVersion(long version) {
        this.version = version;
    }
}
