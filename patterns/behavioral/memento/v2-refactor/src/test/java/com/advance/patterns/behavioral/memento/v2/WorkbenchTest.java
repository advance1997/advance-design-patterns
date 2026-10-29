package com.advance.patterns.behavioral.memento.v2;

import org.junit.jupiter.api.Test;

/**
 * 验证由和解草稿创建状态快照并通过历史栈连续恢复。
 *
 * @author advance
 * @since 2026-10-29 13:34:23
 */
public class WorkbenchTest {
    /**
     * 输入 V1、V2、V3 草稿；期望两次 undo 依次恢复 V2、V1；
     * 此过渡快照提供公开 getter，内部字段尚未完全封装。
     */
    @Test
    public void shouldCreateSnapshotsAndRestoreInReverseOrder() {
        System.out.println("期望：状态对象负责快照创建与恢复，DraftHistory 以 Deque 后进先出管理快照");
        new Workbench().runUndoScenario();
    }
}
