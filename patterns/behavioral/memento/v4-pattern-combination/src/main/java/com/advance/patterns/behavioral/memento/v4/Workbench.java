package com.advance.patterns.behavioral.memento.v4;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将备忘录与命令、中介者和业务状态边界组合，并模拟持久化版本历史概念。
 *
 * @author advance
 * @since 2026-10-29 13:32:02
 */
public class Workbench {
    /**
     * 演示 Mediator 协调编辑组件、Command 封装修改、Memento 支持撤销。
     */
    public void runCommandMediatorScenario() {
        SettlementDraft draft = new SettlementDraft(
                10001L,
                new BigDecimal("1000"),
                Arrays.asList(new BigDecimal("2000"), new BigDecimal("2000")),
                "客户首次提出和解");
        DefaultSettlementEditorMediator mediator = new DefaultSettlementEditorMediator(draft, new CommandHistory());
        DownPaymentEditor downPaymentEditor = new DownPaymentEditor(mediator);
        InstallmentEditor installmentEditor = new InstallmentEditor(mediator);
        RemarkEditor remarkEditor = new RemarkEditor(mediator);
        UndoButton undoButton = new UndoButton(mediator);

        System.out.println("=== 编辑和解草稿 ===");
        draft.print();
        downPaymentEditor.changeTo(new BigDecimal("500"));
        installmentEditor.changeTo(Arrays.asList(new BigDecimal("2500"), new BigDecimal("1500")));
        remarkEditor.changeTo("客户确认调整首付和分期");
        System.out.println("编辑后：");
        draft.print();
        undoButton.click();
        undoButton.click();
        undoButton.click();
        System.out.println("连续撤销三次后：");
        draft.print();
    }

    /**
     * 演示审批是业务生命周期变化，不能通过恢复草稿备忘录撤销。
     */
    public void runApprovedPlanBoundaryScenario() {
        SettlementDraft draft = new SettlementDraft(
                10002L,
                new BigDecimal("800"),
                Arrays.asList(new BigDecimal("1600")),
                "待审批方案");
        DefaultSettlementEditorMediator mediator = new DefaultSettlementEditorMediator(draft, new CommandHistory());
        DownPaymentEditor editor = new DownPaymentEditor(mediator);
        UndoButton undoButton = new UndoButton(mediator);
        ApprovalButton approvalButton = new ApprovalButton(mediator);

        editor.changeTo(new BigDecimal("600"));
        approvalButton.approve();
        System.out.println("期望：审批后修改和 undo 均被业务状态拦截，不能通过恢复 Java 对象状态撤销审批");
        try {
            editor.changeTo(new BigDecimal("400"));
            System.out.println("未抛出异常，请对照期望检查");
        } catch (IllegalStateException exception) {
            System.out.println("实际异常：" + exception.getMessage());
        }
        try {
            undoButton.click();
            System.out.println("未抛出异常，请对照期望检查");
        } catch (IllegalStateException exception) {
            System.out.println("实际异常：" + exception.getMessage());
        }
        draft.print();
    }

    /**
     * 用内存模拟 optimistic-lock 版本与独立历史快照的区别。
     */
    public void runVersionHistoryScenario() {
        InMemoryPlanStore store = new InMemoryPlanStore();
        PlanHistoryArchive archive = new PlanHistoryArchive();
        store.create(10003L, new BigDecimal("1000"));
        archive.save(store.snapshot(10003L, "OPS_DEMO", "2026-10-29 13:32:02"));
        store.updateAmount(10003L, 1L, new BigDecimal("500"));
        archive.save(store.snapshot(10003L, "OPS_DEMO", "2026-10-29 13:33:02"));
        System.out.println("使用旧 version=1 再次更新，期望因并发版本冲突而失败：");
        store.updateAmount(10003L, 1L, new BigDecimal("300"));
        System.out.println("当前记录：");
        store.print(10003L);
        System.out.println("单独归档的状态历史：");
        archive.printAll();
    }

    /**
     * 表示和解草稿当前所处的业务生命周期。
     */
    enum PlanStatus {
        /** 尚未审批，可编辑并可撤销草稿修改。 */
        DRAFT,
        /** 已审批，不能通过内存快照恢复撤销业务审批。 */
        APPROVED
    }

