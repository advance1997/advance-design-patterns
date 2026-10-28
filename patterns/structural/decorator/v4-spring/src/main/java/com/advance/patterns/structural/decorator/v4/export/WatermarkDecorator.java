package com.advance.patterns.structural.decorator.v4.export;

import com.advance.patterns.structural.decorator.v4.domain.MaterialBundle;

/**
 * 在内层结果上追加教学水印，不修改基础导出器和其他装饰器。
 *
 * @author advance
 * @since 2026-10-28 15:30:17
 */
public class WatermarkDecorator extends MaterialExporterDecorator {

    /**
     * 将本层增强包装在指定组件之外。
     *
     * @param delegate 基础导出器或其他装饰器
     */
    public WatermarkDecorator(MaterialExporter delegate) {
        super(delegate);
    }

    /**
     * 先委托内层导出，再追加教学水印；打印进入与返回路径。
     *
     * @param bundle 已归集的虚构材料快照
     * @return 带教学水印的清单文本
     */
    @Override
    public String export(MaterialBundle bundle) {
        System.out.println("[装饰器/v4] WatermarkDecorator.export ---> 进入本层，先调用内层组件");
        String text = super.export(bundle);
        System.out.println("[装饰器/v4] WatermarkDecorator.export ---> 内层返回，追加教学水印");
        return text + "\n水印：内部教学材料，禁止外传。";
    }
}
