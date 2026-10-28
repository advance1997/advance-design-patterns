package com.advance.patterns.structural.flyweight.v3.flyweight;

/**
 * 不可变共享享元，只保存所有案件相同的内在规则，不保存案件编号或归集结果。
 *
 * @author advance
 * @since 2026-10-28 21:22:00
 */
public final class MaterialRuleFlyweight {
    private final String code;
    private final boolean required;
    public MaterialRuleFlyweight(String code, boolean required) {
        this.code = code; this.required = required;
        System.out.println("[享元/v3] MaterialRuleFlyweight.<init> ---> 创建共享规则 code=" + code);
    }
    public String getCode() { return code; }
    public boolean isRequired() { return required; }
}
