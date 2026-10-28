package com.advance.patterns.structural.decorator.v3.export;

import com.advance.patterns.structural.decorator.v3.domain.MaterialBundle;

/**
 * 只负责基础清单，不了解复核说明、水印或任何装饰组合。
 *
 * @author advance
 * @since 2026-10-28 15:30:17
 */
public class PlainMaterialExporter implements MaterialExporter {

    /**
     * 按输入顺序列出材料名称。
     *
     * @param bundle 已归集的虚构材料快照
     * @return 不带附加说明的基础清单
     */
    @Override
    public String export(MaterialBundle bundle) {
        System.out.println("[装饰器/v3] PlainMaterialExporter.export ---> 生成基础材料清单");
        StringBuilder text = new StringBuilder("案件：" + bundle.getCaseId() + "\n材料清单：");
        for (String name : bundle.getMaterialNames()) {
            text.append("\n- ").append(name);
        }
        return text.toString();
    }
}
