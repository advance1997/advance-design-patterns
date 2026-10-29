package com.advance.patterns.behavioral.mediator.v1;

import org.junit.jupiter.api.Test;

/**
 * 展示工作台组件直接互相协调时的执行路径。
 *
 * @author advance
 * @since 2026-10-29 09:32:02
 */
public class WorkbenchTest {
    /**
     * 输入案件 10001 和 10002；期望 10001 正常外呼，切换到 10002 后旧案件回调不刷新当前话术，且 10002 被拒绝外呼。
     */
    @Test
    public void shouldCoordinateDirectlyWithComponentReferences() {
        System.out.println("期望：案件10001正常外呼；案件10002因模拟合规限制被拒绝；协作对象直接持有彼此引用");
        new Workbench().runScenarios();
    }
}
