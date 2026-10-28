# 观察者模式：案件分派后的通知解耦

## 来源与顺序

按《01-1｜设计模式专项》已核对的实际学习顺序，本次在补齐责任链之后补观察者，
下一缺项是状态模式，不跳到外观模式，也不在本次同时实现状态模式。
此前已读取的专题进度总结将观察者关联到 Spring 事件、Outbox 和 MQ。
本次重新读取专题时，接口只返回近期桥接、组合、外观记录，未提供早期观察者正文。
因此以已确认的 Spring 事件主题设计下面的催收教学案例，不宣称复刻原文示例。

## 场景与范围

虚构案件 DEMO-001 分派给模拟经办人 COLLECTOR-A 后，有两个独立的后续动作：

- 工作台接收事件，模拟更新待办。
- 业务日志接收同一事件，模拟记录分派信息。

分派本身不应了解所有后续动作。后续新增接收者时，希望不修改核心分派逻辑。
所有操作仅在内存中打印并计数，不连接数据库、消息队列、真实客户或资金系统。
“日志”仅为普通教学业务日志，不代表具备审计持久化或合规保证。

本模块聚焦观察者与 Spring 同步事件，不实现 MQ、Outbox、异步线程池或分布式可靠投递。
这些工程能力不能由观察者模式本身推导出来，不将本次完成标记为专题可靠性内容全部完成。

## 四个独立版本

| 版本 | 做法 | 变化点 |
| --- | --- | --- |
| v1-naive | 分派服务直接调用两个具体接收者 | 新增后续动作需修改分派服务 |
| v2-refactor | 提取 AssignmentNotifier | 通知职责移出，但通知组件仍显式依赖每个具体对象 |
| v3-pattern | 统一观察者接口与可注册的发布者 | 服务只发布事件，订阅关系决定接收者 |
| v4-spring-event | ApplicationEventPublisher 与 @EventListener | 容器管理事件分发和监听方法注册 |

每个版本包含独立的 event、repository、observer、service；v2 增加 notification，
v4 增加 config。测试故障观察者只放在各自 test/support 中，不进入正常装配。
没有跨版本业务依赖；v1/v2 虽沿用 Observer 命名方便对比，但尚未建立统一观察者订阅契约。

## v3：找到模式的核心

- 事件快照：CaseAssignedEvent，表示已发生的分派事实，字段不可变。
- Subject：AssignmentEventPublisher，管理订阅关系并广播同一事件。
- Observer：AssignmentObserver，统一的 onAssigned 回调接口。
- ConcreteObserver：WorkbenchObserver、AuditObserver，各自处理自己的职责。
- 事件发起者：AssignmentService，保存分派后调用发布者，不持有具体观察者。

```java
publisher.register(workbench);
publisher.register(audit);
AssignmentService service = new AssignmentService(store, publisher);
service.assign("DEMO-001", "COLLECTOR-A");
```

```text
AssignmentService.assign
  → AssignmentStore.save
  → AssignmentEventPublisher.publish
      ├─ WorkbenchObserver.onAssigned
      └─ AuditObserver.onAssigned
  ← 同步处理完成后 assign 返回
```

发布者只依赖观察者接口，不知道待办或日志的实现细节；观察者之间不相互调用。
所有接收者得到相同的事件事实，不需要在通知时重新读取可能变化的案件状态。
调用 unregister(audit) 后，下一次发布不再通知它，但不会撤销以前的通知。
注册同一个对象不重复订阅；一次发布使用订阅列表快照，本轮订阅变更从下一轮生效。
这里的顺序是注册顺序，使用普通集合，适用于单线程学习，不是线程安全事件总线。

## v4：Spring 事件对应什么

| 手写结构 | Spring 版本 |
| --- | --- |
| AssignmentEventPublisher.publish | ApplicationEventPublisher.publishEvent |
| AssignmentObserver.onAssigned | @EventListener 标记的方法 |
| 手动 register | 容器发现监听方法并注册监听适配器 |
| 遍历订阅列表调用 | 默认 SimpleApplicationEventMulticaster 分发 |

CaseAssignedEvent 是普通 Java 8 对象，不需要继承 ApplicationEvent；
Spring 发布普通对象时会包装为 PayloadApplicationEvent，并根据监听方法的事件参数类型匹配。
ObserverConfig 只负责装配存储、服务和两个监听器，不把具体监听器注入服务。

