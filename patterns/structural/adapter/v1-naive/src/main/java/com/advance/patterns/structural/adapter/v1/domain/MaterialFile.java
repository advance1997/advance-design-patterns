package com.advance.patterns.structural.adapter.v1.domain;

/**
 * 内部统一材料结果，只保存虚构文件标识；found 表示是否找到合同，不代表审核通过。
 *
 * @author advance
 * @since 2026-10-28 16:08:56
 */
public final class MaterialFile {

    /** 内部案件编号。 */
    private final long caseId;

    /** 是否找到合同材料。 */
    private final boolean found;

    /** 虚构文件标识；未找到时为空字符串，不含真实下载地址。 */
    private final String fileKey;

    /**
     * 创建平台无关的合同材料结果。
     *
     * @param caseId 内部案件编号
     * @param found 是否找到合同
     * @param fileKey 找到时的虚构文件标识，未找到时为空字符串
     */
    public MaterialFile(long caseId, boolean found, String fileKey) {
        this.caseId = caseId;
        this.found = found;
        this.fileKey = fileKey;
    }

    /**
     * 获取内部案件编号。
     *
     * @return 案件编号
     */
    public long getCaseId() {
        return caseId;
    }

    /**
     * 判断是否找到合同材料。
     *
     * @return 找到为 true，未找到为 false
     */
    public boolean isFound() {
        return found;
    }

    /**
     * 获取内部统一文件标识。
     *
     * @return 文件标识或未找到时的空字符串
     */
    public String getFileKey() {
        return fileKey;
    }

    /**
     * 输出统一字段，便于直接比较两个平台的转换结果。
     *
     * @return 带中文说明和英文字段名的结果
     */
    @Override
    public String toString() {
        return "案件 caseId=" + caseId + "，是否找到 found=" + found + "，文件标识 fileKey=" + fileKey;
    }
}
