package com.advance.patterns.behavioral.visitor.v2;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * 对照两个独立操作服务中的具体类型分支。
 *
 * @author advance
 * @since 2026-10-30 00:13:24
 */
public class VisitorRefactorTest {
    /**
     * 输入含三个目录、两个 PDF 和两段录音的材料树。
     * 期望统计：目录 3、PDF 2、录音 2、总大小 7680 字节；
     * 期望校验问题：借款协议.pdf 缺少必要签名，外呼录音B.mp3 不可访问。
     */
    @Test
    public void shouldRunSeparateServicesWithTypeBranches() {
        MaterialFolder root = createMaterialTree();
        MaterialStatistics statistics = new MaterialStatistics();
        System.out.println("期望：MaterialStatisticsService.collect 按节点类型递归统计，目录3、PDF2、录音2、总大小7680字节");
        new MaterialStatisticsService().collect(root, statistics);
        System.out.println("实际统计：目录=" + statistics.getFolderCount() + "，PDF=" + statistics.getPdfCount()
                + "，录音=" + statistics.getAudioCount() + "，总大小=" + statistics.getTotalSize() + "字节");

        List<String> issues = new ArrayList<String>();
        System.out.println("期望：MaterialValidationService.validate 找出借款协议签名缺失和外呼录音B不可访问");
        new MaterialValidationService().validate(root, issues);
        for (String issue : issues) {
            System.out.println("实际校验问题：" + issue);
        }
    }

    /**
     * 构造课程中的虚构诉讼材料树。
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
