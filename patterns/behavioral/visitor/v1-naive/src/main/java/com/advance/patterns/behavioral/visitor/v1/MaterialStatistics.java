package com.advance.patterns.behavioral.visitor.v1;

/**
 * 保存一次诉讼材料统计的可变结果。
 *
 * <p>该对象由单次统计调用创建并由材料节点逐步累加，不应跨请求共享。</p>
 *
 * @author advance
 * @since 2026-10-30 00:13:24
 */
public class MaterialStatistics {
    /** 统计到的目录数量。 */
    private int folderCount;
    /** 统计到的 PDF 文件数量。 */
    private int pdfCount;
    /** 统计到的录音文件数量。 */
    private int audioCount;
    /** 所有文件的总大小，单位为字节。 */
    private long totalSize;

    /**
     * 累加一个目录节点。
     */
    public void addFolder() {
        folderCount++;
    }

    /**
     * 累加一个 PDF 文件及其大小。
     *
     * @param size 文件大小，单位为字节
     */
    public void addPdf(long size) {
        pdfCount++;
        totalSize += size;
    }

    /**
     * 累加一个录音文件及其大小。
     *
     * @param size 文件大小，单位为字节
     */
    public void addAudio(long size) {
        audioCount++;
        totalSize += size;
    }

    /**
     * 获取目录数量。
     *
     * @return 目录数量
     */
    public int getFolderCount() { return folderCount; }

    /**
     * 获取 PDF 文件数量。
     *
     * @return PDF 文件数量
     */
    public int getPdfCount() { return pdfCount; }

    /**
     * 获取录音文件数量。
     *
     * @return 录音文件数量
     */
    public int getAudioCount() { return audioCount; }

    /**
     * 获取全部文件的总大小。
     *
     * @return 总大小，单位为字节
     */
    public long getTotalSize() { return totalSize; }
}
