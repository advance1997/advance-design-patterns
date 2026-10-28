package com.advance.patterns.structural.composite.v1;

import com.advance.patterns.structural.composite.v1.domain.MaterialGroup;
import com.advance.patterns.structural.composite.v1.domain.MaterialLeaf;
import com.advance.patterns.structural.composite.v1.service.MaterialTreeService;
import org.junit.jupiter.api.Test;

/**
 * v1 材料目录树打印示例。
 *
 * @author advance
 * @since 2026-10-28 00:00:00
 */
public class MaterialTreeTest {
    /**
     * 期望输出：案件材料目录、身份材料目录及其身份证明材料。
     */
    @Test
    public void shouldPrintNestedMaterialTree() {
        MaterialGroup root = new MaterialGroup("诉讼案件材料");
        MaterialGroup identity = new MaterialGroup("身份材料");
        identity.add(new MaterialLeaf("主体身份证明", true));
        root.add(identity);
        System.out.println("期望：目录递归由服务识别并打印叶子材料");
        new MaterialTreeService().print(root, "");
    }
}
