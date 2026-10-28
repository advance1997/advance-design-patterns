package com.advance.patterns.creational.prototype.v2.domain;

/**
 * 可变的人工复核步骤，用来观察浅拷贝是否传播状态修改。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public final class ReviewStep {
    /** 复核项目名称。 */
    private final String name;
    /** 是否已经人工复核，默认 false。 */
    private boolean reviewed;

    /**
     * 创建未完成的复核步骤。
     * @param name 非空复核项目名称
     * @throws IllegalArgumentException 名称为空
     */
    public ReviewStep(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("步骤 name 不能为空");
        }
        this.name = name;
    }

    /**
     * 复制一个步骤的名称和当前状态，不与原步骤共享可变状态。
     * @param source 原步骤
     * @throws IllegalArgumentException 原步骤为空
     */
    public ReviewStep(ReviewStep source) {
        if (source == null) {
            throw new IllegalArgumentException("source 不能为空");
        }
        this.name = source.name;
        this.reviewed = source.reviewed;
    }

    /**
     * 获取复核状态。
     * @return 已复核时为 true
     */
    public boolean isReviewed() {
        return reviewed;
    }

    /**
     * 设置人工复核状态，仅修改内存教学对象。
     * @param reviewed 是否已复核
     */
    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

    /**
     * 输出步骤名称和状态。
     * @return 教学摘要
     */
    @Override
    public String toString() {
        return name + "=" + reviewed;
    }
}
