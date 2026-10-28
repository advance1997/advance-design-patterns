package com.advance.patterns.structural.composite.v3;

import com.advance.patterns.structural.composite.v3.component.MaterialNode;
import com.advance.patterns.structural.composite.v3.composite.MaterialGroup;
import com.advance.patterns.structural.composite.v3.leaf.MaterialLeaf;
import org.junit.jupiter.api.Test;

/**
 * 组合模式统一操作叶子材料与多层目录。
 *
 * @author advance
 * @since 2026-10-28 00:00:00
 */
public class MaterialTreeTest {
    /**
     * 期望输出：两层目录中的材料树，并统计 2 项必需材料。
     */
    @Test
    public void shouldTreatLeafAndGroupUniformly() {
        MaterialGroup root = new MaterialGroup("诉讼案件材料");
        MaterialGroup identity = new MaterialGroup("身份材料");
        identity.add(new MaterialLeaf("主体身份证明", true));
        MaterialGroup fulfillment = new MaterialGroup("履约材料");
        fulfillment.add(new MaterialLeaf("借款合同", true));
        fulfillment.add(new MaterialLeaf("沟通记录", false));
        root.add(identity);
        root.add(fulfillment);

        MaterialNode caseMaterials = root;
        System.out.println("期望：调用方只依赖 MaterialNode，递归输出叶子和组合目录");
        caseMaterials.print("");
        System.out.println("期望必需材料数量：2");
        System.out.println("实际必需材料数量：" + caseMaterials.countRequiredMaterials());
    }
}
