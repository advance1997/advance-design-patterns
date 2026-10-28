# 命令模式：催收运营异步操作

## 专题案例

专题生产案例是运营经理针对投诉案件批量暂停外呼，并支持重新分案。操作请求需要尽快返回操作编号，实际业务动作由后台统一执行；每项操作要有独立状态和失败记录。

本实践保留这个业务流程，但按要求不实现 Spring Boot、MyBatis 或 MySQL。生产版用内存仓库模拟命令记录，用手动触发的 Worker 模拟后台调度；因此它只能演示设计与状态流转，不具备数据库持久化、跨进程投递、并发抢占和故障恢复能力。

## 命令模式结构

### 经典 GoF 实现

```text
调用者 CommandInvoker
     │ 只依赖 CaseCommand
     ▼
命令队列 ──> PauseCallCommand / ReassignCommand
                       │ execute()
                       ▼
             接收者 CaseActionService
```

命令对象封装操作参数和接收者；调用者只负责提交、排队和触发 `execute()`，不判断具体操作类型。示例依次暂停外呼、重新分案、恢复外呼，体现命令按队列顺序延迟执行。提交命令与执行命令分成两个时点。

### 专题生产案例的工程化形态

```text
命令提交服务
    │ 创建只含编号、类型、业务参数的 OperationCommand
    ▼
内存仓库（模拟命令表）── PENDING
    │
后台 CommandWorker ──> RUNNING
    │
CommandDispatcher
    ├── PAUSE_CALL ──> PauseCallHandler ──> CaseActionService
    └── REASSIGN   ──> ReassignHandler   ──> CaseActionService
    │
    └── SUCCESS / FAILED（失败原因保存在记录中）
```

这里的生产命令数据不直接实现 `execute()`，也不持有业务 Service；可保存数据与执行业务的 Handler 分离。这样避免把 Spring Bean 引用当成命令数据序列化。经典 GoF 版本和生产版都表达“操作请求对象化并与调用方解耦”，但负责执行的对象结构不同。

## 版本演进

| 版本 | 做法 | 问题或收获 |
| --- | --- | --- |
| v1-naive | 通过字符串类型分支，同步调用暂停外呼或重新分案 | 操作分支、参数解释与业务调用集中，不能自然排队或延迟执行 |
| v2-pattern | `PauseCallCommand`、`ReassignCommand` 实现统一接口，由 `CommandInvoker` 排队后触发 | 调用者与操作实现解耦；命令对象持有 Receiver，适合同进程经典模式 |
| v3-production | 保存数据型 `OperationCommand`，Worker 认领后按类型派发给 Handler | 模拟 HTTP 提交与后台执行分离，并记录 PENDING/RUNNING/SUCCESS/FAILED 状态 |

生产版模拟三条操作：暂停外呼成功、重新分案成功、无效作业员导致失败。提交后先打印 PENDING 和未变化的案件状态，再运行 Worker 观察最终状态与失败原因。这里用小样本展示同一流程；专题里的批量 5000 个案件是业务背景，不在测试中制造大量无关数据。

## 测试与运行

每个版本一个打印式测试，期望路径写在测试方法 Javadoc 中。测试使用内存案件和命令记录，不用断言，不连接数据库、MQ 或 HTTP。

```bash
mvn -f patterns/behavioral/command/pom.xml test
mvn -f patterns/behavioral/command/v3-production/pom.xml test
```

所有模块兼容 JDK 1.8。生产系统还需单独解决事务一致性、幂等、并发认领、重试、监控和持久化，这些不属于本次设计模式模拟。
