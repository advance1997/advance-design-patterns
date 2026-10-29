package com.advance.patterns.behavioral.mediator.v3;

import org.junit.jupiter.api.Test;

/**
 * 验证中介者接口统一协调工作台各协作对象。
 *
 * @author advance
 * @since 2026-10-29 09:32:02
 */
public class WorkbenchTest {
    /**
     * 输入案件 10001 和 10002；期望组件只依赖 WorkbenchMediator，正常案件完成工作流、限制案件被拒绝，旧案件回调不会更新当前案件。
     */
    @Test
    public void shouldCoordinateWorkbenchThroughMediatorInterface() {
        System.out.println("期望：组件只持有 WorkbenchMediator；中介者统一编排选择、话术、合规、外呼、审计和回调");
        new Workbench().runScenarios();
    }
}
