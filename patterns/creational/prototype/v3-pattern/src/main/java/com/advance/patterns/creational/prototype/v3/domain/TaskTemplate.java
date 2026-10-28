package com.advance.patterns.creational.prototype.v3.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * 深拷贝版本：复制构造器逐项复制可变步骤，读取时也返回深拷贝快照。
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
        this.steps = new ArrayList<ReviewStep>();
        for (ReviewStep step : steps) {
            this.steps.add(new ReviewStep(step));
        }
    }

    /**
     * 为新的案件批次复制完整模板及当前步骤状态。
     * @param source 原模板
     * @param newName 新模板名称
     * @throws IllegalArgumentException 原模板为空或新名称非法
     */
    public TaskTemplate(TaskTemplate source, String newName) {
        if (source == null) {
            throw new IllegalArgumentException("source 不能为空");
        }
        validate(newName, source.steps);
        this.name = newName;
        this.steps = new ArrayList<ReviewStep>();
        for (ReviewStep step : source.steps) {
            this.steps.add(new ReviewStep(step));
        }
    }

    /**
     * 通过复制构造器创建深拷贝，不依赖 Cloneable。
     * @param newName 新模板名称
     * @return 独立模板及独立步骤
     * @throws IllegalArgumentException 新名称非法
     */
    public TaskTemplate copy(String newName) {
        System.out.println("[prototype/v3] TaskTemplate.copy ---> 深拷贝全部可变步骤");
        return new TaskTemplate(this, newName);
    }

    /**
     * 返回不可修改的列表，其元素也复制，外部修改元素不会影响内部状态。
     * @return 深拷贝只读列表
     */
    public List<ReviewStep> getSteps() {
        List<ReviewStep> snapshot = new ArrayList<ReviewStep>();
        for (ReviewStep step : steps) {
            snapshot.add(new ReviewStep(step));
        }
        return Collections.unmodifiableList(snapshot);
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
        System.out.println("[prototype/v3] TaskTemplate.markReviewed ---> 标记本模板步骤，index=" + index);
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
