package com.advance.patterns.behavioral.state.v4.web;

import com.advance.patterns.behavioral.state.v4.domain.SettlementEvent;

/**
 * 前端状态迁移请求；客户端不能设置目标状态或绕过当前状态规则。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
public class TransitionCommand {

    /** 业务事件，不是目标状态。 */
    private SettlementEvent event;

    /** 客户端读取到的预期版本，必须显式提供。 */
    private Long expectedVersion;

    /**
     * 提供框架映射所需的无参构造方法。
     */
    public TransitionCommand() {
    }

    /**
     * 获取业务事件，不是目标状态。
     *
     * @return 业务事件，不是目标状态
     */
    public SettlementEvent getEvent() {
        return event;
    }

    /**
     * 设置业务事件，不是目标状态，仅用于请求或持久化映射。
     *
     * @param event 业务事件，不是目标状态
     */
    public void setEvent(SettlementEvent event) {
        this.event = event;
    }

    /**
     * 获取客户端读取到的预期版本，必须显式提供。
     *
     * @return 客户端读取到的预期版本，必须显式提供
     */
    public Long getExpectedVersion() {
        return expectedVersion;
    }

    /**
     * 设置客户端读取到的预期版本，必须显式提供，仅用于请求或持久化映射。
     *
     * @param expectedVersion 客户端读取到的预期版本，必须显式提供
     */
    public void setExpectedVersion(Long expectedVersion) {
        this.expectedVersion = expectedVersion;
    }
}
