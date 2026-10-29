package com.advance.patterns.behavioral.mediator.v3;

/**
 * 通过中介者接口解耦催收外呼工作台协作对象的完整示例。
 *
 * @author advance
 * @since 2026-10-29 09:32:02
 */
public class Workbench {
    /**
     * 运行正常外呼、通话结束回调和合规拒绝场景。
     */
    public void runScenarios() {
        ScriptPanel scriptPanel = new ScriptPanel();
        ComplianceService complianceService = new ComplianceService();
        DialGateway dialGateway = new DialGateway();
        AuditService auditService = new AuditService();
        DefaultWorkbenchMediator mediator = new DefaultWorkbenchMediator(scriptPanel, complianceService, dialGateway, auditService);
        CaseSelector selector = new CaseSelector(mediator);
        DialPanel dialPanel = new DialPanel(mediator);
        mediator.registerDialPanel(dialPanel);
        CallCallback callback = new CallCallback(mediator);

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
     * 定义工作台协作对象可发出的交互事件。
     *
     * <p>接口表达对象之间需要协调的行为，不暴露具体组件和业务服务实现。</p>
     */
    interface WorkbenchMediator {
        /**
         * 响应案件选择事件。
         *
         * @param caseId 被选择的案件编号
         */
        void onCaseSelected(long caseId);

        /**
         * 响应外呼请求事件。
         */
        void onDialRequested();

        /**
         * 响应通话结束事件。
         *
         * @param caseId 通话结束的案件编号
         */
        void onCallFinished(long caseId);
    }

    /**
     * 案件选择组件仅向中介者报告案件选择，不直接调用其他面板。
     */
    static class CaseSelector {
        /** 定义工作台协作协议的中介者。 */
        private final WorkbenchMediator mediator;

        /**
         * 创建案件选择组件。
         *
         * @param mediator 工作台中介者接口
         */
        CaseSelector(WorkbenchMediator mediator) {
            this.mediator = mediator;
        }

        /**
         * 报告作业员选择案件的交互。
         *
         * @param caseId 模拟案件编号
         */
        void select(long caseId) {
            System.out.println("[中介者/v3] CaseSelector.select ---> 通知中介者选择案件：" + caseId);
            mediator.onCaseSelected(caseId);
        }
    }

    /**
     * 外呼面板仅通过中介者提交用户请求和接收显示状态。
     */
    static class DialPanel {
        /** 定义面板协作协议的中介者。 */
        private final WorkbenchMediator mediator;
        /** 当前控件是否可用。 */
        private boolean enabled;

        /**
         * 创建外呼面板。
         *
         * @param mediator 工作台中介者接口
         */
        DialPanel(WorkbenchMediator mediator) {
            this.mediator = mediator;
        }

        /**
         * 由具体中介者设置控件状态。
         *
         * @param enabled true 表示当前可发起外呼
         */
        void setEnabled(boolean enabled) {
            this.enabled = enabled;
            System.out.println("[中介者/v3] DialPanel.setEnabled ---> 外呼按钮：" + (enabled ? "可用" : "禁用"));
        }

        /**
         * 将点击行为交给中介者协调。
         */
        void click() {
            System.out.println("[中介者/v3] DialPanel.click ---> 通知中介者发起外呼");
            mediator.onDialRequested();
        }

        /**
         * 获取界面状态供中介者进行额外防御性校验。
         *
         * @return true 表示外呼按钮可用
         */
        boolean isEnabled() {
            return enabled;
        }
    }

    /**
     * 通话回调组件通过中介者报告通话结束，不直接更新其他组件。
     */
    static class CallCallback {
        /** 定义回调处理协议的中介者。 */
        private final WorkbenchMediator mediator;

        /**
         * 创建通话回调组件。
         *
         * @param mediator 工作台中介者接口
         */
        CallCallback(WorkbenchMediator mediator) {
            this.mediator = mediator;
        }

        /**
         * 报告指定案件通话结束。
         *
         * @param caseId 通话结束的案件编号
         */
        void callFinished(long caseId) {
            System.out.println("[中介者/v3] CallCallback.callFinished ---> 通知中介者通话结束：" + caseId);
            mediator.onCallFinished(caseId);
        }
    }

    /**
     * 根据工作台事件集中协调各组件和业务服务。
     *
     * <p>该示例保存当前工作台案件状态，因此仅适用于单工作台演示，不能作为多用户共享的有状态单例。</p>
     */
    static class DefaultWorkbenchMediator implements WorkbenchMediator {
        /** 负责展示话术和通话结果的面板。 */
        private final ScriptPanel scriptPanel;
        /** 模拟外呼合规校验服务。 */
        private final ComplianceService complianceService;
        /** 模拟外呼客户端。 */
        private final DialGateway dialGateway;
        /** 模拟审计服务。 */
        private final AuditService auditService;
        /** 接收协调结果的外呼面板。 */
        private DialPanel dialPanel;
        /** 当前工作台选择的案件编号。 */
        private Long selectedCaseId;

        /**
         * 创建具体中介者及其独立业务能力依赖。
         *
         * @param scriptPanel 话术展示面板
         * @param complianceService 合规校验服务
         * @param dialGateway 模拟外呼客户端
         * @param auditService 审计服务
         */
        DefaultWorkbenchMediator(ScriptPanel scriptPanel, ComplianceService complianceService, DialGateway dialGateway, AuditService auditService) {
            this.scriptPanel = scriptPanel;
            this.complianceService = complianceService;
            this.dialGateway = dialGateway;
            this.auditService = auditService;
        }

        /**
         * 注册需要显示合规校验结果的外呼面板。
         *
         * @param dialPanel 外呼面板
         */
        void registerDialPanel(DialPanel dialPanel) {
            this.dialPanel = dialPanel;
        }

        /**
         * 协调案件选择后的话术加载、合规校验、按钮更新和审计。
         *
         * @param caseId 被选择的案件编号
         */
        @Override
        public void onCaseSelected(long caseId) {
            selectedCaseId = caseId;
            System.out.println("[中介者/v3] DefaultWorkbenchMediator.onCaseSelected ---> 协调案件选择：" + caseId);
            scriptPanel.showScript(caseId);
            boolean allowed = complianceService.canDial(caseId);
            dialPanel.setEnabled(allowed);
            auditService.record("选择案件：" + caseId);
        }

        /**
         * 重新执行合规检查后决定是否调用模拟外呼客户端。
         */
        @Override
        public void onDialRequested() {
            if (selectedCaseId == null) {
                System.out.println("[中介者/v3] DefaultWorkbenchMediator.onDialRequested ---> 请先选择案件");
                return;
            }
            long caseId = selectedCaseId.longValue();
            boolean allowed = complianceService.canDial(caseId);
            if (!allowed) {
                dialPanel.setEnabled(false);
                System.out.println("[中介者/v3] DefaultWorkbenchMediator.onDialRequested ---> 合规限制，禁止外呼");
                return;
            }
            if (!dialPanel.isEnabled()) {
                System.out.println("[中介者/v3] DefaultWorkbenchMediator.onDialRequested ---> 当前外呼按钮不可用");
                return;
            }
            dialGateway.dial(caseId);
            auditService.record("发起外呼：" + caseId);
        }

        /**
         * 记录通话结束；旧案件回调不会刷新当前选中案件的话术面板。
         *
         * @param caseId 通话结束的案件编号
         */
        @Override
        public void onCallFinished(long caseId) {
            auditService.record("通话结束：" + caseId);
            if (selectedCaseId != null && selectedCaseId.longValue() == caseId) {
                scriptPanel.showCallFinished(caseId);
            }
        }
    }

    /**
     * 展示案件话术与通话结果的模拟面板。
     */
    static class ScriptPanel {
        /**
         * 展示指定案件话术。
         *
         * @param caseId 案件编号
         */
        void showScript(long caseId) {
            System.out.println("[中介者/v3] ScriptPanel.showScript ---> 加载案件话术：" + caseId);
        }

        /**
         * 展示通话结束状态。
         *
         * @param caseId 案件编号
         */
        void showCallFinished(long caseId) {
            System.out.println("[中介者/v3] ScriptPanel.showCallFinished ---> 展示通话结束状态：" + caseId);
        }
    }

    /**
     * 按教学数据模拟外呼合规校验。
     */
    static class ComplianceService {
        /**
         * 判断给定案件是否允许外呼。
         *
         * @param caseId 案件编号
         * @return 案件编号不是 10002 时返回 true
         */
        boolean canDial(long caseId) {
            boolean allowed = caseId != 10002L;
            System.out.println("[中介者/v3] ComplianceService.canDial ---> 案件" + caseId + (allowed ? "允许" : "禁止") + "外呼");
            return allowed;
        }
    }

    /**
     * 模拟外呼客户端，不连接外部系统。
     */
    static class DialGateway {
        /**
         * 输出模拟外呼执行路径。
         *
         * @param caseId 案件编号
         */
        void dial(long caseId) {
            System.out.println("[中介者/v3] DialGateway.dial ---> 模拟调用外呼平台：" + caseId);
        }
    }

    /**
     * 模拟操作审计服务。
     */
    static class AuditService {
        /**
         * 输出操作审计内容。
         *
         * @param message 审计内容
         */
        void record(String message) {
            System.out.println("[中介者/v3] AuditService.record ---> 审计日志：" + message);
        }
    }
}
