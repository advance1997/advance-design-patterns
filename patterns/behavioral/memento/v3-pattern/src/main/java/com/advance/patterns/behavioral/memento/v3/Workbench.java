package com.advance.patterns.behavioral.memento.v3;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * 使用封装状态的 Memento 保存并恢复催收和解方案编辑草稿。
 *
 * @author advance
 * @since 2026-10-29 13:34:23
 */
public class Workbench {
    /**
     * 运行专题中的连续修改及多次撤销案例。
     */
    public void runUndoScenario() {
        SettlementDraft draft = new SettlementDraft(
                10001L,
                new BigDecimal("1000"),
                Arrays.asList(new BigDecimal("2000"), new BigDecimal("2000")),
                "客户首次提出和解");
        DraftHistory history = new DraftHistory();
        System.out.println("=== V1 初始方案 ===");
        draft.print();
        history.save(draft.createMemento());
        draft.changeDownPayment(new BigDecimal("500"));
        System.out.println("\n=== V2 修改首付 ===");
        draft.print();
        history.save(draft.createMemento());
        draft.changeInstallments(Arrays.asList(new BigDecimal("2500"), new BigDecimal("1500")));
        System.out.println("\n=== V3 修改分期 ===");
        draft.print();
        draft.restore(history.undo());
        System.out.println("\n=== 第一次撤销，恢复 V2 ===");
        draft.print();
        draft.restore(history.undo());
        System.out.println("\n=== 第二次撤销，恢复 V1 ===");
        draft.print();
    }

    /**
     * Originator：拥有草稿字段并对外提供受控的快照创建与状态恢复。
     */
    static class SettlementDraft {
        /** 草稿所属虚构案件编号。 */
        private final long caseId;
        /** 当前首付款。 */
        private BigDecimal downPayment;
        /** 当前分期金额。 */
        private List<BigDecimal> installments;
        /** 当前和解备注。 */
        private String remark;

        /**
         * 创建和解草稿并复制分期集合，防止调用方修改草稿内部状态。
         *
         * @param caseId 虚构案件编号
         * @param downPayment 初始首付款
         * @param installments 初始分期金额列表
         * @param remark 初始和解备注
         */
        SettlementDraft(long caseId, BigDecimal downPayment, List<BigDecimal> installments, String remark) {
            this.caseId = caseId;
            this.downPayment = downPayment;
            this.installments = new ArrayList<BigDecimal>(installments);
            this.remark = remark;
        }

        /**
         * 修改首付款并拒绝空值或负数。
         *
         * @param amount 新首付款金额
         * @throws IllegalArgumentException 首付款为空或小于零时抛出
         */
        void changeDownPayment(BigDecimal amount) {
            if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("[备忘录/v3] SettlementDraft.changeDownPayment ---> 首付款校验失败");
                throw new IllegalArgumentException("首付款不能为空或小于零");
            }
            downPayment = amount;
            System.out.println("[备忘录/v3] SettlementDraft.changeDownPayment ---> 首付款修改为：" + amount);
        }

        /**
         * 修改分期金额，并复制集合以隔离外部可变引用。
         *
         * @param amounts 新分期金额列表
         * @throws IllegalArgumentException 列表为空或包含空值、负数时抛出
         */
        void changeInstallments(List<BigDecimal> amounts) {
            if (amounts == null || amounts.isEmpty()) {
                System.out.println("[备忘录/v3] SettlementDraft.changeInstallments ---> 分期列表为空，拒绝修改");
                throw new IllegalArgumentException("分期金额不能为空");
            }
            for (BigDecimal amount : amounts) {
                if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("[备忘录/v3] SettlementDraft.changeInstallments ---> 分期金额校验失败");
                    throw new IllegalArgumentException("分期金额不能为空或小于零");
                }
            }
            installments = new ArrayList<BigDecimal>(amounts);
            System.out.println("[备忘录/v3] SettlementDraft.changeInstallments ---> 分期金额修改为：" + installments);
        }

        /**
         * 创建保存当前完整草稿状态的不可变备忘录。
         *
         * @return 当前草稿状态的 Memento
         */
        Memento createMemento() {
            System.out.println("[备忘录/v3] SettlementDraft.createMemento ---> 创建不可变状态快照");
            return new Memento(downPayment, installments, remark);
        }

        /**
         * 将草稿恢复到备忘录保存的状态，草稿对象独占恢复后的列表。
         *
         * @param memento 由本草稿创建的状态快照
         * @throws IllegalArgumentException 备忘录为空时抛出
         */
        void restore(Memento memento) {
            if (memento == null) {
                System.out.println("[备忘录/v3] SettlementDraft.restore ---> 备忘录为空，拒绝恢复");
                throw new IllegalArgumentException("备忘录不能为空");
            }
            downPayment = memento.downPayment;
            installments = new ArrayList<BigDecimal>(memento.installments);
            remark = memento.remark;
            System.out.println("[备忘录/v3] SettlementDraft.restore ---> 已恢复首付、分期和备注");
        }

        /**
         * 输出草稿完整状态用于人工检查。
         */
        void print() {
            System.out.println("[备忘录/v3] SettlementDraft.print ---> 案件：" + caseId
                    + "，首付：" + downPayment + "，分期：" + installments
                    + "，备注：" + remark);
        }

        /**
         * Memento：保存 Originator 状态但不向 Caretaker 暴露内部字段。
         */
        public static final class Memento {
            /** 保存时的首付款。 */
            private final BigDecimal downPayment;
            /** 保存时的只读分期列表。 */
            private final List<BigDecimal> installments;
            /** 保存时的备注。 */
            private final String remark;

            /**
             * 仅允许外层 Originator 创建备忘录。
             *
             * @param downPayment 快照首付款
             * @param installments 快照分期金额
             * @param remark 快照备注
             */
            private Memento(BigDecimal downPayment, List<BigDecimal> installments, String remark) {
                this.downPayment = downPayment;
                this.installments = Collections.unmodifiableList(new ArrayList<BigDecimal>(installments));
                this.remark = remark;
            }
        }
    }

    /**
     * Caretaker：只管理 Memento 栈，不读取或修改快照中的业务字段。
     */
    static class DraftHistory {
        /** 保存历史状态的 LIFO 栈。 */
        private final Deque<SettlementDraft.Memento> history = new ArrayDeque<SettlementDraft.Memento>();

        /**
         * 将一次草稿状态快照压入历史栈。
         *
         * @param memento Originator 创建的状态快照
         * @throws IllegalArgumentException 备忘录为空时抛出
         */
        void save(SettlementDraft.Memento memento) {
            if (memento == null) {
                throw new IllegalArgumentException("不能保存空备忘录");
            }
            history.push(memento);
            System.out.println("[备忘录/v3] DraftHistory.save ---> 快照入栈");
        }

        /**
         * 获取并移除最近一次保存的状态快照。
         *
         * @return 最近保存的 Memento
         * @throws IllegalStateException 历史栈为空时抛出
         */
        SettlementDraft.Memento undo() {
            if (history.isEmpty()) {
                System.out.println("[备忘录/v3] DraftHistory.undo ---> 历史栈为空，无法撤销");
                throw new IllegalStateException("没有可撤销的历史");
            }
            System.out.println("[备忘录/v3] DraftHistory.undo ---> 弹出最近状态快照");
            return history.pop();
        }
    }
}
