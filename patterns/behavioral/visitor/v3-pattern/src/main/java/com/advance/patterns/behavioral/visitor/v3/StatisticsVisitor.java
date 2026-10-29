package com.advance.patterns.behavioral.visitor.v3;

/**
 * 统计材料目录、PDF、录音数量及总字节数的访问者。
 *
 * <p>访问者在遍历期间保存可变计数，因此每个独立统计任务应创建新实例，不应将其作为并发共享单例。</p>
 *
 * @author advance
 * @since 2026-10-30 00:13:24
 */
public class StatisticsVisitor implements MaterialVisitor {
    /** 访问到的目录数量。 */
    private int folderCount;
    /** 访问到的 PDF 数量。 */
    private int pdfCount;
    /** 访问到的录音数量。 */
    private int audioCount;
    /** 所有文件大小之和，单位为字节。 */
    private long totalSize;

    /**
     * 统计一个目录元素。
     *
     * @param folder 当前目录
     */
    @Override
    public void visit(MaterialFolder folder) {
        System.out.println("[访问者/v3] StatisticsVisitor.visit(MaterialFolder) ---> 统计目录：" + folder.getName());
        folderCount++;
    }

    /**
     * 统计一个 PDF 及其大小。
     *
     * @param pdf 当前 PDF
     */
    @Override
    public void visit(PdfDocument pdf) {
        System.out.println("[访问者/v3] StatisticsVisitor.visit(PdfDocument) ---> 统计 PDF：" + pdf.getName());
        pdfCount++;
        totalSize += pdf.getSize();
    }

    /**
     * 统计一个录音及其大小。
     *
     * @param audio 当前录音
     */
    @Override
    public void visit(AudioRecording audio) {
        System.out.println("[访问者/v3] StatisticsVisitor.visit(AudioRecording) ---> 统计录音：" + audio.getName());
        audioCount++;
        totalSize += audio.getSize();
    }

    /**
     * 打印当前访问者累计的统计结果。
     */
    public void printResult() {
        System.out.println("目录数量：" + folderCount);
        System.out.println("PDF数量：" + pdfCount);
        System.out.println("录音数量：" + audioCount);
        System.out.println("总大小：" + totalSize + "字节");
    }
}
