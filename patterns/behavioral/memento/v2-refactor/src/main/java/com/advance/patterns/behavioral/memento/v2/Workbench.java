package com.advance.patterns.behavioral.memento.v2;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * 将快照创建和恢复职责移入和解草稿对象的重构版本。
 *
 * @author advance
 * @since 2026-10-29 13:34:23
 */
public class Workbench {
    /**
     * 演示创建快照、修改草稿并连续撤销到先前版本。
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
        history.save(draft.createSnapshot());
        draft.changeDownPayment(new BigDecimal("500"));
        System.out.println("\n=== V2 修改首付 ===");
        draft.print();
        history.save(draft.createSnapshot());
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
     * 和解草稿作为状态发起者，负责创建快照与恢复自身状态。
     */
    static class SettlementDraft {
        /** 草稿所属虚构案件编号。 */
        private final long caseId;
        /** 当前首付款金额。 */
        private BigDecimal downPayment;
        /** 当前分期金额。 */
        private List<BigDecimal> installments;
        /** 当前方案备注。 */
        private String remark;

        /**
         * 创建草稿并隔离调用方传入的可变分期列表。
         *
         * @param caseId 虚构案件编号
         * @param downPayment 首付款
         * @param installments 分期金额列表
         * @param remark 和解备注
         */
        SettlementDraft(long caseId, BigDecimal downPayment, List<BigDecimal> installments, String remark) {
            this.caseId = caseId;
            this.downPayment = downPayment;
            this.installments = new ArrayList<BigDecimal>(installments);
            this.remark = remark;
        }

        /**
         * 更新首付款。
         *
         * @param amount 新首付款
         */
        void changeDownPayment(BigDecimal amount) {
            downPayment = amount;
            System.out.println("[备忘录/v2] SettlementDraft.changeDownPayment ---> 首付款修改为：" + amount);
        }

        /**
         * 更新分期金额并复制传入列表。
         *
         * @param amounts 新分期金额列表
         */
        void changeInstallments(List<BigDecimal> amounts) {
            installments = new ArrayList<BigDecimal>(amounts);
            System.out.println("[备忘录/v2] SettlementDraft.changeInstallments ---> 分期金额修改为：" + installments);
        }

        /**
         * 由状态拥有者创建完整快照。
         *
         * @return 当前和解方案状态快照
         */
        Snapshot createSnapshot() {
            System.out.println("[备忘录/v2] SettlementDraft.createSnapshot ---> 由草稿保存完整状态");
            return new Snapshot(downPayment, installments, remark);
        }

        /**
         * 将草稿恢复到给定快照中的字段值。
         *
         * @param snapshot 需要恢复的方案快照
         * @throws IllegalArgumentException snapshot 为空时抛出
         */
        void restore(Snapshot snapshot) {
            if (snapshot == null) {
                throw new IllegalArgumentException("方案快照不能为空");
            }
            downPayment = snapshot.getDownPayment();
            installments = new ArrayList<BigDecimal>(snapshot.getInstallments());
            remark = snapshot.getRemark();
            System.out.println("[备忘录/v2] SettlementDraft.restore ---> 从快照恢复首付、分期和备注");
        }

        /**
         * 输出完整方案状态用于人工核对。
         */
        void print() {
            System.out.println("[备忘录/v2] SettlementDraft.print ---> 案件：" + caseId
                    + "，首付：" + downPayment + "，分期：" + installments
                    + "，备注：" + remark);
        }
    }

    /**
     * 保存一次状态快照；本过渡版本仍公开快照内容给调用方读取。
     */
    public static class Snapshot {
        /** 保存时的首付款。 */
        private final BigDecimal downPayment;
        /** 保存时的分期金额不可变副本。 */
        private final List<BigDecimal> installments;
        /** 保存时的草稿备注。 */
        private final String remark;

        /**
         * 创建快照并隔离可变分期集合。
         *
         * @param downPayment 保存时首付款
         * @param installments 保存时分期金额
         * @param remark 保存时草稿备注
         */
        Snapshot(BigDecimal downPayment, List<BigDecimal> installments, String remark) {
            this.downPayment = downPayment;
            this.installments = Collections.unmodifiableList(new ArrayList<BigDecimal>(installments));
            this.remark = remark;
        }

        /**
         * 获取快照首付款。
         *
         * @return 保存时首付款
         */
        public BigDecimal getDownPayment() {
            return downPayment;
        }

        /**
         * 获取只读分期列表。
         *
         * @return 保存时分期金额列表
         */
        public List<BigDecimal> getInstallments() {
            return installments;
        }

        /**
         * 获取快照备注。
         *
         * @return 保存时备注
         */
        public String getRemark() {
            return remark;
        }
    }

    /**
     * 使用栈保存快照并提供后进先出的撤销记录。
     */
    static class DraftHistory {
        /** 历史状态栈，最近保存的快照位于栈顶。 */
        private final Deque<Snapshot> history = new ArrayDeque<Snapshot>();

        /**
         * 保存一次草稿快照。
         *
         * @param snapshot 由草稿对象创建的状态快照
         */
        void save(Snapshot snapshot) {
            history.push(snapshot);
            System.out.println("[备忘录/v2] DraftHistory.save ---> 将方案快照压入撤销栈");
        }

        /**
         * 弹出最近一次保存的状态快照。
         *
         * @return 最近保存的快照
         * @throws IllegalStateException 没有可撤销快照时抛出
         */
        Snapshot undo() {
            if (history.isEmpty()) {
                System.out.println("[备忘录/v2] DraftHistory.undo ---> 没有可撤销的历史");
                throw new IllegalStateException("没有可撤销的历史");
            }
            System.out.println("[备忘录/v2] DraftHistory.undo ---> 从撤销栈取出最近快照");
            return history.pop();
        }
    }
}
