package com.advance.patterns.behavioral.visitor.v2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 只保存名称与子节点的材料目录数据对象。
 *
 * @author advance
 * @since 2026-10-30 00:13:24
 */
public class MaterialFolder implements MaterialNode {
    /** 目录展示名称。 */
    private final String name;
    /** 目录中的材料或子目录。 */
    private final List<MaterialNode> children = new ArrayList<MaterialNode>();

    /**
     * 创建目录数据对象。
     *
     * @param name 目录名称
     */
    public MaterialFolder(String name) { this.name = name; }

    /**
     * 添加材料或子目录。
     *
     * @param child 要加入的材料节点
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
}
