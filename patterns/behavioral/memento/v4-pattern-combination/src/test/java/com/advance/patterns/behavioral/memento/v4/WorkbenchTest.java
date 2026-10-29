package com.advance.patterns.behavioral.memento.v4;

import org.junit.jupiter.api.Test;

/**
 * 验证备忘录与命令、中介者、状态边界及版本历史的专题组合案例。
 *
 * @author advance
 * @since 2026-10-29 13:32:02
 */
public class WorkbenchTest {
    /**
     * 输入首付、分期及备注编辑请求；期望中介者转交命令，命令用 Memento 保存完整旧状态，
     * 三次撤销按 LIFO 逐项恢复。
     */
    @Test
    public void shouldCombineMediatorCommandAndMementoForUndo() {
        System.out.println("期望：编辑组件只通知 Mediator，Command 封装操作，Memento 恢复每次修改前的状态");
        new Workbench().runCommandMediatorScenario();
    }

    /**
     * 输入已编辑草稿并审批；期望状态规则拒绝后续编辑和 undo，
     * 已审批业务状态不因恢复对象快照而回滚。
     */
    @Test
    public void shouldNotRestoreApprovedBusinessPlan() {
        System.out.println("期望：仅未提交审批的草稿允许 Memento 撤销，已审批方案必须走业务变更流程");
        new Workbench().runApprovedPlanBoundaryScenario();
    }

    /**
     * 输入初始金额1000并更新至500；期望 version 只控制并发更新，独立历史归档才保留1000与500两个快照。
     */
    @Test
    public void shouldDistinguishOptimisticVersionFromSavedHistory() {
        System.out.println("期望：乐观锁 version 不自动保存历史；需单独维护方案快照历史");
        new Workbench().runVersionHistoryScenario();
    }
}
