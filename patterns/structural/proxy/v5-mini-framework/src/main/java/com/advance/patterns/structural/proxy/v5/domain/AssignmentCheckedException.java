package com.advance.patterns.structural.proxy.v5.domain;

/**
 * 模拟需要人工补充核查的受检异常，用于对比默认提交与 rollbackFor。
 *
 * @author advance
 * @since 2026-10-28 10:15:29
 */
public final class AssignmentCheckedException extends Exception {
    /** 序列化版本。 */
    private static final long serialVersionUID = 1L;

    /**
     * 保存核查失败原因。
     * @param message 中文说明
     */
    public AssignmentCheckedException(String message) {
        super(message);
    }
}
