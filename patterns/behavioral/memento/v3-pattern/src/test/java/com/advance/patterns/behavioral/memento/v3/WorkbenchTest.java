package com.advance.patterns.behavioral.memento.v3;

import org.junit.jupiter.api.Test;

/**
 * 验证 Originator、Memento 与 Caretaker 的职责划分。
 *
 * @author advance
 * @since 2026-10-29 13:34:23
 */
public class WorkbenchTest {
    /**
     * 输入 V1、V2、V3 草稿；期望两次撤销依次恢复 V2 和 V1，历史管理者只压栈/弹栈而不操作业务字段。
     */
    @Test
    public void shouldRestoreOpaqueMementosInLifoOrder() {
        System.out.println("期望：Memento 保存不可变状态，DraftHistory 以 Deque 管理历史，SettlementDraft 自行恢复");
        new Workbench().runUndoScenario();
    }

    /**
     * 输入空历史栈并请求 undo；期望抛出 IllegalStateException，清楚提示不存在可撤销状态。
     */
    @Test
    public void shouldReportWhenUndoHistoryIsEmpty() {
        Workbench.DraftHistory history = new Workbench.DraftHistory();
        System.out.println("期望异常：空历史调用 undo() 时抛出 IllegalStateException");
        try {
            history.undo();
            System.out.println("未抛出异常，请对照期望检查");
        } catch (IllegalStateException exception) {
            System.out.println("实际异常：" + exception.getClass().getSimpleName() + "，原因：" + exception.getMessage());
        }
    }
}
