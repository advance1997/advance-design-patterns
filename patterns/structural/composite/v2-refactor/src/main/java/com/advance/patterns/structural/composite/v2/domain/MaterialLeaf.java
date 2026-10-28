package com.advance.patterns.structural.composite.v2.domain;

/**
 * 催收案件中的单项材料。
 *
 * @author advance
 * @since 2026-10-28 00:00:00
 */
public class MaterialLeaf {
    private final String name;
    private final boolean required;

    /**
     * 创建材料叶子。
     *
     * @param name 材料名称
     * @param required 是否必需
     */
    public MaterialLeaf(String name, boolean required) { this.name = name; this.required = required; }

    /**
     * 输出材料名称和要求。
     *
     * @param indent 当前缩进
     */
    public void print(String indent) {
        System.out.println(indent + "材料：" + name + (required ? "（必需）" : "（可选）"));
    }
}
