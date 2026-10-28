package com.advance.patterns.structural.decorator.v3;

import com.advance.patterns.structural.decorator.v3.domain.MaterialBundle;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import com.advance.patterns.structural.decorator.v3.export.MaterialExporter;
import com.advance.patterns.structural.decorator.v3.export.PlainMaterialExporter;
import com.advance.patterns.structural.decorator.v3.export.ReviewNoteDecorator;
import com.advance.patterns.structural.decorator.v3.export.WatermarkDecorator;

/**
 * 只围绕装饰器演进与组合路径进行打印式验证；实际结果由读者对照各方法的期望说明。
 *
 * @author advance
 * @since 2026-10-28 15:30:17
 */
class MaterialExportTest {

    /**
     * 输入 DEMO-001 及借款合同、还款流水两项模拟材料。
     * 期望：仅输出基础清单，路径为 PlainMaterialExporter.export。
     */
    @Test
    void exportPlain() {
        MaterialBundle bundle = new MaterialBundle("DEMO-001", Arrays.asList("借款合同（模拟）", "还款流水（模拟）"));
        MaterialExporter exporter = new PlainMaterialExporter();
        System.out.println("实际清单：\n" + exporter.export(bundle));
    }

    /**
     * 输入 DEMO-001 及借款合同、还款流水两项模拟材料。
     * 期望：进入 ReviewNoteDecorator 后调用 PlainMaterialExporter；返回时追加复核说明，不出现水印。
     */
    @Test
    void exportWithOneDecorator() {
        MaterialBundle bundle = new MaterialBundle("DEMO-001", Arrays.asList("借款合同（模拟）", "还款流水（模拟）"));
        MaterialExporter exporter = new ReviewNoteDecorator(new PlainMaterialExporter());
        System.out.println("实际清单：\n" + exporter.export(bundle));
    }

    /**
     * 输入 DEMO-001 及借款合同、还款流水两项模拟材料。
     * 期望：进入顺序 WatermarkDecorator → ReviewNoteDecorator → PlainMaterialExporter；结果为基础清单、复核说明、水印。
     */
    @Test
    void exportWithTwoDecorators() {
        MaterialBundle bundle = new MaterialBundle("DEMO-001", Arrays.asList("借款合同（模拟）", "还款流水（模拟）"));
        MaterialExporter exporter = new WatermarkDecorator(
                new ReviewNoteDecorator(new PlainMaterialExporter()));
        System.out.println("实际清单：\n" + exporter.export(bundle));
    }

    /**
     * 输入 DEMO-001 及借款合同、还款流水两项模拟材料。
     * 期望：进入顺序 ReviewNoteDecorator → WatermarkDecorator → PlainMaterialExporter；结果为基础清单、水印、复核说明。
     */
    @Test
    void reverseDecoratorOrder() {
        MaterialBundle bundle = new MaterialBundle("DEMO-001", Arrays.asList("借款合同（模拟）", "还款流水（模拟）"));
        MaterialExporter exporter = new ReviewNoteDecorator(
                new WatermarkDecorator(new PlainMaterialExporter()));
        System.out.println("实际清单：\n" + exporter.export(bundle));
    }
}
