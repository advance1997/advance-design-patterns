# 中介者模式：催收外呼工作台

## 专题来源与案例

本实践对应《01-1｜设计模式专项》第二十二讲“中介者模式”。专题以催收外呼工作台为例：作业员选择案件后加载话术、执行合规检查、更新外呼按钮并记录审计；点击外呼时重新校验；通话回调记录结果，并避免旧案件回调更新当前案件。

本实践使用模拟组件和虚构案件编号：10001 表示允许外呼，10002 表示被教学合规规则限制。没有真实外呼、客户通知或远程服务调用。当前示例没有引入 Spring，因为专题重点是识别组件协作关系；特别要注意最终中介者保留当前案件状态，不能直接作为所有作业员共享的有状态 Spring singleton。

## 版本演进

| 版本 | 做法 | 问题或收获 |
| --- | --- | --- |
| v1-naive | `CaseSelector` 直接调用话术、合规、外呼面板和审计；外呼面板及通话回调也直接持有其他组件 | 组件承担协调职责，逐渐形成网状依赖；新增面板时多个协作对象都可能变化 |
| v2-refactor | 将协作流程移到 `WorkbenchCoordinator`，组件向具体协调者报告事件 | 协调职责集中，组件不再相互调用；组件仍依赖具体协调类，扩展替换边界不明确 |
| v3-pattern | 定义 `WorkbenchMediator` 协议，`CaseSelector`、`DialPanel`、`CallCallback` 只依赖该接口，由 `DefaultWorkbenchMediator` 决定协作流程 | Colleague 与 ConcreteMediator 的角色清楚，组件直接依赖减少；协作复杂度仍集中在中介者，需按工作台或业务边界拆分 |

## 中介者协调关系

```text
CaseSelector ─┐
DialPanel ────┼──> WorkbenchMediator ──> ScriptPanel
CallCallback ┘            │            ├─> ComplianceService
                           │            ├─> DialPanel
                           │            ├─> DialGateway（模拟）
                           └────────────└─> AuditService（模拟）
```

以选择案件为例，中介者按顺序保存当前案件、加载话术、校验外呼权限、更新按钮状态并记录审计。点击外呼时会重新校验合规，不把界面按钮状态当作安全边界。通话回调仅在案件仍是当前选择时刷新该案件的工作台展示。

## 适用边界与模式辨析

- **中介者与外观：** Facade 简化外部调用者访问复杂子系统的入口；Mediator 解开多个内部协作对象之间的网状依赖。
- **中介者与观察者：** Observer 广播事件，由订阅者独立响应；Mediator 负责有顺序和条件关系的对象协作，例如合规校验结果决定按钮状态。
- **不要强行使用：** 如果只是清晰的应用服务按顺序调用几个领域服务，普通流程编排更简单。不要把所有 Service 编排都称为中介者模式。
- **状态生命周期：** 本例具体中介者保存 `selectedCaseId`，用于模拟单个工作台。真实多用户服务应将会话/页面状态与无状态的业务协调服务分开，不能让多个用户共享一个有状态 singleton。
- **复杂度代价：** Mediator 降低同事对象之间的直接依赖，但不会消除协作逻辑；持续扩展后要避免形成 God Object，按 `CallWorkbenchMediator` 等清晰边界拆分。

## 测试与运行

每个版本一个打印式测试，期望结果写在测试方法中文 Javadoc 中。输出覆盖正常外呼、禁止外呼和通话结束回调，JUnit 只负责发现并执行测试，不使用断言。

```bash
mvn -f patterns/behavioral/mediator/pom.xml test
mvn -f patterns/behavioral/mediator/v3-pattern/pom.xml test
```

所有版本相互独立并兼容 JDK 1.8。控制台输出仅供学习观察，生产系统应使用结构化日志，并在服务端执行合规校验。
