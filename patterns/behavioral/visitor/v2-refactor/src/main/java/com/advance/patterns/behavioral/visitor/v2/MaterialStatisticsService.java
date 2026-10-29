package com.advance.patterns.behavioral.visitor.v2;

/**
 * 通过具体类型分支递归统计材料节点的重构版服务。
 *
 * <p>统计逻辑已从元素类移出，但节点类型增加时要修改此服务；其他服务也会重复相同类型判断。</p>
 *
 * @author advance
 * @since 2026-10-30 00:13:24
 */
public class MaterialStatisticsService {
    /**
     * 统计给定节点及其所有子节点。
     *
     * @param node 当前材料节点
     * @param statistics 本次统计使用的结果对象
     * @throws IllegalArgumentException 节点不是本版本支持的目录、PDF 或录音类型时抛出
     */
    public void collect(MaterialNode node, MaterialStatistics statistics) {
        if (node instanceof MaterialFolder) {
            MaterialFolder folder = (MaterialFolder) node;
            System.out.println("[访问者/v2] MaterialStatisticsService.collect ---> 统计目录：" + folder.getName());
            statistics.addFolder();
            for (MaterialNode child : folder.getChildren()) {
                collect(child, statistics);
            }
        } else if (node instanceof PdfDocument) {
            PdfDocument pdf = (PdfDocument) node;
            System.out.println("[访问者/v2] MaterialStatisticsService.collect ---> 统计 PDF：" + pdf.getName());
            statistics.addPdf(pdf.getSize());
        } else if (node instanceof AudioRecording) {
            AudioRecording audio = (AudioRecording) node;
            System.out.println("[访问者/v2] MaterialStatisticsService.collect ---> 统计录音：" + audio.getName());
            statistics.addAudio(audio.getSize());
        } else {
            throw new IllegalArgumentException("不支持的材料节点类型：" + node.getClass().getName());
        }
    }
}
