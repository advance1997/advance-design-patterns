package com.advance.patterns.behavioral.mediator.v2;

/**
 * 将工作台组件间的直接协作提取到一个具体协调者中的重构版本。
 *
 * @author advance
 * @since 2026-10-29 09:32:02
 */
public class Workbench {
    /**
     * 运行正常外呼、通话回调和合规拒绝场景。
     */
    public void runScenarios() {
        ScriptPanel scriptPanel = new ScriptPanel();
        ComplianceService complianceService = new ComplianceService();
        DialGateway dialGateway = new DialGateway();
        AuditService auditService = new AuditService();
        WorkbenchCoordinator coordinator = new WorkbenchCoordinator(scriptPanel, complianceService, dialGateway, auditService);
        CaseSelector selector = new CaseSelector(coordinator);
        DialPanel dialPanel = new DialPanel(coordinator);
        coordinator.registerDialPanel(dialPanel);
        CallCallback callback = new CallCallback(coordinator);

        System.out.println("=== 场景一：正常案件允许外呼 ===");
        selector.select(10001L);
        dialPanel.click();
        callback.callFinished(10001L);

        System.out.println("\n=== 场景二：合规限制案件禁止外呼 ===");
        selector.select(10002L);
        System.out.println("旧案件通话回调到达：");
        callback.callFinished(10001L);
        dialPanel.click();
    }

    /**
     * 案件选择面板只向具体协调者报告用户操作。
     */
    static class CaseSelector {
        /** 负责协调工作台组件的具体对象。 */
        private final WorkbenchCoordinator coordinator;

        /**
         * 创建案件选择面板。
         *
         * @param coordinator 工作台协调者
         */
        CaseSelector(WorkbenchCoordinator coordinator) {
            this.coordinator = coordinator;
        }

        /**
         * 报告案件选择事件，不直接调用其他面板或服务。
         *
         * @param caseId 模拟案件编号
         */
        void select(long caseId) {
            System.out.println("[中介者/v2] CaseSelector.select ---> 通知工作台协调者选择案件：" + caseId);
            coordinator.onCaseSelected(caseId);
        }
    }

    /**
     * 外呼面板向具体协调者报告点击操作。
     */
    static class DialPanel {
        /** 负责协调外呼动作的具体对象。 */
        private final WorkbenchCoordinator coordinator;
        /** 由协调者根据合规结果设置的控件状态。 */
        private boolean enabled;

        /**
         * 创建外呼面板。
         *
         * @param coordinator 工作台协调者
         */
        DialPanel(WorkbenchCoordinator coordinator) {
            this.coordinator = coordinator;
        }

        /**
         * 由协调者更新外呼控件状态。
         *
         * @param enabled true 表示控件可用
         */
        void setEnabled(boolean enabled) {
            this.enabled = enabled;
            System.out.println("[中介者/v2] DialPanel.setEnabled ---> 外呼按钮：" + (enabled ? "可用" : "禁用"));
        }

        /**
         * 向协调者提交点击事件；本面板不自行访问案件或合规服务。
         */
        void click() {
            System.out.println("[中介者/v2] DialPanel.click ---> 通知工作台协调者发起外呼");
            coordinator.onDialRequested();
        }

        /**
         * 查询控件状态，供协调者执行前进行防御性校验。
         *
         * @return true 表示控件当前可用
         */
        boolean isEnabled() {
            return enabled;
        }
    }

    /**
     * 模拟外呼平台通话结束后发出的回调组件。
     */
    static class CallCallback {
        /** 负责协调回调响应的具体对象。 */
        private final WorkbenchCoordinator coordinator;

        /**
         * 创建通话回调组件。
         *
         * @param coordinator 工作台协调者
         */
        CallCallback(WorkbenchCoordinator coordinator) {
            this.coordinator = coordinator;
        }

        /**
         * 报告案件通话结束事件。
         *
         * @param caseId 通话结束的案件编号
         */
        void callFinished(long caseId) {
            System.out.println("[中介者/v2] CallCallback.callFinished ---> 通知工作台协调者通话结束：" + caseId);
            coordinator.onCallFinished(caseId);
        }
    }

    /**
     * 将工作台协作顺序集中管理的具体协调者；本版本尚未抽象 Mediator 接口。
     */
    static class WorkbenchCoordinator {
        /** 展示案件话术及通话结果的面板。 */
        private final ScriptPanel scriptPanel;
        /** 模拟外呼合规校验服务。 */
        private final ComplianceService complianceService;
        /** 模拟外呼客户端。 */
        private final DialGateway dialGateway;
        /** 模拟审计服务。 */
        private final AuditService auditService;
        /** 当前工作台外呼面板。 */
        private DialPanel dialPanel;
        /** 当前工作台选中的案件；该有状态协调者仅用于单工作台演示。 */
        private Long selectedCaseId;

