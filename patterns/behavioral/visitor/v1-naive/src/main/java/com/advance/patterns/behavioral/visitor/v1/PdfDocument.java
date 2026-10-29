package com.advance.patterns.behavioral.visitor.v1;

import java.util.List;

/**
 * PDF 材料元素基线类，将 PDF 统计与签名校验逻辑直接放在数据对象中。
 *
 * @author advance
 * @since 2026-10-30 00:13:24
 */
public class PdfDocument implements MaterialNode {
    /** PDF 文件名称。 */
    private final String name;
    /** PDF 文件大小，单位为字节。 */
    private final long size;
    /** 是否要求签名。 */
    private final boolean requiresSignature;
    /** 当前是否已签名。 */
    private final boolean signed;

    /**
     * 创建虚构 PDF 材料。
     *
     * @param name 文件名称
     * @param size 文件大小，单位为字节
     * @param requiresSignature 是否要求签名
     * @param signed 当前是否已签名
     */
    public PdfDocument(String name, long size, boolean requiresSignature, boolean signed) {
        this.name = name;
        this.size = size;
        this.requiresSignature = requiresSignature;
        this.signed = signed;
    }

    /**
     * 获取文件名称。
     *
     * @return 文件名称
     */
    public String getName() { return name; }

    /**
     * 获取文件大小。
     *
     * @return 文件大小，单位为字节
     */
    public long getSize() { return size; }

    /**
     * 将当前 PDF 计入统计结果。
     *
     * @param statistics 本次统计使用的可变结果对象
     */
    @Override
    public void collectStatistics(MaterialStatistics statistics) {
        System.out.println("[访问者/v1] PdfDocument.collectStatistics ---> 统计 PDF：" + name);
        statistics.addPdf(size);
    }

    /**
     * 检查当前 PDF 是否满足示例中的签名要求。
     *
     * @param issues 本次校验发现的问题集合
     */
    @Override
    public void validate(List<String> issues) {
        System.out.println("[访问者/v1] PdfDocument.validate ---> 校验 PDF：" + name);
        if (requiresSignature && !signed) {
            issues.add("PDF缺少必要签名：" + name);
        }
    }
}
