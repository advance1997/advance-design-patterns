package com.advance.patterns.behavioral.state.v2.domain;

/**
 * 表示当前状态不允许接收给定事件，拒绝任意跳转或重复终态操作。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
public class InvalidTransitionException extends RuntimeException {

    /** Java 异常序列化标识。 */
    private static final long serialVersionUID = 1L;

    /**
     * 构建明确包含当前状态与事件的拒绝信息。
     *
     * @param status 当前状态
     * @param event 当前尝试的事件
     */
    public InvalidTransitionException(SettlementStatus status, SettlementEvent event) {
        super("当前状态 " + status + " 不允许事件 " + event);
    }
}
