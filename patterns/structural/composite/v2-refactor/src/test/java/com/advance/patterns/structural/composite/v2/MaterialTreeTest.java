package com.advance.patterns.structural.composite.v2;

import com.advance.patterns.structural.composite.v2.domain.MaterialGroup;
import com.advance.patterns.structural.composite.v2.domain.MaterialLeaf;
import org.junit.jupiter.api.Test;

/**
 * v2 材料目录树打印示例。
 *
 * @author advance
 * @since 2026-10-28 00:00:00
 */
public class MaterialTreeTest {
    /**
     * 期望输出：目录递归逻辑进入 MaterialGroup，服务不再承担整棵树遍历。
     */
    @Test
    public void shouldPrintNestedMaterialTree() {
        MaterialGroup root = new MaterialGroup("诉讼案件材料");
        MaterialGroup identity = new MaterialGroup("身份材料");
        identity.add(new MaterialLeaf("主体身份证明", true));
        root.add(identity);
        System.out.println("期望：MaterialGroup 递归打印目录，叶子仍需按类型单独处理");
        root.print("");
    }
}
