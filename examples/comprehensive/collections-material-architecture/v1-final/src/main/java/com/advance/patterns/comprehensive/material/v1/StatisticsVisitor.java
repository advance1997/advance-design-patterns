package com.advance.patterns.comprehensive.material.v1;

/** 统计目录数、文件类型数量和总字节数。 @author advance @since 2026-10-30 12:00:00 */
public class StatisticsVisitor implements MaterialVisitor {
    private int folderCount; private int pdfCount; private int audioCount; private long totalBytes;
    @Override public void visit(MaterialFolder folder) { folderCount++; }
    @Override public void visit(MaterialFile file) {
        if (file.getName().toLowerCase().endsWith(".pdf")) { pdfCount++; }
        if (file.getName().toLowerCase().endsWith(".mp3")) { audioCount++; }
        totalBytes += file.getSize();
    }
    /** @return 目录数量（含根目录） */ public int getFolderCount() { return folderCount; }
    /** @return PDF 文件数 */ public int getPdfCount() { return pdfCount; }
    /** @return 录音文件数 */ public int getAudioCount() { return audioCount; }
    /** @return 文件总大小（字节） */ public long getTotalBytes() { return totalBytes; }
}
