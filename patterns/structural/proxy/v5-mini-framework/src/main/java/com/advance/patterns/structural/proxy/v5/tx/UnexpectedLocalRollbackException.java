package com.advance.patterns.structural.proxy.v5.tx;

/**
 * 内部 REQUIRED 调用已标记仅回滚，但外层仍尝试正常提交时抛出的教学异常。
 *
 * @author advance
 * @since 2026-10-28 10:15:29
 */
public final class UnexpectedLocalRollbackException extends RuntimeException {
    /** 序列化版本。 */
    private static final long serialVersionUID = 1L;

    /**
     * 记录无法提交原因。
     * @param message 中文异常说明
     */
    public UnexpectedLocalRollbackException(String message) {
        super(message);
    }
}
