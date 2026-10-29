package com.advance.patterns.behavioral.visitor.v1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 材料目录基线元素，既保存子节点，又递归实现统计和校验操作。
 *
 * @author advance
 * @since 2026-10-30 00:13:24
 */
public class MaterialFolder implements MaterialNode {
    /** 目录展示名称。 */
    private final String name;
    /** 当前目录包含的材料或子目录。 */
    private final List<MaterialNode> children = new ArrayList<MaterialNode>();

    /**
     * 创建材料目录。
     *
     * @param name 目录名称
     */
    public MaterialFolder(String name) { this.name = name; }

    /**
     * 将材料元素或子目录加入当前目录。
     *
     * @param child 待归集的材料节点
     */
    public void add(MaterialNode child) { children.add(child); }

    /**
     * 获取目录名称。
     *
     * @return 目录名称
     */
    public String getName() { return name; }

    /**
     * 获取子节点只读视图，避免外部绕过目录的加入入口修改集合。
     *
     * @return 子节点只读列表
     */
    public List<MaterialNode> getChildren() { return Collections.unmodifiableList(children); }

    /**
     * 将目录本身及其后代递归计入统计结果。
     *
     * @param statistics 本次统计使用的可变结果对象
     */
    @Override
    public void collectStatistics(MaterialStatistics statistics) {
        System.out.println("[访问者/v1] MaterialFolder.collectStatistics ---> 统计目录：" + name);
        statistics.addFolder();
        for (MaterialNode child : children) {
            child.collectStatistics(statistics);
        }
    }

    /**
     * 依次调用每个子节点自身的校验操作。
     *
     * @param issues 本次校验发现的问题集合
     */
    @Override
    public void validate(List<String> issues) {
        System.out.println("[访问者/v1] MaterialFolder.validate ---> 遍历目录：" + name);
        for (MaterialNode child : children) {
            child.validate(issues);
        }
    }
}