本例没有配置异步执行器，也没有 @Async，因此监听器在发布线程内执行。
方法日志打印线程名，测试在 assign 返回后读取通知次数，观察同步语义。
@Order(10)、@Order(20) 只为稳定展示先后顺序；故障监听器使用 @Order(15)。
不要把必须严格依赖前一步结果的核心业务流程隐藏在多个监听器里。

建议断点：

1. AssignmentService.assign：观察状态保存与事件发布的先后。
2. 手写 AssignmentEventPublisher.publish：观察订阅列表与同一事件对象。
3. Spring AbstractApplicationContext.publishEvent、SimpleApplicationEventMulticaster.multicastEvent。
4. ApplicationListenerMethodAdapter.onApplicationEvent 与两个 @EventListener 方法。

选择 Spring Context 是因为它已直接提供本例需要的事件机制。
Spring Boot 可以承载相同的监听器，但不改变默认事件语义；本例没有 Web 或数据库需求，
因此不额外引入 Boot、MVC 或 MyBatis。沿用兼容 Java 8 的 Spring 5.3.39，仅作教学，不作生产推荐。

## 同步异常不是自动回滚

在工作台与业务日志之间放入测试用 FailingObserver：

```text
保存分派成功 → 工作台已处理 → 故障观察者抛异常
                                  × 日志观察者不再执行
                                  × 服务“通知完成”日志不出现
```

手写发布者没有捕获异常继续广播；Spring 默认同步分发且没有自定义 ErrorHandler 时，
本例的 RuntimeException 同样向发布调用方传播，后续监听器不再执行。
已经写入的内存分派仍在、已经完成的工作台通知也不会撤销。
这不是事务回滚实验：AssignmentStore 没有事务，且本例没有 @Transactional。

若换成事务监听、异步执行或自定义错误处理器，时机与异常传播规则可能不同。
@TransactionalEventListener 也不等于可靠 MQ；提交后进程崩溃等问题仍需另外设计。
需要跨进程投递和重试时，应另行学习事务边界、Outbox、幂等消费等内容，不能以本例替代。

## 与已学模式的区别

- 责任链：节点决定是否继续传递，常用于规则检查与短路。
- 观察者：发布一个事实，让多个独立订阅者作出反应；不是某个订阅者调用下一订阅者。
- 本例监听失败会中止后续通知，是同步分发的异常策略，不是把广播设计成责任链。
- 策略通常选择一种算法；观察者通常让多个接收者都参与。

收益是发布者与后续动作解耦；代价是调用关系更隐蔽，订阅生命周期、顺序和异常策略需要明确。
只有一个稳定后续调用时，直接方法调用可能更清楚，不必为使用模式强行发布事件。

## 测试与运行

仅 7 个打印式测试：

| 版本 | 数量 | 观察点 |
| --- | --- | --- |
| v1 | 1 | 主业务直接通知两个具体接收者 |
| v2 | 1 | 提取通知职责后的相同行为 |
| v3 | 3 | 一对多通知、取消订阅、同步监听异常 |
| v4 | 2 | Spring 同线程分发、监听异常与部分已完成操作 |

每个方法的中文 Javadoc 标注输入、期望路径和次数。
正常时存储经办人为 COLLECTOR-A，两个观察者各收到 1 次。
取消日志订阅后再次分派，累计工作台 2 次、日志 1 次。
异常时工作台为 1 次、日志为 0 次，内存分派仍为 COLLECTOR-A。
预期异常用 try/catch 打印，未抛出时明确提示人工核对。
JUnit 仅运行测试，不使用断言、参数化测试或函数式回调；绿色结果不是自动语义判定。

设置 JAVA_HOME 为 JDK 1.8，在仓库根目录使用 Maven 3.9.x：

```bash
mvn test
mvn -f patterns/behavioral/observer/pom.xml test
mvn -f patterns/behavioral/observer/v3-pattern/pom.xml test
mvn -f patterns/behavioral/observer/v4-spring-event/pom.xml test
```

其他版本同样可通过各自 pom.xml 独立运行，不恢复已清理的 Maven Wrapper 配置。
