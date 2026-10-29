package com.advance.patterns.behavioral.visitor.v2;

/**
 * 仅保存 PDF 材料属性的数据对象，统计和校验由独立服务承担。
 *
 * @author advance
 * @since 2026-10-30 00:13:24
 */
public class PdfDocument implements MaterialNode {
    /** 文件名称。 */
    private final String name;
    /** 文件大小，单位为字节。 */
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
     * 判断文件是否要求签名。
     *
     * @return 要求签名时返回 true
     */
    public boolean isRequiresSignature() { return requiresSignature; }

    /**
     * 判断文件是否已签名。
     *
     * @return 已签名时返回 true
     */
    public boolean isSigned() { return signed; }
}
