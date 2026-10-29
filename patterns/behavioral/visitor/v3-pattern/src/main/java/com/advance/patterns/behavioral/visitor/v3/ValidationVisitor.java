package com.advance.patterns.behavioral.visitor.v3;

import java.util.ArrayList;
import java.util.List;

/**
 * 校验诉讼材料示例规则的访问者：要求签名的 PDF 必须已签名，录音必须可访问。
 *
 * <p>这些规则仅为学习双重分派的虚构示例，不代表真实司法材料的合规标准。</p>
 *
 * @author advance
 * @since 2026-10-30 00:13:24
 */
public class ValidationVisitor implements MaterialVisitor {
    /** 本次遍历收集到的问题。 */
    private final List<String> issues = new ArrayList<String>();

    /**
     * 示例中不对目录本身添加校验规则。
     *
     * @param folder 当前目录
     */
    @Override
    public void visit(MaterialFolder folder) {
        System.out.println("[访问者/v3] ValidationVisitor.visit(MaterialFolder) ---> 目录无需单独校验："
                + folder.getName());
    }

    /**
     * 检查 PDF 的签名要求。
     *
     * @param pdf 当前 PDF
     */
    @Override
    public void visit(PdfDocument pdf) {
        System.out.println("[访问者/v3] ValidationVisitor.visit(PdfDocument) ---> 校验 PDF：" + pdf.getName());
        if (pdf.isRequiresSignature() && !pdf.isSigned()) {
            issues.add("PDF缺少必要签名：" + pdf.getName());
        }
    }

    /**
     * 检查录音是否可访问。
     *
     * @param audio 当前录音
     */
    @Override
    public void visit(AudioRecording audio) {
        System.out.println("[访问者/v3] ValidationVisitor.visit(AudioRecording) ---> 校验录音：" + audio.getName());
        if (!audio.isAccessible()) {
            issues.add("录音不可访问：" + audio.getName());
        }
    }

    /**
     * 打印全部校验问题；无问题时明确打印通过结果。
     */
    public void printResult() {
        if (issues.isEmpty()) {
            System.out.println("全部材料校验通过");
            return;
        }
        for (String issue : issues) {
            System.out.println(issue);
        }
    }
}
