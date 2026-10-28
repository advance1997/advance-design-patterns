package com.advance.patterns.structural.decorator.v4.service;

import com.advance.patterns.structural.decorator.v4.domain.MaterialBundle;
import com.advance.patterns.structural.decorator.v4.export.MaterialExporter;

/**
 * 催收材料清单业务入口；只依赖导出接口，由 Spring 决定所注入的装饰组合。
 *
 * @author advance
 * @since 2026-10-28 15:30:17
 */
public class MaterialExportService {

    /** 已由容器装配完成的导出组件。 */
    private final MaterialExporter exporter;

    /**
     * 通过构造方法接收完整导出链。
     *
     * @param exporter 由容器指定的导出组件
     */
    public MaterialExportService(MaterialExporter exporter) {
        this.exporter = exporter;
    }

    /**
     * 导出已归集的材料名称，不执行真实材料读取或外发。
     *
     * @param bundle 虚构材料快照
     * @return 包含所选增强的清单文本
     */
    public String export(MaterialBundle bundle) {
        System.out.println("[装饰器/v4] MaterialExportService.export ---> 调用 Spring 注入的导出链");
        return exporter.export(bundle);
    }
}
