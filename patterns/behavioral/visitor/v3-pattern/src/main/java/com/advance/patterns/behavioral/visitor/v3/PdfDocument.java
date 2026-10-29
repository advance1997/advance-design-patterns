package com.advance.patterns.behavioral.visitor.v3;

/**
 * PDF 叶子元素，只保存材料数据并通过 accept 将操作分派给访问者。
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
     * 获取 PDF 名称。
     *
     * @return 文件名称
     */
    public String getName() { return name; }

    /**
     * 获取 PDF 文件大小。
     *
     * @return 文件大小，单位为字节
     */
    public long getSize() { return size; }

    /**
     * 判断该 PDF 是否要求签名。
     *
     * @return 要求签名时返回 true
     */
    public boolean isRequiresSignature() { return requiresSignature; }

    /**
     * 判断该 PDF 是否已签名。
     *
     * @return 已签名时返回 true
     */
    public boolean isSigned() { return signed; }

    /**
     * 将当前节点静态类型作为重载参数传给访问者。
     *
     * @param visitor 本次操作的访问者
     */
    @Override
    public void accept(MaterialVisitor visitor) {
        System.out.println("[访问者/v3] PdfDocument.accept ---> 调用 visit(PdfDocument)：" + name);
        visitor.visit(this);
    }
}
