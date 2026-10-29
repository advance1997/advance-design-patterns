package com.advance.patterns.behavioral.mediator.v2;

import org.junit.jupiter.api.Test;

/**
 * 验证抽取具体协调者后的工作台交互路径。
 *
 * @author advance
 * @since 2026-10-29 09:32:02
 */
public class WorkbenchTest {
    /**
     * 输入案件 10001 和 10002；期望具体协调者组织正常外呼及合规拒绝流程，组件不再直接访问其他组件，旧案件回调不刷新当前话术。
     */
    @Test
    public void shouldDelegateComponentInteractionsToCoordinator() {
        System.out.println("期望：CaseSelector、DialPanel、CallCallback 只向 WorkbenchCoordinator 报告交互，由协调者决定后续流程");
        new Workbench().runScenarios();
    }
}
