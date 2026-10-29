package com.advance.patterns.behavioral.visitor.v2;

import java.util.List;

/**
 * 通过具体类型分支递归校验材料的重构版服务。
 *
 * <p>该服务与统计服务各自维护节点类型分支，节点类型增长会同时扩大多个操作服务。</p>
 *
 * @author advance
 * @since 2026-10-30 00:13:24
 */
public class MaterialValidationService {
    /**
     * 校验给定节点及其全部后代。
     *
     * @param node 当前材料节点
     * @param issues 收集校验问题的列表
     * @throws IllegalArgumentException 节点不是本版本支持的目录、PDF 或录音类型时抛出
     */
    public void validate(MaterialNode node, List<String> issues) {
        if (node instanceof MaterialFolder) {
            MaterialFolder folder = (MaterialFolder) node;
            System.out.println("[访问者/v2] MaterialValidationService.validate ---> 遍历目录：" + folder.getName());
            for (MaterialNode child : folder.getChildren()) {
                validate(child, issues);
            }
        } else if (node instanceof PdfDocument) {
            PdfDocument pdf = (PdfDocument) node;
            System.out.println("[访问者/v2] MaterialValidationService.validate ---> 校验 PDF：" + pdf.getName());
            if (pdf.isRequiresSignature() && !pdf.isSigned()) {
                issues.add("PDF缺少必要签名：" + pdf.getName());
            }
        } else if (node instanceof AudioRecording) {
            AudioRecording audio = (AudioRecording) node;
            System.out.println("[访问者/v2] MaterialValidationService.validate ---> 校验录音：" + audio.getName());
            if (!audio.isAccessible()) {
                issues.add("录音不可访问：" + audio.getName());
            }
        } else {
            throw new IllegalArgumentException("不支持的材料节点类型：" + node.getClass().getName());
        }
    }
}
