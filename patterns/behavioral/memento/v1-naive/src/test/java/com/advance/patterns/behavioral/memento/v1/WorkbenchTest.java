package com.advance.patterns.behavioral.memento.v1;

import org.junit.jupiter.api.Test;

/**
 * 验证外部控制器手工保存和恢复和解草稿字段的推演版本。
 *
 * @author advance
 * @since 2026-10-29 13:34:23
 */
public class WorkbenchTest {
    /**
     * 输入初始方案、降低首付、调整分期；期望两次撤销依次恢复完整 V2 与 V1，
     * 但字段清单仍由控制器维护。
     */
    @Test
    public void shouldManuallySaveAndRestoreDraftFields() {
        System.out.println("期望：V3 后第一次撤销恢复首付500与分期[2000,2000]，");
        System.out.println("第二次撤销恢复首付1000与原始分期");
        new Workbench().runUndoScenario();
    }
}
