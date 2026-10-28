package com.advance.patterns.structural.composite.v1.domain;

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
    public MaterialLeaf(String name, boolean required) {
        this.name = name;
        this.required = required;
    }

    /**
     * 获取材料名称。
     *
     * @return 材料名称
     */
    public String getName() { return name; }

    /**
     * 判断材料是否必需。
     *
     * @return 必需时返回 {@code true}
     */
    public boolean isRequired() { return required; }
}
