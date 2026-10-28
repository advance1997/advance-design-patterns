package com.advance.patterns.structural.decorator.v4.config;

import com.advance.patterns.structural.decorator.v4.export.MaterialExporter;
import com.advance.patterns.structural.decorator.v4.export.PlainMaterialExporter;
import com.advance.patterns.structural.decorator.v4.export.ReviewNoteDecorator;
import com.advance.patterns.structural.decorator.v4.export.WatermarkDecorator;
import com.advance.patterns.structural.decorator.v4.service.MaterialExportService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 由 Spring 显式装配两条材料清单导出链；不用 AOP，也不依赖 Bean 扫描顺序。
 *
 * @author advance
 * @since 2026-10-28 15:30:17
 */
@Configuration
public class MaterialExportConfig {

    /**
     * 提供无状态基础组件，可供两条链共同委托。
     *
     * @return 基础材料清单导出器
     */
    @Bean
    public MaterialExporter plainExporter() {
        return new PlainMaterialExporter();
    }

    /**
     * 为内部人工复核场景装配基础清单与复核说明。
     *
     * @param plainExporter 按 Bean 名称注入的基础组件
     * @return 内部复核导出链
     */
    @Bean
    public MaterialExporter reviewExporter(
            @Qualifier("plainExporter") MaterialExporter plainExporter) {
        return new ReviewNoteDecorator(plainExporter);
    }

    /**
     * 在复核链外再包装水印，演示同接口组件的递归组合。
     *
     * @param reviewExporter 按 Bean 名称注入的复核链
     * @return 带教学水印的复核导出链
     */
    @Bean
    public MaterialExporter watermarkedExporter(
            @Qualifier("reviewExporter") MaterialExporter reviewExporter) {
        return new WatermarkDecorator(reviewExporter);
    }

    /**
     * 向业务服务注入完整链，业务服务无需了解具体装饰器。
     *
     * @param exporter 明确选中的带水印导出链
     * @return 材料清单导出业务服务
     */
    @Bean
    public MaterialExportService materialExportService(
            @Qualifier("watermarkedExporter") MaterialExporter exporter) {
        return new MaterialExportService(exporter);
    }
}
