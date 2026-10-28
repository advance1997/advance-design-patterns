package com.advance.patterns.creational.prototype.v1.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 手工复用版本：构造时直接保存步骤列表，两个模板会共享集合和元素。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public final class TaskTemplate {
    /** 模板名称。 */
    private final String name;
    /** 本模板步骤；旧版本故意暴露共享风险。 */
    private final List<ReviewStep> steps;

    /**
     * 创建催收人工复核模板，执行对应版本的复制策略。
     * @param name 模板名称
     * @param steps 非空且不含 null 的步骤列表
     * @throws IllegalArgumentException 名称或步骤非法
     */
    public TaskTemplate(String name, List<ReviewStep> steps) {
        validate(name, steps);
        this.name = name;
        this.steps = steps;
    }


    /**
     * 返回内部列表，刻意展示暴露可变集合的风险。
     * @return 内部可变列表
     */
    public List<ReviewStep> getSteps() {
        return steps;
    }

    /**
     * 修改本模板指定步骤的人工复核状态。
     * @param index 从 0 开始的步骤位置
     * @throws IllegalArgumentException 位置越界
     */
    public void markReviewed(int index) {
        if (index < 0 || index >= steps.size()) {
            throw new IllegalArgumentException("index 超出步骤范围");
        }
        System.out.println("[prototype/v1] TaskTemplate.markReviewed ---> 标记本模板步骤，index=" + index);
        steps.get(index).setReviewed(true);
    }

    /**
     * 输出模板状态，便于对照复制前后变化。
     * @return 名称和步骤列表
     */
    public String describe() {
        return name + ": " + steps;
    }

    /**
     * 校验模板输入，避免复制不可用对象。
     * @param name 模板名称
     * @param steps 步骤列表
     * @throws IllegalArgumentException 任一输入非法
     */
    private static void validate(String name, List<ReviewStep> steps) {
        if (name == null || name.trim().isEmpty() || steps == null || steps.isEmpty()) {
            throw new IllegalArgumentException("模板 name 与 steps 不能为空");
        }
        for (ReviewStep step : steps) {
            if (step == null) {
                throw new IllegalArgumentException("steps 不能包含 null");
            }
        }
    }
}
