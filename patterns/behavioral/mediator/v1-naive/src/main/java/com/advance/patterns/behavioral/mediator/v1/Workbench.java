package com.advance.patterns.behavioral.mediator.v1;

/**
 * 通过业务组件直接互相调用完成催收外呼工作台协作的最初版本。
 *
 * @author advance
 * @since 2026-10-29 09:32:02
 */
public class Workbench {
    /**
     * 运行正常案件和禁止外呼案件两个教学场景。
     */
    public void runScenarios() {
        ScriptPanel scriptPanel = new ScriptPanel();
        ComplianceService complianceService = new ComplianceService();
        DialGateway dialGateway = new DialGateway();
        AuditService auditService = new AuditService();
        CaseSelector selector = new CaseSelector(scriptPanel, complianceService, auditService);
        DialPanel dialPanel = new DialPanel(selector, complianceService, dialGateway, auditService);
        selector.registerDialPanel(dialPanel);
        CallCallback callback = new CallCallback(selector, scriptPanel, auditService);

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
     * 案件选择面板；本版本直接协调多个工作台组件和服务。
     */
    static class CaseSelector {
        /** 当前选中案件编号；仅用于单个教学工作台实例。 */
        private Long selectedCaseId;
        /** 话术展示面板。 */
        private final ScriptPanel scriptPanel;
        /** 模拟外呼合规校验服务。 */
        private final ComplianceService complianceService;
        /** 模拟审计服务。 */
        private final AuditService auditService;
        /** 外呼面板需要回写控件状态。 */
        private DialPanel dialPanel;

        /**
         * 创建案件选择面板及其直接依赖。
         *
         * @param scriptPanel 话术展示面板
         * @param complianceService 合规校验服务
         * @param auditService 审计服务
         */
        CaseSelector(ScriptPanel scriptPanel, ComplianceService complianceService, AuditService auditService) {
            this.scriptPanel = scriptPanel;
            this.complianceService = complianceService;
            this.auditService = auditService;
        }

        /**
         * 注册外呼面板以展示案件选择后的直接组件耦合。
         *
         * @param dialPanel 外呼面板
         */
        void registerDialPanel(DialPanel dialPanel) {
            this.dialPanel = dialPanel;
        }

        /**
         * 选择案件并直接协调话术、合规、外呼面板与审计行为。
         *
         * @param caseId 模拟案件编号
         */
        void select(long caseId) {
            selectedCaseId = caseId;
            System.out.println("[中介者/v1] CaseSelector.select ---> 作业员选择案件：" + caseId);
            scriptPanel.showScript(caseId);
            boolean allowed = complianceService.canDial(caseId);
            dialPanel.setEnabled(allowed);
            auditService.record("选择案件：" + caseId);
        }

        /**
         * 获取当前选择的案件，供直接依赖本面板的外呼组件读取。
         *
         * @return 当前案件编号；尚未选择时返回 null
         */
        Long getSelectedCaseId() {
            return selectedCaseId;
        }
    }

    /**
     * 外呼面板直接读取案件选择状态并调用多个协作者。
     */
    static class DialPanel {
        /** 案件选择面板，体现协作对象之间的直接依赖。 */
        private final CaseSelector selector;
        /** 合规校验服务。 */
        private final ComplianceService complianceService;
        /** 模拟外呼服务。 */
        private final DialGateway dialGateway;
        /** 审计服务。 */
        private final AuditService auditService;
        /** 当前面板是否允许发起外呼。 */
        private boolean enabled;

        /**
         * 创建外呼面板。
         *
         * @param selector 案件选择面板
         * @param complianceService 合规校验服务
         * @param dialGateway 模拟外呼服务
         * @param auditService 审计服务
         */
        DialPanel(CaseSelector selector, ComplianceService complianceService, DialGateway dialGateway, AuditService auditService) {
            this.selector = selector;
            this.complianceService = complianceService;
            this.dialGateway = dialGateway;
            this.auditService = auditService;
        }

        /**
         * 更新外呼控件可用状态。
         *
         * @param enabled true 表示面板可用
         */
        void setEnabled(boolean enabled) {
            this.enabled = enabled;
            System.out.println("[中介者/v1] DialPanel.setEnabled ---> 外呼按钮：" + (enabled ? "可用" : "禁用"));
        }

        /**
         * 处理外呼点击；执行前重新检查合规，不以按钮状态代替服务端校验。
         */
        void click() {
            Long caseId = selector.getSelectedCaseId();
            if (caseId == null) {
                System.out.println("[中介者/v1] DialPanel.click ---> 请先选择案件");
                return;
            }
            boolean allowed = complianceService.canDial(caseId.longValue());
            if (!allowed) {
                setEnabled(false);
                System.out.println("[中介者/v1] DialPanel.click ---> 合规限制，禁止外呼");
                return;
            }
            if (!enabled) {
                System.out.println("[中介者/v1] DialPanel.click ---> 当前外呼按钮不可用");
                return;
            }
            dialGateway.dial(caseId.longValue());
            auditService.record("发起外呼：" + caseId);
        }
    }

    /**
     * 通话回调直接通知其他组件更新展示和审计。
     */
    static class CallCallback {
        /** 案件选择面板，用于判断回调是否属于当前案件。 */
        private final CaseSelector selector;
        /** 话术展示面板。 */
        private final ScriptPanel scriptPanel;
        /** 审计服务。 */
        private final AuditService auditService;

        /**
         * 创建通话回调组件。
         *
         * @param selector 案件选择面板
         * @param scriptPanel 话术面板
         * @param auditService 审计服务
         */
        CallCallback(CaseSelector selector, ScriptPanel scriptPanel, AuditService auditService) {
            this.selector = selector;
            this.scriptPanel = scriptPanel;
            this.auditService = auditService;
        }

        /**
         * 记录通话结束，并仅更新仍处于当前工作台的案件展示。
         *
         * @param caseId 已结束通话的案件编号
         */
        void callFinished(long caseId) {
            auditService.record("通话结束：" + caseId);
            Long selectedCaseId = selector.getSelectedCaseId();
            if (selectedCaseId != null && selectedCaseId.longValue() == caseId) {
                scriptPanel.showCallFinished(caseId);
            }
        }
    }

    /**
     * 负责展示案件话术和通话状态的模拟面板。
     */
    static class ScriptPanel {
        /**
         * 展示案件对应的话术。
         *
         * @param caseId 案件编号
         */
        void showScript(long caseId) {
            System.out.println("[中介者/v1] ScriptPanel.showScript ---> 加载案件话术：" + caseId);
        }

        /**
         * 展示通话结束状态。
         *
         * @param caseId 案件编号
         */
        void showCallFinished(long caseId) {
            System.out.println("[中介者/v1] ScriptPanel.showCallFinished ---> 展示通话结束状态：" + caseId);
        }
    }

    /**
     * 按虚构规则判断案件是否允许外呼的模拟服务。
     */
    static class ComplianceService {
        /**
         * 检查模拟案件的外呼许可。
         *
         * @param caseId 案件编号
         * @return 案件编号不是 10002 时返回 true
         */
        boolean canDial(long caseId) {
            boolean allowed = caseId != 10002L;
            System.out.println("[中介者/v1] ComplianceService.canDial ---> 案件" + caseId + (allowed ? "允许" : "禁止") + "外呼");
            return allowed;
        }
    }

    /**
     * 模拟外呼客户端，不连接真实外呼平台。
     */
    static class DialGateway {
        /**
         * 输出模拟外呼调用路径。
         *
         * @param caseId 案件编号
         */
        void dial(long caseId) {
            System.out.println("[中介者/v1] DialGateway.dial ---> 模拟调用外呼平台：" + caseId);
        }
    }

    /**
     * 模拟审计记录服务。
     */
    static class AuditService {
        /**
         * 输出一条模拟审计记录。
         *
         * @param message 审计内容
         */
        void record(String message) {
            System.out.println("[中介者/v1] AuditService.record ---> 审计日志：" + message);
        }
    }
}