    /**
     * 和解草稿 Originator；负责创建状态快照和按业务状态恢复草稿。
     */
    static class SettlementDraft {
        /** 虚构案件编号。 */
        private final long caseId;
        /** 当前首付款。 */
        private BigDecimal downPayment;
        /** 当前分期金额。 */
        private List<BigDecimal> installments;
        /** 和解备注。 */
        private String remark;
        /** 当前草稿审批状态。 */
        private PlanStatus status = PlanStatus.DRAFT;

        /**
         * 创建尚未审批的和解草稿。
         *
         * @param caseId 虚构案件编号
         * @param downPayment 初始首付款
         * @param installments 初始分期金额
         * @param remark 初始备注
         */
        SettlementDraft(long caseId, BigDecimal downPayment, List<BigDecimal> installments, String remark) {
            this.caseId = caseId;
            this.downPayment = downPayment;
            this.installments = new ArrayList<BigDecimal>(installments);
            this.remark = remark;
        }

        /**
         * 修改首付款；只有草稿状态允许编辑。
         *
         * @param amount 新首付款
         * @throws IllegalStateException 草稿已审批时抛出
         */
        void changeDownPayment(BigDecimal amount) {
            ensureDraft();
            if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("首付款不能为空或小于零");
            }
            downPayment = amount;
            System.out.println("[备忘录/v4] SettlementDraft.changeDownPayment ---> 首付款修改为：" + amount);
        }

