/**
 * 中介者模式专题的v3-pattern实践版本。
 *
 * <p>业务场景：本实践对应《01-1｜设计模式专项》第二十二讲“中介者模式”。专题以催收外呼工作台为例：作业员选择案件后加载话术、执行合规检查、更新外呼按钮并记录审计；点击外呼时重新校验；通话回调记录结果，并避免旧案件回调更新当前案件。 本实践使用模拟组件和虚构案件编号：10001 表示允许外呼，10002 表示被教学合规规则限制。没有真实外呼、客户通知或远程服务调用。当前示例没有引入 Spring，因为专题重点是识别组件协作关系；特别要注意最终中介者保留当前案件状态，不能直接作为所有作业员共享的有状态 Spring singleton。</p>
 *
 * <p>本版本实现要点：定义 `WorkbenchMediator` 协议，`CaseSelector`、`DialPanel`、`CallCallback` 只依赖该接口，由 `DefaultWorkbenchMediator` 决定协作流程；Colleague 与 ConcreteMediator 的角色清楚，组件直接依赖减少；协作复杂度仍集中在中介者，需按工作台或业务边界拆分。中介者集中协调多个组件的交互，减少组件间的网状依赖。</p>
 *
 * <p>具体调用路径请参见本版本目录下的流程图与时序图文档。</p>
 *
 * @author advance
 * @since 2026-10-29 15:47:52
 */
package com.advance.patterns.behavioral.mediator.v3;
