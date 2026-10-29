# 备忘录模式：和解方案草稿撤销

## 专题来源与主案例

本实践对应《01-1｜设计模式专项》第二十三讲“备忘录模式”。主案例是催收和解方案编辑：案件 `10001` 初始首付 1000 元、两期各 2000 元；第一次修改首付为 500 元，第二次把分期改为 2500 元和 1500 元；连续撤销应先恢复到首付 500 元的 V2，再恢复到初始 V1。

撤销范围仅限**尚未提交审批的编辑草稿**。金额、案件与客户端均为虚构或内存模拟；不接数据库、MySQL、外呼、客户通知或真实资金系统。

## 版本演进

| 版本 | 做法 | 体现的问题或收获 |
| --- | --- | --- |
| v1-naive | Controller 逐项读取首付、分期、备注，建立字段备份并逐项恢复 | 草稿字段增加时，外部 Controller 需要同步维护历史字段，容易漏保存或漏恢复 |
| v2-refactor | `SettlementDraft` 创建 `Snapshot` 并恢复自身状态，`DraftHistory` 用 `Deque` 管理历史 | Originator 负责自身状态；快照仍有公开 getter，尚未完全隐藏内部结构 |
| v3-pattern | `SettlementDraft` 创建不可变且不暴露字段的 `Memento`；`DraftHistory` 只保存/弹出 | 完整展示 Originator、Memento、Caretaker 三个角色，以及两次撤销、空历史异常和集合防御性复制 |
| v4-pattern-combination | 编辑工作台由 Mediator 协调 UI；Command 封装首付/分期/备注编辑；每条 Command 使用 Memento 撤销；业务状态限制审批后的编辑 | 展示 Command 负责操作、Memento 负责历史状态、Mediator 负责组件协作、状态规则负责可编辑边界；另以内存存储模拟版本历史取舍 |

## GoF 备忘录关系

```text
Originator: SettlementDraft
    ├── createMemento()：创建内部状态快照
    └── restore(memento)：恢复自身状态
                  │
                  ▼
Memento: 保存首付、分期和备注的不可变状态
                  │
                  ▼
Caretaker: DraftHistory ── Deque（后进先出）
           只保存和弹出，不读取草稿字段
```

快照中的分期列表使用防御性复制，避免历史与当前对象共享可变集合。`BigDecimal` 不可变，不需要逐个复制。完整快照简单直观，但快照数量和状态体积会带来内存/存储成本；生产中可限制历史数量，并按需要选择完整快照或更复杂的增量记录。

## 专题里的模式连接与生产边界

### 原型与备忘录

原型模式的目的在于复制对象并创建新的独立对象；备忘录模式的目的在于保存旧状态并恢复原对象。它们都可能执行复制，但后续用途不同。

### 命令与备忘录

Command 封装一次编辑操作；Memento 保存该操作之前的草稿状态。v4 中每个修改命令在执行前创建快照，`undo()` 再让 Originator 恢复该快照。二者可以组合实现撤销，但各自职责不相同。

### 中介者、命令、备忘录与状态

v4 以和解编辑工作台模拟专题中的组合关系：

```text
DownPaymentEditor / InstallmentEditor / RemarkEditor / UndoButton
                                │
                                ▼
                   SettlementEditorMediator
                       │             │
                       ▼             ▼
                   EditCommand   PlanStatus
                       │             │
                       ▼             └── 仅 DRAFT 可编辑/撤销
                Memento + DraftHistory
                       │
                       ▼
                SettlementDraft
```

这只是教学组合，并不表示每个编辑页面都必须增加四种模式。`PlanStatus` 体现草稿与审批后的生命周期边界：已审批方案不能通过恢复 JVM 对象快照撤销已经产生的审批、通知或履约；这类变化需要业务变更/补偿流程。

### 乐观锁版本号与历史快照

v4 使用 `InMemoryPlanStore` 模拟“当前记录及 version”，并用独立 `PlanHistoryArchive` 保存历史值、快照 JSON 内容、操作员和创建时间。专题提到的历史表可抽象为 `plan_id / version / snapshot_json / created_at / operator_id`。乐观锁 `version` 只用于识别并发更新冲突，递增它并不会自动保留旧状态；历史版本需另行保存快照。真实数据库历史表还可能负责审计、版本对比和恢复授权，本练习不接 MySQL。

## 测试与运行

所有测试打印预期路径和实际状态，不使用断言。v4 三个测试分别演示模式组合撤销、已审批状态边界和乐观锁版本与持久化历史的区别。

```bash
mvn -f patterns/behavioral/memento/pom.xml test
mvn -f patterns/behavioral/memento/v3-pattern/pom.xml test
mvn -f patterns/behavioral/memento/v4-pattern-combination/pom.xml test
```

全部版本相互独立并兼容 JDK 1.8。普通草稿适合有界的内存快照；生产实现还应评估快照数量、数据大小、权限、审计和业务状态可逆性。
