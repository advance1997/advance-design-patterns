package com.advance.patterns.structural.composite.v2.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * v2 目录负责递归打印子目录，但仍与材料叶子类型不同。
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
     * 递归输出当前目录中的内容。
     *
     * @param indent 当前缩进
     */
    public void print(String indent) {
        System.out.println(indent + "目录：" + name);
        for (Object child : children) {
            if (child instanceof MaterialGroup) {
                ((MaterialGroup) child).print(indent + "  ");
            } else {
                ((MaterialLeaf) child).print(indent + "  ");
            }
        }
    }
}
