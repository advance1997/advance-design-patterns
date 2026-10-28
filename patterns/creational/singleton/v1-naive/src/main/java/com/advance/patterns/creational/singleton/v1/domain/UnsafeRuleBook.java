package com.advance.patterns.creational.singleton.v1.domain;

/**
 * 反例：未同步的懒加载与共享案件字段；禁止作为生产实现。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public final class UnsafeRuleBook {
    /** 未使用同步或 volatile，首次并发获取存在竞态。 */
    private static UnsafeRuleBook instance;
    /** 错误地放在共享对象中的单次案件状态。 */
    private String currentCaseId;

    /**
     * 私有构造器本身不能保证并发只创建一次。
     */
    private UnsafeRuleBook() {
        System.out.println("[singleton/v1] UnsafeRuleBook ---> 创建不安全懒加载实例");
    }

    /**
     * 未同步的懒加载反例。
     * @return 当前静态引用，并发时不保证调用方得到同一对象
     */
    public static UnsafeRuleBook getInstance() {
        if (instance == null) {
            instance = new UnsafeRuleBook();
        }
        return instance;
    }

    /**
     * 错误示范：将一次请求的案件编号保存到全局共享对象。
     * @param caseId 虚构案件编号
     * @throws IllegalArgumentException 编号为空
     */
    public void beginReview(String caseId) {
        if (caseId == null || caseId.trim().isEmpty()) {
            throw new IllegalArgumentException("caseId 不能为空");
        }
        currentCaseId = caseId;
        System.out.println("[singleton/v1] UnsafeRuleBook.beginReview ---> 覆盖共享案件状态，caseId=" + caseId);
    }

    /**
     * 读取容易被其他调用覆盖的状态。
     * @return 当前共享案件编号
     */
    public String getCurrentCaseId() {
        return currentCaseId;
    }
}