        /**
         * 创建协调者及其业务能力依赖。
         *
         * @param scriptPanel 话术面板
         * @param complianceService 合规校验服务
         * @param dialGateway 模拟外呼客户端
         * @param auditService 审计服务
         */
        WorkbenchCoordinator(ScriptPanel scriptPanel, ComplianceService complianceService, DialGateway dialGateway, AuditService auditService) {
            this.scriptPanel = scriptPanel;
            this.complianceService = complianceService;
            this.dialGateway = dialGateway;
            this.auditService = auditService;
        }

        /**
         * 注册需要更新控件状态的外呼面板。
         *
         * @param dialPanel 外呼面板
         */
        void registerDialPanel(DialPanel dialPanel) {
            this.dialPanel = dialPanel;
        }

        /**
         * 协调案件选择后的话术、合规、控件状态和审计操作。
         *
         * @param caseId 被选择的案件编号
         */
        void onCaseSelected(long caseId) {
            selectedCaseId = caseId;
            System.out.println("[中介者/v2] WorkbenchCoordinator.onCaseSelected ---> 保存当前案件：" + caseId);
            scriptPanel.showScript(caseId);
            dialPanel.setEnabled(complianceService.canDial(caseId));
            auditService.record("选择案件：" + caseId);
        }

        /**
         * 协调外呼请求并在执行时重新校验合规规则。
         */
        void onDialRequested() {
            if (selectedCaseId == null) {
                System.out.println("[中介者/v2] WorkbenchCoordinator.onDialRequested ---> 请先选择案件");
                return;
            }
            long caseId = selectedCaseId.longValue();
            if (!complianceService.canDial(caseId)) {
                dialPanel.setEnabled(false);
                System.out.println("[中介者/v2] WorkbenchCoordinator.onDialRequested ---> 合规限制，禁止外呼");
                return;
            }
            if (!dialPanel.isEnabled()) {
                System.out.println("[中介者/v2] WorkbenchCoordinator.onDialRequested ---> 当前外呼按钮不可用");
                return;
            }
            dialGateway.dial(caseId);
            auditService.record("发起外呼：" + caseId);
        }

        /**
         * 记录通话结束，仅当回调仍属于当前选中案件时刷新面板。
         *
         * @param caseId 通话结束的案件编号
         */
        void onCallFinished(long caseId) {
            auditService.record("通话结束：" + caseId);
            if (selectedCaseId != null && selectedCaseId.longValue() == caseId) {
                scriptPanel.showCallFinished(caseId);
            }
        }
    }

    /**
     * 展示模拟案件话术和通话结束状态。
     */
    static class ScriptPanel {
        /**
         * 展示案件对应话术。
         *
         * @param caseId 案件编号
         */
        void showScript(long caseId) {
            System.out.println("[中介者/v2] ScriptPanel.showScript ---> 加载案件话术：" + caseId);
        }

        /**
         * 展示通话结束状态。
         *
         * @param caseId 案件编号
         */
        void showCallFinished(long caseId) {
            System.out.println("[中介者/v2] ScriptPanel.showCallFinished ---> 展示通话结束状态：" + caseId);
        }
    }

    /**
     * 通过虚构规则模拟外呼合规校验。
     */
    static class ComplianceService {
        /**
         * 查询案件是否允许外呼。
         *
         * @param caseId 案件编号
         * @return 案件编号不是 10002 时返回 true
         */
        boolean canDial(long caseId) {
            boolean allowed = caseId != 10002L;
            System.out.println("[中介者/v2] ComplianceService.canDial ---> 案件" + caseId + (allowed ? "允许" : "禁止") + "外呼");
            return allowed;
        }
    }

    /**
     * 模拟外呼客户端，不执行真实呼叫。
     */
    static class DialGateway {
        /**
         * 输出模拟外呼路径。
         *
         * @param caseId 案件编号
         */
        void dial(long caseId) {
            System.out.println("[中介者/v2] DialGateway.dial ---> 模拟调用外呼平台：" + caseId);
        }
    }

    /**
     * 模拟审计日志服务。
     */
    static class AuditService {
        /**
         * 输出审计事件。
         *
         * @param message 审计内容
         */
        void record(String message) {
            System.out.println("[中介者/v2] AuditService.record ---> 审计日志：" + message);
        }
    }
}
