package com.advance.patterns.creational.factoryioc.v3.domain;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 一次人工复核的模拟会话；prototype 创建后由使用方显式关闭，不保存客户数据。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public final class ReviewSession implements AutoCloseable {
    /** 教学序号生成器，只用于展示对象不同，不代表生产业务编号。 */
    private static final AtomicInteger SEQUENCE = new AtomicInteger();
    /** 本会话序号。 */
    private final int id = SEQUENCE.incrementAndGet();
    /** 是否已释放。 */
    private boolean closed;

    /**
     * 创建模拟会话。
     */
    public ReviewSession() {
        System.out.println("[factory-ioc/v3] ReviewSession ---> 创建独立会话，id=" + id);
    }

    /**
     * 获取教学序号。
     * @return 本会话序号
     */
    public int getId() {
        return id;
    }

    /**
     * 记录虚构案件进入会话。
     * @param caseId 非空教学编号
     * @throws IllegalStateException 会话已关闭
     * @throws IllegalArgumentException 编号为空
     */
    public void bind(String caseId) {
        if (closed) {
            throw new IllegalStateException("会话已关闭");
        }
        if (caseId == null || caseId.trim().isEmpty()) {
            throw new IllegalArgumentException("caseId 不能为空");
        }
        System.out.println("[factory-ioc/v3] ReviewSession.bind ---> 绑定虚构案件，id=" + id);
    }

    /**
     * 幂等关闭模拟会话。
     */
    @Override
    public void close() {
        if (!closed) {
            closed = true;
            System.out.println("[factory-ioc/v3] ReviewSession.close ---> 使用方释放会话，id=" + id);
        }
    }
}
