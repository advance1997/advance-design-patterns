package com.advance.patterns.comprehensive.material.v1;

import java.util.ArrayList;
import java.util.List;

/** 可包含材料文件或子目录的组合节点。 @author advance @since 2026-10-30 12:00:00 */
public class MaterialFolder implements MaterialNode {
    private final String name;
    private final List<MaterialNode> children = new ArrayList<MaterialNode>();
    /** @param name 目录名称 */ public MaterialFolder(String name) { this.name = name; }
    /** @return 目录名称 */ public String getName() { return name; }
    /** @return 直接子节点集合 */ public List<MaterialNode> getChildren() { return children; }
    /** @param child 要加入的文件或子目录 */ public void add(MaterialNode child) { children.add(child); }
    @Override public void accept(MaterialVisitor visitor) { visitor.visit(this); }
}
