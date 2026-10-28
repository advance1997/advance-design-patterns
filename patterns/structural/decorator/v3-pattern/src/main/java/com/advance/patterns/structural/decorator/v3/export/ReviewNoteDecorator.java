package com.advance.patterns.structural.decorator.v3.export;

import com.advance.patterns.structural.decorator.v3.domain.MaterialBundle;

/**
 * 在内层结果上追加人工复核说明，不修改基础导出器和其他装饰器。
 *
 * @author advance
 * @since 2026-10-28 15:30:17
 */
public class ReviewNoteDecorator extends MaterialExporterDecorator {

    /**
     * 将本层增强包装在指定组件之外。
     *
     * @param delegate 基础导出器或其他装饰器
     */
    public ReviewNoteDecorator(MaterialExporter delegate) {
        super(delegate);
    }

    /**
     * 先委托内层导出，再追加人工复核说明；打印进入与返回路径。
     *
     * @param bundle 已归集的虚构材料快照
     * @return 带人工复核说明的清单文本
     */
    @Override
    public String export(MaterialBundle bundle) {
        System.out.println("[装饰器/v3] ReviewNoteDecorator.export ---> 进入本层，先调用内层组件");
        String text = super.export(bundle);
        System.out.println("[装饰器/v3] ReviewNoteDecorator.export ---> 内层返回，追加人工复核说明");
        return text + "\n复核说明：材料仅供人工复核，不代表审核通过。";
    }
}
