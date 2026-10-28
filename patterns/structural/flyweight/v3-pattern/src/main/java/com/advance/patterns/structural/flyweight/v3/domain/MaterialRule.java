package com.advance.patterns.structural.flyweight.v3.domain;

/**
 * 一条催收材料规则；v1/v2故意允许按案件复制，v3将其作为共享享元。
 *
 * @author advance
 * @since 2026-10-28 21:22:00
 */
public class MaterialRule {
    /** 材料编码。 */
    private final String code;
    /** 是否必需。 */
    private final boolean required;
    /** 创建次数，用于直观看到对象是否共享。 */
    private static int createdCount;
    public MaterialRule(String code, boolean required) {
        this.code = code; this.required = required; createdCount++;
        System.out.println("[享元/v3] MaterialRule.<init> ---> 创建规则 code=" + code);
    }
    public String getCode() { return code; }
    public boolean isRequired() { return required; }
    public static int getCreatedCount() { return createdCount; }
}
