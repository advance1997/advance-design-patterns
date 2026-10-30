# 催收诉讼材料归集系统：最终生产架构

## 专题来源与范围

本案例落地《01-1｜设计模式专项》中“催收诉讼材料归集系统：最终生产架构”。设计推演假设每天 10 万个归集任务（仅为教学规模假设，不是实际生产数据），需要按场景选择材料、通过不同来源取件、校验、异步重试、形成目录并生成可追踪的文件包。

本案例只保留一个 `v1-final` 综合版本：目标是看清已学设计思想如何在一条完整业务流中分工，不重复每个模式的基础推演。该版本源代码和测试独立，不依赖仓库中各模式练习模块。MySQL、Outbox、MQ、对象存储和外部材料系统全部使用内存 mock，不引入实际数据库、消息中间件、网络调用或客户数据。

## 一次执行链路

```text
MaterialFacade.submit(request)
  → MaterialTaskService.createIfAbsent(request)       幂等 + Mock DB
  → OutboxPublisher.publishPending()                   模拟事务发件箱
  → MockMessageBroker.send(taskId)                     模拟 MQ
  → MaterialWorker.handle(taskId)                      认领并执行任务
  → RequirementResolver.resolve(scene)                 Strategy
  → MaterialCollector.collect(requirement)             Adapter
  → MaterialCheckHandler.check(result)                  Chain of Responsibility
  → MaterialTreeAssembler.assemble(items)                Composite
  → MaterialTreeIterator                               Iterator
  → StatisticsVisitor / ManifestVisitor                 Visitor
  → MaterialPackageBuilder.build(tree)                   Builder
  → MockObjectStorage.put(key, package)                  模拟对象存储
  → MaterialTaskRepository.saveResult(...)               Mock DB 状态提交
```

## 模式各自解决的问题

| 设计 | 本例中的职责 | 边界 |
| --- | --- | --- |
| Facade | 对外只暴露提交归集任务的入口，隐藏创建任务与发件箱步骤 | 不承担分布式事务 |
| Strategy | 按诉讼/仲裁场景提供不同材料要求 | 不负责材料来源调用 |
| Adapter | 将合同、账务、录音系统的不同客户端统一成材料来源接口 | 不吞掉超时，也不把未知结果误报为缺失 |
| Chain | 逐项检查来源结果与必需性，可按顺序扩展校验 | 重试决策属于任务执行协调，不伪装成校验规则 |
| Composite | 将材料文件组织为多级目录树 | 不承担打包或统计职责 |
| Iterator | 以统一顺序遍历目录树中的文件 | 不改变目录结构 |
| Visitor | 在稳定节点结构上分离统计、清单输出操作 | 增加节点类型时需要扩展访问者接口 |
| Builder | 将冻结后的材料树构建成包描述对象 | 不把全部模式都塞进 Builder |

任务状态仅用于表达流程进展，不实现状态模式；此例也不为凑模式强加 Command 类。Outbox Event 是可靠发布机制的数据记录，不等同于 GoF Command。

## 可靠性机制与模式的区别

Mock repository 用业务请求键模拟唯一约束和幂等创建；任务明细单独记录每份材料的成功/待重试状态；Outbox Publisher 可重复投递，消费者以任务状态认领；临时异常只重试失败明细，已经成功的材料不会丢失；任务冻结教学规则版本并将其写入对象键，便于关联本次归集口径；达到有限重试次数后转为失败；打包上传后再以状态条件提交最终对象键。

这些机制体现数据库约束、消息至少一次投递、消费幂等、重试上限与对象存储补偿的工程思路，不是设计模式本身提供的保证。示例中的 `MockMessageBroker` 和存储均为单进程内存对象，不能证明真实分布式环境下的原子性、并发安全或可靠性。

## 运行方式

```bash
mvn -f examples/comprehensive/collections-material-architecture/pom.xml test
mvn -f examples/comprehensive/collections-material-architecture/v1-final/pom.xml test
```

测试只打印期望结果与执行路径供人工对照，不使用断言。全部 Java 代码采用 JDK 1.8 写法。可视化：[`v1-final/流程图与时序图.md`](v1-final/流程图与时序图.md)。

真实生产还需根据存储、MQ 和服务选型设计事务边界、唯一索引、任务租约/fencing token、并发控制、死信处理、权限与数据脱敏、文件完整性校验、对象清理、监控告警及容量规划；本教学 mock 不应被视为生产实现。
