package com.advance.patterns.behavioral.visitor.v3;

/**
 * 定义访问诉讼材料目录、PDF 与录音的操作入口。
 *
 * <p>新增元素类型时需要增加对应重载并适配具体访问者；稳定元素结构上新增操作只需新增访问者实现。</p>
 *
 * @author advance
 * @since 2026-10-30 00:13:24
 */
public interface MaterialVisitor {
    /**
     * 访问材料目录。
     *
     * @param folder 当前目录元素
     */
    void visit(MaterialFolder folder);

    /**
     * 访问 PDF 材料。
     *
     * @param pdf 当前 PDF 元素
     */
    void visit(PdfDocument pdf);

    /**
     * 访问录音材料。
     *
     * @param audio 当前录音元素
     */
    void visit(AudioRecording audio);
}
