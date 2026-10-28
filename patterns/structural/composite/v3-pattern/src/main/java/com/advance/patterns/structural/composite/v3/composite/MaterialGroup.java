package com.advance.patterns.structural.composite.v3.composite;

import com.advance.patterns.structural.composite.v3.component.MaterialNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 组合节点既可作为目录被统一处理，也可包含材料或其他目录。
 *
 * @author advance
 * @since 2026-10-28 00:00:00
 */
public class MaterialGroup implements MaterialNode {
    private final String name;
    private final List<MaterialNode> children = new ArrayList<MaterialNode>();

    /**
     * 创建材料目录。
     *
     * @param name 目录名称
     */
    public MaterialGroup(String name) { this.name = name; }

    /**
     * 将材料或子目录加入当前目录。
     *
     * @param child 材料树节点
     */
    public void add(MaterialNode child) { children.add(child); }

    /**
     * 获取当前目录的只读子节点视图。
     *
     * @return 子节点列表
     */
    public List<MaterialNode> getChildren() { return Collections.unmodifiableList(children); }

    @Override
    public void print(String indent) {
        System.out.println(indent + "目录：" + name);
        for (MaterialNode child : children) {
            child.print(indent + "  ");
        }
    }

    @Override
    public int countRequiredMaterials() {
        int count = 0;
        for (MaterialNode child : children) {
            count += child.countRequiredMaterials();
        }
        return count;
    }
}
