package com.advance.patterns.structural.decorator.v1;

import com.advance.patterns.structural.decorator.v1.domain.MaterialBundle;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import com.advance.patterns.structural.decorator.v1.service.MaterialExportService;

/**
 * 只围绕装饰器演进与组合路径进行打印式验证；实际结果由读者对照各方法的期望说明。
 *
 * @author advance
 * @since 2026-10-28 15:30:17
 */
class MaterialExportTest {

    /**
     * 输入 DEMO-001 及借款合同、还款流水两项模拟材料。
     * 期望：只输出案件编号和两项材料；不出现复核说明与水印，路径仅为 MaterialExportService.export。
     */
    @Test
    void exportPlain() {
        MaterialBundle bundle = new MaterialBundle("DEMO-001", Arrays.asList("借款合同（模拟）", "还款流水（模拟）"));
        MaterialExportService service = new MaterialExportService();
        System.out.println("实际清单：\n" + service.export(bundle, false, false));
    }

    /**
     * 输入 DEMO-001 及借款合同、还款流水两项模拟材料。
     * 期望：基础清单之后依次出现复核说明、水印；全部路径位于 MaterialExportService.export。
     */
    @Test
    void exportWithBothEnhancements() {
        MaterialBundle bundle = new MaterialBundle("DEMO-001", Arrays.asList("借款合同（模拟）", "还款流水（模拟）"));
        MaterialExportService service = new MaterialExportService();
        System.out.println("实际清单：\n" + service.export(bundle, true, true));
    }
}
