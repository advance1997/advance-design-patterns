package com.advance.patterns.structural.composite.v3.leaf;

import com.advance.patterns.structural.composite.v3.component.MaterialNode;

/**
 * 组合树的叶子节点，表示一项具体催收材料。
 *
 * @author advance
 * @since 2026-10-28 00:00:00
 */
public class MaterialLeaf implements MaterialNode {
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

    @Override
    public void print(String indent) {
        System.out.println(indent + "材料：" + name + (required ? "（必需）" : "（可选）"));
    }

    @Override
    public int countRequiredMaterials() { return required ? 1 : 0; }
}
