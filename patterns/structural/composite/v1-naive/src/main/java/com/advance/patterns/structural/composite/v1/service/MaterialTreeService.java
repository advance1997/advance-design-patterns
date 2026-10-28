package com.advance.patterns.structural.composite.v1.service;

import com.advance.patterns.structural.composite.v1.domain.MaterialGroup;
import com.advance.patterns.structural.composite.v1.domain.MaterialLeaf;

/**
 * v1 由服务识别材料叶子和目录并分别递归处理。
 *
 * @author advance
 * @since 2026-10-28 00:00:00
 */
public class MaterialTreeService {
    /**
     * 按树形结构输出材料清单。
     *
     * @param node 材料叶子或目录
     * @param indent 当前缩进
     */
    public void print(Object node, String indent) {
        if (node instanceof MaterialLeaf) {
            MaterialLeaf leaf = (MaterialLeaf) node;
            System.out.println(indent + "材料：" + leaf.getName() + (leaf.isRequired() ? "（必需）" : "（可选）"));
            return;
        }
        MaterialGroup group = (MaterialGroup) node;
        System.out.println(indent + "目录：" + group.getName());
        for (Object child : group.getChildren()) {
            print(child, indent + "  ");
        }
    }
}
