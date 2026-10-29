package com.advance.patterns.behavioral.visitor.v3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 组合结构中的目录元素，接收访问者并递归将访问转发给子节点。
 *
 * @author advance
 * @since 2026-10-30 00:13:24
 */
public class MaterialFolder implements MaterialNode {
    /** 目录展示名称。 */
    private final String name;
    /** 当前目录的直接子节点。 */
    private final List<MaterialNode> children = new ArrayList<MaterialNode>();

    /**
     * 创建材料目录。
     *
     * @param name 目录名称
     */
    public MaterialFolder(String name) { this.name = name; }

    /**
     * 添加材料或子目录。
     *
     * @param child 要加入目录的节点
     */
    public void add(MaterialNode child) { children.add(child); }

    /**
     * 获取目录名称。
     *
     * @return 目录名称
     */
    public String getName() { return name; }

    /**
     * 获取子节点的只读视图。
     *
     * @return 子节点只读列表
     */
    public List<MaterialNode> getChildren() { return Collections.unmodifiableList(children); }

    /**
     * 先访问当前目录，再使用 List 的迭代器逐个递归访问子节点。
     *
     * @param visitor 本次操作的访问者
     */
    @Override
    public void accept(MaterialVisitor visitor) {
        System.out.println("[访问者/v3] MaterialFolder.accept ---> 将目录交给 "
                + visitor.getClass().getSimpleName() + "，目录名：" + name);
        visitor.visit(this);
        for (MaterialNode child : children) {
            child.accept(visitor);
        }
    }
}
