package com.advance.patterns.comprehensive.material.v1;

/**
 * 表示材料来源查询的明确结果，区分不存在与暂时无法判断。
 *
 * @author advance
 * @since 2026-10-30 12:00:00
 */
public class FetchResult {
    /** 查询状态。 */
    public enum Status { FOUND, MISSING, ERROR }
    private final Status status;
    private final String fileName;
    private final String folderName;
    private final long size;
    private final String detail;

    /**
     * 创建来源查询结果。
     *
     * @param status FOUND、MISSING 或 ERROR
     * @param fileName 已获取材料的文件名
     * @param folderName 目录名
     * @param size 文件字节数
     * @param detail 结果说明或失败原因
     */
    public FetchResult(Status status, String fileName, String folderName, long size, String detail) {
        this.status = status;
        this.fileName = fileName;
        this.folderName = folderName;
        this.size = size;
        this.detail = detail;
    }

    /** @return 查询状态 */
    public Status getStatus() { return status; }
    /** @return 文件名 */
    public String getFileName() { return fileName; }
    /** @return 目录名 */
    public String getFolderName() { return folderName; }
    /** @return 文件大小，单位为字节 */
    public long getSize() { return size; }
    /** @return 来源说明 */
    public String getDetail() { return detail; }
}
