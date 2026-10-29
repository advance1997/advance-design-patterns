package com.advance.patterns.behavioral.visitor.v3;

import org.junit.jupiter.api.Test;

/**
 * 在同一棵材料树上依次执行统计访问者和校验访问者。
 *
 * @author advance
 * @since 2026-10-30 00:13:24
 */
public class VisitorPatternTest {
    /**
     * 输入课程中的诉讼材料树，依次传入两个访问者。
     * 期望 StatisticsVisitor 输出目录 3、PDF 2、录音 2、总大小 7680 字节；
     * 期望 ValidationVisitor 报告借款协议.pdf 缺少签名和外呼录音B.mp3 不可访问。
     */
    @Test
    public void shouldApplyDifferentVisitorsToTheSameMaterialTree() {
        MaterialFolder root = createMaterialTree();

        System.out.println("期望：root.accept(statisticsVisitor) 统计目录3、PDF2、录音2、总大小7680字节");
        StatisticsVisitor statisticsVisitor = new StatisticsVisitor();
        root.accept(statisticsVisitor);
        statisticsVisitor.printResult();

        System.out.println("\n期望：root.accept(validationVisitor) 报告借款协议签名缺失和外呼录音B不可访问");
        ValidationVisitor validationVisitor = new ValidationVisitor();
        root.accept(validationVisitor);
        validationVisitor.printResult();
    }

    /**
     * 构造课程中的虚构诉讼材料树，供多个访问者复用。
     *
     * @return 诉讼材料根目录
     */
    private MaterialFolder createMaterialTree() {
        MaterialFolder root = new MaterialFolder("诉讼材料包");
        MaterialFolder contracts = new MaterialFolder("合同材料");
        MaterialFolder recordings = new MaterialFolder("催收材料");
        contracts.add(new PdfDocument("借款合同.pdf", 1024L, true, true));
        contracts.add(new PdfDocument("借款协议.pdf", 2048L, true, false));
        recordings.add(new AudioRecording("外呼录音A.mp3", 4096L, true));
        recordings.add(new AudioRecording("外呼录音B.mp3", 512L, false));
        root.add(contracts);
        root.add(recordings);
        return root;
    }
}
