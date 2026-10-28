package com.advance.patterns.structural.composite.v3.component;

/**
 * 催收材料目录树中的统一节点抽象。
 *
 * @author advance
 * @since 2026-10-28 00:00:00
 */
public interface MaterialNode {
    /**
     * 按树形结构打印当前节点及其内容。
     *
     * @param indent 当前缩进
     */
    void print(String indent);

    /**
     * 统计当前节点下必需材料的数量。
     *
     * @return 必需材料数量
     */
    int countRequiredMaterials();
}
