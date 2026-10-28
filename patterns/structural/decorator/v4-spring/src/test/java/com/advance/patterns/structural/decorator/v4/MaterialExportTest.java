package com.advance.patterns.structural.decorator.v4;

import com.advance.patterns.structural.decorator.v4.domain.MaterialBundle;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import com.advance.patterns.structural.decorator.v4.config.MaterialExportConfig;
import com.advance.patterns.structural.decorator.v4.service.MaterialExportService;
import com.advance.patterns.structural.decorator.v4.export.MaterialExporter;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 只围绕装饰器演进与组合路径进行打印式验证；实际结果由读者对照各方法的期望说明。
 *
 * @author advance
 * @since 2026-10-28 15:30:17
 */
class MaterialExportTest {

    /**
     * 输入 DEMO-001 及借款合同、还款流水两项模拟材料。
     * 期望：业务服务调用容器注入的链；依次进入 WatermarkDecorator、ReviewNoteDecorator、PlainMaterialExporter；结果为基础清单、复核说明、水印。
     */
    @Test
    void exportThroughSpringService() {
        try (AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MaterialExportConfig.class)) {
            MaterialBundle bundle = new MaterialBundle("DEMO-001", Arrays.asList("借款合同（模拟）", "还款流水（模拟）"));
            MaterialExportService service = context.getBean(MaterialExportService.class);
            System.out.println("实际清单：\n" + service.export(bundle));
        }
    }

    /**
     * 输入 DEMO-001 及借款合同、还款流水两项模拟材料。
     * 期望：按 Bean 名选择 reviewExporter，只调用复核装饰器和基础组件；结果有复核说明、无水印。
     */
    @Test
    void selectReviewChain() {
        try (AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MaterialExportConfig.class)) {
            MaterialBundle bundle = new MaterialBundle("DEMO-001", Arrays.asList("借款合同（模拟）", "还款流水（模拟）"));
            MaterialExporter exporter = context.getBean("reviewExporter", MaterialExporter.class);
            System.out.println("实际清单：\n" + exporter.export(bundle));
        }
    }
}
