package com.advance.patterns.behavioral.memento.v1;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * 演示由外部编辑流程逐项保存和恢复和解草稿字段的朴素方案。
 *
 * @author advance
 * @since 2026-10-29 13:34:23
 */
public class Workbench {
    /**
     * 运行多次修改与撤销示例。
     */
    public void runUndoScenario() {
        SettlementDraft draft = new SettlementDraft(
                10001L,
                new BigDecimal("1000"),
                Arrays.asList(new BigDecimal("2000"), new BigDecimal("2000")),
                "客户首次提出和解");
        DraftController controller = new DraftController(draft);
        System.out.println("=== V1 初始方案 ===");
        draft.print();
        controller.saveBeforeEdit();
        draft.changeDownPayment(new BigDecimal("500"));
        System.out.println("\n=== V2 修改首付 ===");
        draft.print();
        controller.saveBeforeEdit();
        draft.changeInstallments(Arrays.asList(new BigDecimal("2500"), new BigDecimal("1500")));
        System.out.println("\n=== V3 修改分期 ===");
        draft.print();
        controller.undo();
        System.out.println("\n=== 第一次撤销，恢复 V2 ===");
        draft.print();
        controller.undo();
        System.out.println("\n=== 第二次撤销，恢复 V1 ===");
        draft.print();
    }

    /**
     * 保存和解方案草稿当前可编辑状态。
     */
    static class SettlementDraft {
        /** 草稿所属的虚构案件编号。 */
        private final long caseId;
        /** 当前首付款金额。 */
        private BigDecimal downPayment;
        /** 当前分期金额列表。 */
        private List<BigDecimal> installments;
        /** 和解草稿备注。 */
        private String remark;

        /**
         * 创建和解草稿并复制分期列表，避免调用方随后修改内部集合。
         *
         * @param caseId 虚构案件编号
         * @param downPayment 初始首付款
         * @param installments 初始分期金额列表
         * @param remark 草稿备注
         */
        SettlementDraft(long caseId, BigDecimal downPayment, List<BigDecimal> installments, String remark) {
            this.caseId = caseId;
            this.downPayment = downPayment;
            this.installments = new ArrayList<BigDecimal>(installments);
            this.remark = remark;
        }

        /**
         * 修改草稿首付款。
         *
         * @param amount 新首付款
         */
        void changeDownPayment(BigDecimal amount) {
            this.downPayment = amount;
            System.out.println("[备忘录/v1] SettlementDraft.changeDownPayment ---> 首付款修改为：" + amount);
        }

        /**
         * 修改分期金额并复制输入列表。
         *
         * @param amounts 新分期金额列表
         */
        void changeInstallments(List<BigDecimal> amounts) {
            this.installments = new ArrayList<BigDecimal>(amounts);
            System.out.println("[备忘录/v1] SettlementDraft.changeInstallments ---> 分期金额修改为：" + this.installments);
        }

        /**
         * 获取当前首付款供外部控制器手工备份。
         *
         * @return 当前首付款
         */
        BigDecimal getDownPayment() {
            return downPayment;
        }

        /**
         * 获取分期列表副本供外部控制器手工备份。
         *
         * @return 分期金额列表副本
         */
        List<BigDecimal> copyInstallments() {
            return new ArrayList<BigDecimal>(installments);
        }

        /**
         * 获取草稿备注供外部控制器手工备份。
         *
         * @return 草稿备注
         */
        String getRemark() {
            return remark;
        }

        /**
         * 逐项恢复备份字段；调用者必须同步维护字段清单。
         *
         * @param oldDownPayment 历史首付款
         * @param oldInstallments 历史分期列表
         * @param oldRemark 历史备注
         */
        void restoreFields(BigDecimal oldDownPayment, List<BigDecimal> oldInstallments, String oldRemark) {
            downPayment = oldDownPayment;
            installments = new ArrayList<BigDecimal>(oldInstallments);
            remark = oldRemark;
            System.out.println("[备忘录/v1] SettlementDraft.restoreFields ---> 外部控制器逐个恢复草稿字段");
        }

        /**
         * 输出完整草稿状态供人工核对。
         */
        void print() {
            System.out.println("[备忘录/v1] SettlementDraft.print ---> 案件：" + caseId
                    + "，首付：" + downPayment + "，分期：" + installments
                    + "，备注：" + remark);
        }
    }

    /**
     * 外部编辑控制器手工维护全部历史字段，展示字段变更时控制器也要修改的问题。
     */
    static class DraftController {
        /** 正在编辑的方案草稿。 */
        private final SettlementDraft draft;
        /** 按后进先出保存逐字段备份。 */
        private final Deque<ExplicitBackup> history = new ArrayDeque<ExplicitBackup>();

        /**
         * 创建控制器并绑定待编辑草稿。
         *
         * @param draft 当前和解草稿
         */
        DraftController(SettlementDraft draft) {
            this.draft = draft;
        }

        /**
         * 手工读取每个业务字段并存入历史栈。
         */
        void saveBeforeEdit() {
            history.push(new ExplicitBackup(draft.getDownPayment(), draft.copyInstallments(), draft.getRemark()));
            System.out.println("[备忘录/v1] DraftController.saveBeforeEdit ---> 外部控制器分别保存首付、分期和备注");
        }

        /**
         * 从历史栈取回显式字段并逐项交还给草稿。
         *
         * @throws IllegalStateException 没有历史备份时抛出
         */
        void undo() {
            if (history.isEmpty()) {
                System.out.println("[备忘录/v1] DraftController.undo ---> 没有可撤销的历史");
                throw new IllegalStateException("没有可撤销的历史");
            }
            ExplicitBackup backup = history.pop();
            draft.restoreFields(backup.downPayment, backup.installments, backup.remark);
        }
    }

    /**
     * 公开表达控制器知道的所有业务字段，体现封装不足的临时备份结构。
     */
    static class ExplicitBackup {
        /** 历史首付款。 */
        private final BigDecimal downPayment;
        /** 历史分期列表。 */
        private final List<BigDecimal> installments;
        /** 历史备注。 */
        private final String remark;

        /**
         * 创建一次字段级备份。
         *
         * @param downPayment 历史首付款
         * @param installments 历史分期列表
         * @param remark 历史备注
         */
        ExplicitBackup(BigDecimal downPayment, List<BigDecimal> installments, String remark) {
            this.downPayment = downPayment;
            this.installments = new ArrayList<BigDecimal>(installments);
            this.remark = remark;
        }
    }
}
