package com.advance.patterns.structural.composite.v1.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * v1 中与材料叶子分离的目录对象。
 *
 * @author advance
 * @since 2026-10-28 00:00:00
 */
public class MaterialGroup {
    private final String name;
    private final List<Object> children = new ArrayList<Object>();

    /**
     * 创建材料目录。
     *
     * @param name 目录名称
     */
    public MaterialGroup(String name) { this.name = name; }

    /**
     * 添加材料或子目录。
     *
     * @param child 材料叶子或材料目录
     */
    public void add(Object child) { children.add(child); }

    /**
     * 获取目录名称。
     *
     * @return 目录名称
     */
    public String getName() { return name; }

    /**
     * 获取目录中的节点。
     *
     * @return 子节点列表
     */
    public List<Object> getChildren() { return children; }
}