        /**
         * 修改分期并防御性复制传入列表。
         *
         * @param amounts 新分期金额
         * @throws IllegalStateException 草稿已审批时抛出
         * @throws IllegalArgumentException 列表为空或包含非法金额时抛出
         */
        void changeInstallments(List<BigDecimal> amounts) {
            ensureDraft();
            if (amounts == null || amounts.isEmpty()) {
                throw new IllegalArgumentException("分期金额不能为空");
            }
            for (BigDecimal amount : amounts) {
                if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
                    throw new IllegalArgumentException("分期金额不能为空或小于零");
                }
            }
            installments = new ArrayList<BigDecimal>(amounts);
            System.out.println("[备忘录/v4] SettlementDraft.changeInstallments ---> 分期修改为：" + installments);
        }

        /**
         * 修改和解备注。
         *
         * @param newRemark 新备注
         * @throws IllegalStateException 草稿已审批时抛出
         */
        void changeRemark(String newRemark) {
            ensureDraft();
            remark = newRemark;
            System.out.println("[备忘录/v4] SettlementDraft.changeRemark ---> 更新和解备注");
        }

        /**
         * 创建当前方案状态的不可变备忘录。
         *
         * @return 保存首付、分期和备注的快照
         */
        Memento createMemento() {
            System.out.println("[备忘录/v4] SettlementDraft.createMemento ---> 保存命令执行前的完整草稿状态");
            return new Memento(downPayment, installments, remark);
        }

        /**
         * 从历史状态恢复草稿数据；审批状态本身不属于可撤销快照。
         *
         * @param memento 原草稿状态快照
         * @throws IllegalStateException 草稿已审批时抛出
         */
        void restore(Memento memento) {
            ensureDraft();
            downPayment = memento.downPayment;
            installments = new ArrayList<BigDecimal>(memento.installments);
            remark = memento.remark;
            System.out.println("[备忘录/v4] SettlementDraft.restore ---> 恢复草稿字段，不回滚审批状态");
        }

        /**
         * 提交教学用审批状态变更。
         */
        void approve() {
            ensureDraft();
            status = PlanStatus.APPROVED;
            System.out.println("[备忘录/v4] SettlementDraft.approve ---> 和解方案进入已审批状态");
        }

        /**
         * 查询草稿是否仍允许修改。
         *
         * @return 当前处于 DRAFT 时返回 true
         */
        boolean isDraft() {
            return status == PlanStatus.DRAFT;
        }

        /**
         * 校验当前业务状态并在输出后拒绝不可逆操作。
         */
        private void ensureDraft() {
            if (status != PlanStatus.DRAFT) {
                System.out.println("[备忘录/v4] SettlementDraft.ensureDraft ---> 方案已审批，拒绝草稿编辑或恢复");
                throw new IllegalStateException("方案已审批，不能通过草稿快照回滚");
            }
        }

        /**
         * 输出草稿状态，供测试者人工核对。
         */
        void print() {
            System.out.println("[备忘录/v4] SettlementDraft.print ---> 案件：" + caseId
                    + "，首付：" + downPayment + "，分期：" + installments
                    + "，备注：" + remark + "，状态：" + status);
        }

        /**
         * 保存 Originator 的可恢复编辑字段，不保存审批生命周期状态。
         */
        static final class Memento {
            /** 快照首付款。 */
            private final BigDecimal downPayment;
            /** 快照分期金额。 */
            private final List<BigDecimal> installments;
            /** 快照备注。 */
            private final String remark;

            /**
             * 创建不可变草稿快照并复制集合字段。
             *
             * @param downPayment 首付款
             * @param installments 分期金额
             * @param remark 备注
             */
            private Memento(BigDecimal downPayment, List<BigDecimal> installments, String remark) {
                this.downPayment = downPayment;
                this.installments = new ArrayList<BigDecimal>(installments);
                this.remark = remark;
            }
        }
    }

    /**
     * 命令接口：将一次编辑操作及其撤销行为对象化。
     */
    interface EditCommand {
        /**
         * 保存修改前状态并应用编辑。
         */
        void execute();

        /**
         * 使用命令持有的备忘录恢复修改前状态。
         */
        void undo();
    }

    /**
     * 封装首付款修改及基于 Memento 的撤销。
     */
    static class ChangeDownPaymentCommand implements EditCommand {
        /** 被编辑的和解草稿。 */
        private final SettlementDraft draft;
        /** 修改目标金额。 */
        private final BigDecimal amount;
        /** 命令执行前的草稿状态。 */
        private SettlementDraft.Memento memento;

        /**
         * 创建首付款修改命令。
         *
         * @param draft 被编辑草稿
         * @param amount 目标首付款
         */
        ChangeDownPaymentCommand(SettlementDraft draft, BigDecimal amount) {
            this.draft = draft;
            this.amount = amount;
        }

        /**
         * 保存旧状态后修改首付款。
         */
        @Override
        public void execute() {
            memento = draft.createMemento();
            draft.changeDownPayment(amount);
        }

        /**
         * 将草稿恢复至命令执行前状态。
         */
        @Override
        public void undo() {
            draft.restore(memento);
        }
    }

    /**
     * 封装分期金额修改及基于 Memento 的撤销。
     */
    static class ChangeInstallmentsCommand implements EditCommand {
        /** 被编辑草稿。 */
        private final SettlementDraft draft;
        /** 新分期金额的隔离副本。 */
        private final List<BigDecimal> amounts;
        /** 命令执行前的状态。 */
        private SettlementDraft.Memento memento;

        /**
         * 创建分期修改命令并复制命令参数。
         *
         * @param draft 被编辑草稿
         * @param amounts 目标分期金额
         */
        ChangeInstallmentsCommand(SettlementDraft draft, List<BigDecimal> amounts) {
            this.draft = draft;
            this.amounts = amounts == null ? null : new ArrayList<BigDecimal>(amounts);
        }

        /**
         * 保存旧状态后修改分期。
         */
        @Override
        public void execute() {
            memento = draft.createMemento();
            draft.changeInstallments(amounts);
        }

        /**
         * 恢复命令执行前草稿状态。
         */
        @Override
        public void undo() {
            draft.restore(memento);
        }
    }

    /**
     * 封装和解备注修改及基于 Memento 的撤销。
     */
    static class ChangeRemarkCommand implements EditCommand {
        /** 被编辑草稿。 */
        private final SettlementDraft draft;
        /** 新备注内容。 */
        private final String remark;
        /** 命令执行前的状态。 */
        private SettlementDraft.Memento memento;

        /**
         * 创建备注修改命令。
         *
         * @param draft 被编辑草稿
         * @param remark 新备注
         */
        ChangeRemarkCommand(SettlementDraft draft, String remark) {
            this.draft = draft;
            this.remark = remark;
        }

        /**
         * 保存旧状态后修改备注。
         */
        @Override
        public void execute() {
            memento = draft.createMemento();
            draft.changeRemark(remark);
        }

        /**
         * 恢复命令执行前草稿状态。
         */
        @Override
        public void undo() {
            draft.restore(memento);
        }
    }

    /**
     * 命令调用者兼历史管理者，执行命令并按 LIFO 顺序撤销已成功命令。
     */
    static class CommandHistory {
        /** 已成功执行且尚未撤销的命令栈。 */
        private final Deque<EditCommand> commands = new ArrayDeque<EditCommand>();

        /**
         * 执行编辑命令并将成功命令压入撤销栈。
         *
         * @param command 待执行编辑命令
         */
        void execute(EditCommand command) {
            command.execute();
            commands.push(command);
            System.out.println("[备忘录/v4] CommandHistory.execute ---> 命令执行成功并进入撤销栈");
        }

        /**
         * 撤销最近一条命令，仅在撤销成功后移除命令。
         *
         * @throws IllegalStateException 没有可撤销命令时抛出
         */
        void undo() {
            if (commands.isEmpty()) {
                throw new IllegalStateException("没有可撤销的编辑命令");
            }
            EditCommand command = commands.peek();
            command.undo();
            commands.pop();
            System.out.println("[备忘录/v4] CommandHistory.undo ---> 最近编辑命令已撤销");
        }
    }

    /**
     * 定义 UI 编辑组件如何向工作台协调者报告操作。
     */
    interface SettlementEditorMediator {
        /**
         * 接收首付款修改请求。
         *
         * @param amount 目标首付款
         */
        void changeDownPayment(BigDecimal amount);

        /**
         * 接收分期修改请求。
         *
         * @param amounts 目标分期金额
         */
        void changeInstallments(List<BigDecimal> amounts);

        /**
         * 接收备注修改请求。
         *
         * @param remark 新备注
         */
        void changeRemark(String remark);

        /**
         * 接收撤销请求。
         */
        void undo();

        /**
         * 接收审批请求。
         */
        void approve();
    }

    /**
     * 具体中介者协调编辑组件、CommandHistory 与草稿生命周期状态。
     */
    static class DefaultSettlementEditorMediator implements SettlementEditorMediator {
        /** 当前编辑的和解草稿。 */
        private final SettlementDraft draft;
        /** 管理编辑命令及撤销顺序。 */
        private final CommandHistory commandHistory;

        /**
         * 创建单个编辑工作台的协调者。
         *
         * @param draft 当前和解草稿
         * @param commandHistory 命令调用者与历史栈
         */
        DefaultSettlementEditorMediator(SettlementDraft draft, CommandHistory commandHistory) {
            this.draft = draft;
            this.commandHistory = commandHistory;
        }

        /**
         * 将首付款编辑请求封装成命令后执行。
         *
         * @param amount 新首付款
         */
        @Override
        public void changeDownPayment(BigDecimal amount) {
            commandHistory.execute(new ChangeDownPaymentCommand(draft, amount));
        }

        /**
         * 将分期编辑请求封装成命令后执行。
         *
         * @param amounts 新分期金额
         */
        @Override
        public void changeInstallments(List<BigDecimal> amounts) {
            commandHistory.execute(new ChangeInstallmentsCommand(draft, amounts));
        }

        /**
         * 将备注编辑请求封装成命令后执行。
         *
         * @param remark 新备注
         */
        @Override
        public void changeRemark(String remark) {
            commandHistory.execute(new ChangeRemarkCommand(draft, remark));
        }

        /**
         * 仅在草稿状态下调用命令历史撤销。
         */
        @Override
        public void undo() {
            if (!draft.isDraft()) {
                System.out.println("[备忘录/v4] DefaultSettlementEditorMediator.undo ---> 已审批方案不能使用草稿撤销");
                throw new IllegalStateException("方案已审批，需走业务变更流程");
            }
            commandHistory.undo();
        }

        /**
         * 推进和解方案审批状态。
         */
        @Override
        public void approve() {
            draft.approve();
        }
    }

    /**
     * 首付款编辑组件仅向中介者提交用户输入。
     */
    static class DownPaymentEditor {
        /** 编辑工作台中介者接口。 */
        private final SettlementEditorMediator mediator;

        /**
         * 创建首付款编辑组件。
         *
         * @param mediator 编辑工作台中介者
         */
        DownPaymentEditor(SettlementEditorMediator mediator) {
            this.mediator = mediator;
        }

        /**
         * 请求修改首付款。
         *
         * @param amount 新首付款
         */
        void changeTo(BigDecimal amount) {
            System.out.println("[备忘录/v4] DownPaymentEditor.changeTo ---> 提交首付款修改请求");
            mediator.changeDownPayment(amount);
        }
    }

    /**
     * 分期编辑组件仅向中介者提交用户输入。
     */
    static class InstallmentEditor {
        /** 编辑工作台中介者接口。 */
        private final SettlementEditorMediator mediator;

        /**
         * 创建分期编辑组件。
         *
         * @param mediator 编辑工作台中介者
         */
        InstallmentEditor(SettlementEditorMediator mediator) {
            this.mediator = mediator;
        }

        /**
         * 请求修改分期金额。
         *
         * @param amounts 新分期金额列表
         */
        void changeTo(List<BigDecimal> amounts) {
            System.out.println("[备忘录/v4] InstallmentEditor.changeTo ---> 提交分期修改请求");
            mediator.changeInstallments(amounts);
        }
    }

    /**
     * 备注编辑组件只负责提交修改事件。
     */
    static class RemarkEditor {
        /** 编辑工作台中介者接口。 */
        private final SettlementEditorMediator mediator;

        /**
         * 创建备注编辑组件。
         *
         * @param mediator 编辑工作台中介者
         */
        RemarkEditor(SettlementEditorMediator mediator) {
            this.mediator = mediator;
        }

        /**
         * 请求修改和解备注。
         *
         * @param remark 新备注
         */
        void changeTo(String remark) {
            System.out.println("[备忘录/v4] RemarkEditor.changeTo ---> 提交备注修改请求");
            mediator.changeRemark(remark);
        }
    }

    /**
     * 撤销按钮组件只向中介者发送撤销请求。
     */
    static class UndoButton {
        /** 编辑工作台中介者接口。 */
        private final SettlementEditorMediator mediator;

        /**
         * 创建撤销按钮。
         *
         * @param mediator 编辑工作台中介者
         */
        UndoButton(SettlementEditorMediator mediator) {
            this.mediator = mediator;
        }

        /**
         * 请求撤销最近一次编辑。
         */
        void click() {
            System.out.println("[备忘录/v4] UndoButton.click ---> 提交撤销请求");
            mediator.undo();
        }
    }

    /**
     * 审批按钮组件只向中介者发送审批请求。
     */
    static class ApprovalButton {
        /** 编辑工作台中介者接口。 */
        private final SettlementEditorMediator mediator;

        /**
         * 创建审批按钮。
         *
         * @param mediator 编辑工作台中介者
         */
        ApprovalButton(SettlementEditorMediator mediator) {
            this.mediator = mediator;
        }

        /**
         * 请求推进方案审批状态。
         */
        void approve() {
            System.out.println("[备忘录/v4] ApprovalButton.approve ---> 提交审批请求");
            mediator.approve();
        }
    }

    /**
     * 用内存 Map 模拟数据库当前表，只保存最新记录及乐观锁版本号。
     */
    static class InMemoryPlanStore {
        /** 当前方案记录，不保存历史版本。 */
        private final Map<Long, VersionedPlan> currentPlans = new HashMap<Long, VersionedPlan>();

        /**
         * 创建方案当前记录，初始版本设为 1。
         *
         * @param caseId 虚构案件编号
         * @param amount 当前首付款
         */
        void create(long caseId, BigDecimal amount) {
            currentPlans.put(caseId, new VersionedPlan(amount, 1L));
            System.out.println("[备忘录/v4] InMemoryPlanStore.create ---> 创建当前方案模拟记录，version=1");
        }

        /**
         * 使用乐观锁版本号更新当前方案；版本号不匹配时拒绝覆盖。
         *
         * @param caseId 虚构案件编号
         * @param expectedVersion 调用方读取的预期版本号
         * @param amount 新首付款
         * @return 更新成功时返回 true，版本冲突时返回 false
         */
        boolean updateAmount(long caseId, long expectedVersion, BigDecimal amount) {
            VersionedPlan plan = currentPlans.get(caseId);
            if (plan.version != expectedVersion) {
                System.out.println("[备忘录/v4] InMemoryPlanStore.updateAmount ---> version 冲突，拒绝覆盖当前记录");
                return false;
            }
            plan.amount = amount;
            plan.version++;
            System.out.println("[备忘录/v4] InMemoryPlanStore.updateAmount ---> 更新当前值并递增 version=" + plan.version);
            return true;
        }

        /**
         * 创建只读当前值快照，供单独的历史归档对象保存。
         *
         * @param caseId 虚构案件编号
         * @param operatorId 模拟快照操作员编号
         * @param createdAt 模拟快照创建时间
         * @return 当前方案值和版本的快照
         */
        StoredPlanSnapshot snapshot(long caseId, String operatorId, String createdAt) {
            VersionedPlan plan = currentPlans.get(caseId);
            return new StoredPlanSnapshot(caseId, plan.amount, plan.version,
                    operatorId, createdAt);
        }

        /**
         * 输出当前表中的最新值。
         *
         * @param caseId 虚构案件编号
         */
        void print(long caseId) {
            VersionedPlan plan = currentPlans.get(caseId);
            System.out.println("[备忘录/v4] InMemoryPlanStore.print ---> 案件：" + caseId
                    + "，当前首付：" + plan.amount + "，version：" + plan.version);
        }
    }

    /**
     * 当前方案记录，仅持有最新值及并发版本号。
     */
    static class VersionedPlan {
        /** 当前首付款金额。 */
        private BigDecimal amount;
        /** 用于并发更新检查的版本号，不是状态历史。 */
        private long version;

        /**
         * 创建当前方案记录。
         *
         * @param amount 首付款金额
         * @param version 乐观锁版本号
         */
        VersionedPlan(BigDecimal amount, long version) {
            this.amount = amount;
            this.version = version;
        }
    }

    /**
     * 数据库历史表概念的内存模拟，独立保存多份方案快照。
     */
    static class PlanHistoryArchive {
        /** 已保存的不可变历史快照。 */
        private final List<StoredPlanSnapshot> snapshots = new ArrayList<StoredPlanSnapshot>();

        /**
         * 保存一个当前方案快照；只追加历史，不回写当前记录。
         *
         * @param snapshot 当前方案值、版本及教学审计信息快照
         */
        void save(StoredPlanSnapshot snapshot) {
            snapshots.add(snapshot);
            System.out.println("[备忘录/v4] PlanHistoryArchive.save ---> 单独保存方案历史快照 version=" + snapshot.version);
        }

        /**
         * 输出归档的所有历史状态。
         */
        void printAll() {
            for (StoredPlanSnapshot snapshot : snapshots) {
                System.out.println("[备忘录/v4] PlanHistoryArchive.printAll ---> " + snapshot);
            }
        }
    }

    /**
     * 持久化历史方案的不可变教学记录。
     */
    static class StoredPlanSnapshot {
        /** 虚构案件编号。 */
        private final long caseId;
        /** 保存时首付款金额。 */
        private final BigDecimal amount;
        /** 保存时的业务版本号。 */
        private final long version;
        /** 记录该历史版本的模拟操作员编号。 */
        private final String operatorId;
        /** 模拟历史表中的创建时间。 */
        private final String createdAt;

        /**
         * 创建持久化历史快照。
         *
         * @param caseId 虚构案件编号
         * @param amount 保存时首付款
         * @param version 保存时版本号
         * @param operatorId 模拟操作员编号
         * @param createdAt 模拟快照创建时间
         */
        StoredPlanSnapshot(long caseId, BigDecimal amount, long version,
                String operatorId, String createdAt) {
            this.caseId = caseId;
            this.amount = amount;
            this.version = version;
            this.operatorId = operatorId;
            this.createdAt = createdAt;
        }

        /**
         * 格式化输出快照字段用于演示。
         *
         * @return 案件、金额及版本说明
         */
        @Override
        public String toString() {
            return "案件：" + caseId + "，snapshot_json：{\"downPayment\":\""
                    + amount + "\"}，保存版本：" + version + "，操作员：" + operatorId
                    + "，创建时间：" + createdAt;
        }
    }
}
