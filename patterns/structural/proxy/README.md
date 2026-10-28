# 代理模式：催收案件分派与本地事务

## 来源、顺序与本次范围

已读取《01-1｜设计模式专项》阶段 3「代理模式」原文：
会话 `01a0ba95-6029-74e0-a6e2-c68184b4d5db`，
大纲回合 `01a0ba95-6670-7ed0-8d27-9b2279bc770d`。

课程链路为 Class → 反射调用 → 静态代理 → JDK 动态代理 → CGLIB →
Byte Buddy 基本思想 → Spring AOP → RPC Stub。
本次按用户指定聚焦事务场景、Spring 事务代理和自制简化事务框架：
V1 观察反射，V2 静态代理，V3 JDK 动态代理，V4 对照 Spring 的 JDK/CGLIB 事务，
V5 实现教学框架。Byte Buddy 与 RPC Stub 在本文说明边界，未另行扩展实现。
不把本次基础实现标记为已完成全部框架源码和 RPC 专题。

按照用户要求将课程的横切事务场景改编为催收金融业务；业务类、SQL、H2 数据库
和迷你框架均为本仓库工程化补充，不是课程原始代码。
只操作 DEMO-001、DEMO-002 等虚构案件与 REVIEW-A 等内部复核组。
不外呼、不发送短信、不查询真实客户资料，不操作资金或采取法律行动。

## 为什么选案件分派

一次内部人工复核分派需要两次写入：

1. 更新 collection_case.assignee。
2. 插入 assignment_record，记录本次分派。

两次写入必须一起成功或一起撤销。否则可能出现“案件已经分派，但没有分派记录”。
稳定业务是更新与记录；事务开启、提交、回滚和连接清理是横切能力，
适合用代理放在目标方法前后，而不是把 JDBC 事务代码塞进每个业务方法。

本例是真实 JDBC 事务：使用 H2 内存数据库，不是只打印 commit/rollback。
测试通过独立连接查询已提交状态，避免把同一事务里的未提交数据当作最终结果。
每个用例拥有随机命名的独立内存库，最后关闭保持连接，不使用永久存活内存库选项。

## 独立版本与阅读入口

| 版本 | 入口 | 学习重点 |
| --- | --- | --- |
| v1-naive | CaseAssignmentService、ProxyTest | 自动提交造成部分更新；Class/Method 反射不会自动带来事务 |
| v2-refactor | StaticTransactionProxy | 手工实现同一接口，在目标调用前后包事务 |
| v3-pattern | JdkTransactionProxy | Proxy.newProxyInstance + 具名 InvocationHandler，统一拦截接口方法 |
| v4-spring | JdkTransactionConfig、CglibTransactionConfig | 真实 @Transactional，分别强制接口代理与类代理 |
| v5-mini-framework | MiniTransactionAutoProxyCreator | 自定义注解 + Spring 后处理器 + JDK 代理 + 自制事务管理器 |

V4、V5 是按仓库约定增加的独立扩展版本，分别解决“观察框架行为”和“理解最小实现”。
包前缀为 com.advance.patterns.structural.proxy.v1 至 v5。
各自持有数据库、仓储、服务、事务实现和测试，不跨版本共享业务代码或相互依赖。

## 动态代理到底动态在哪里

静态代理需要开发者编写 StaticTransactionProxy 类并实现接口；
新增接口方法通常需要同步维护代理。

JDK 动态代理由 Proxy.newProxyInstance 在运行时生成实现接口的代理类。
调用方拿到的是接口代理，不是 CaseAssignmentService 目标对象，也不是它的子类：

```text
调用方 → AssignmentService 代理
       → InvocationHandler.invoke(proxy, method, args)
       → 开启或加入事务
       → Method.invoke(真实目标, args)
       → 按结果提交或回滚
       → 清理连接 → 返回或抛出原始异常
```

关键区别：

- Class 提供类型信息，Method 表示方法，反射负责调用；反射本身不提供事务。
- InvocationHandler 必须调用真实目标，不能再调用代理自身，否则可能无限递归。
- Method.invoke 会用 InvocationTargetException 包装目标异常；
  V3/V5 解包并抛回原始异常，让回滚判断和调用方看到真正的业务失败。
- JDK 代理可被当作 AssignmentService 使用，不能强转成 CaseAssignmentService。
- V3/V5 的 equals/hashCode 使用代理身份，toString 输出代理摘要；
  这些 Object 方法不创建业务事务。
- Spring CGLIB 通过目标类的子类进行增强，可以代理没有接口的普通类，
  但不能覆盖 final/private 方法，也不能继承 final 类。
- final 目标方法仍可通过 JDK 接口代理被增强，因为 JDK 代理不依赖覆盖该方法。
- Byte Buddy 是另一种运行时生成或增强字节码的工具，不等于 JDK Proxy，
  也不能据此声称本例 Spring 事务使用 Byte Buddy。
- RPC Stub 可把本地接口调用转为远端请求；代理并不让网络调用获得本地事务原子性。
- 不把“反射一定慢”当作设计结论；本例不做性能基准，数据库访问、锁竞争和业务工作量
  都可能比代理调用开销更重要。

## V4：Spring 事务是怎么生效的

Spring Framework 5.3.39、H2 2.2.224 均用于 Java 8 教学环境，
是固定的旧版本，不作为生产安全选型推荐。不使用 Spring Boot 或外部数据库服务器。

```text
@EnableTransactionManagement
→ 注册事务属性解析器、Advisor、TransactionInterceptor 与自动代理基础设施
→ 调用进入 Spring JDK/CGLIB 代理
→ 解析目标方法上的 @Transactional
→ DataSourceTransactionManager 开启/参与本地 JDBC 事务
→ JdbcTemplate 经 DataSourceUtils 使用同线程绑定连接
→ 方法返回或抛异常
→ 提交/回滚 → 清理 TransactionSynchronizationManager 资源
```

TracingTransactionManager 继承真实的 DataSourceTransactionManager，
只增加中文路径输出，各生命周期方法都委托 super，不重写事务规则。
注解解析与拦截仍是 Spring 本身实现，不是我们打印出来模拟的框架行为。

JdkTransactionConfig 显式设置 proxyTargetClass=false；
CglibTransactionConfig 显式设置 true。两个配置在不同测试上下文运行，不能混在一个上下文
再根据 Bean 声明顺序推测代理类型。

### 需要亲自观察的语义

| 场景 | 最终案件状态 | 记录数 | 原因 |
| --- | --- | ---: | --- |
| 正常分派 | REVIEW-A | 1 | 两次写入一起提交 |
| 更新后运行时异常 | UNASSIGNED | 0 | 默认回滚 |
| 默认受检异常 | REVIEW-A | 0 | 默认不因受检异常回滚，异常仍向上抛 |
| rollbackFor=Exception.class | UNASSIGNED | 0 | 显式把受检异常纳入回滚 |
| 未注解入口 this.assign | REVIEW-A | 0 | 自调用没有再次经过代理，外层也没有事务 |
| 直接调用原始目标 | REVIEW-A | 0 | 绕过代理 |
| 内层 REQUIRED 失败被外层捕获 | UNASSIGNED | 0 | 已标记 rollback-only，外层提交抛 UnexpectedRollbackException |
| 第二次插入同案件记录触发主键冲突 | 保留第一次 REVIEW-A | 1 | 第二次更新与插入整体回滚 |

testFinalMethod 特意使用不访问实例字段的 final 方法：
JDK 代理内部事务活动为 true，CGLIB 为 false。
不要把 CGLIB final 方法中的字段访问异常误当作事务规则；
真实服务应避免把需要增强的方法声明为 final。

“自调用事务失效”不等于任何 this 调用都没有事务：
如果外层已经开启事务，内部方法的 JDBC 操作仍可参与外层连接，
但内部方法的独立事务注解不会通过这次自调用重新解析。
本例故意从没有事务的外层入口调用，便于明确展示边界。

不要让仓储直接绕过 Spring 随意调用 DataSource.getConnection，
否则可能拿到不参与当前事务的新连接。本例 V4 使用 JdbcTemplate 正确参与事务。

## V5：自己实现的简化事务框架

本版使用 Spring 的 IOC 和 BeanPostProcessor，但**不依赖 Spring JDBC/Tx 的
事务管理器、TransactionInterceptor 或 @EnableTransactionManagement**。
事务注解、代理拦截、回滚判断、线程连接绑定、commit/rollback 都由本版代码实现。

| 教学组件 | 作用 | 对照 Spring 概念 |
| --- | --- | --- |
| MiniTransactional | 实现方法上的运行时事务注解 | Transactional |
| MiniTransactionAutoProxyCreator | Bean 初始化后按注解创建 JDK 代理 | 自动代理创建器的一小部分 |
| MiniTransactionInterceptor | 解析注解、反射调用、异常回滚规则 | TransactionInterceptor 的最小主线 |
| LocalTransactionManager | 单数据源 REQUIRED、本地提交回滚 | DataSourceTransactionManager 的最小主线 |
| ConnectionContext | 同线程共享连接 | TransactionSynchronizationManager 的资源绑定概念 |
| ConnectionLease | 借用事务连接，事务外临时连接自行关闭 | DataSourceUtils 的部分连接职责 |

核心顺序：

1. 后处理器扫描目标实现类的 public 方法。
2. 含 MiniTransactional 的方法必须能通过目标直接实现的接口暴露。
3. 生成 JDK 代理，并保留真实目标引用。
4. 调用进入代理后解析实现方法注解；未注解方法直接委托。
5. 无事务则获取连接并关闭 autoCommit；有事务则 REQUIRED 加入。
6. 仓储通过 ConnectionContext 使用同一连接，不提前关闭借用连接。
7. RuntimeException/Error 或 rollbackFor 匹配时回滚；默认受检异常提交。
8. 内层参与者只能标记 rollback-only，外层拥有者才完成物理事务。
9. 解除 ThreadLocal、恢复已完成事务连接的 autoCommit、关闭连接。

原始业务异常优先保留，完成事务或清理异常作为 suppressed 附加。
完成失败后会尝试回滚，但无法因此保证“提交一定没有发生”；
数据库结果未知时不自动重试业务。
若回滚仍失败，不贸然把 autoCommit 切回 true，以免额外触发提交。

TransactionFailureTest 用具名 JDBC InvocationHandler 注入可控故障：
commit 前失败、rollback 前失败，以及物理 close 后报告失败。
其中“已提交但关闭报错”的用例能读到 REVIEW-A / 1，
说明出现异常不等于业务一定回滚。
H2 关闭未提交连接后的行为不应被推广为所有驱动的故障恢复保证。

### 明确不支持的能力

- 只支持同步、单线程调用栈中的一个本地数据源；不支持跨线程、异步或分布式事务。
- 只支持 REQUIRED，不支持 REQUIRES_NEW、NESTED、保存点、事务挂起或 JTA。
- 不支持隔离级别配置、超时、readOnly、noRollbackFor、事务监听与提交后事件。
- 注解只支持目标实现类的 public 接口方法，不支持类级、接口级或非 public 注解语义。
- 只生成 JDK 接口代理，不实现 CGLIB、完整 Advisor 链、循环依赖早期代理等 Spring 能力。
- 不实现连接池、不承诺任意驱动错误都可恢复；清理失败只报告诊断，不装作已恢复。
- public 注解方法没有接口入口时启动失败，不静默运行；私有方法不在支持范围内。
- 自调用仍然绕过代理，不通过“把自己从容器取出来”掩盖这个边界。
- 本框架是学习实现，不能替换生产 Spring 事务管理。

## 运行与阅读建议

设置 JAVA_HOME 为 JDK 1.8，在仓库根目录运行：

```bash
./mvnw -f patterns/structural/proxy/pom.xml test
./mvnw -f patterns/structural/proxy/v1-naive/pom.xml test
./mvnw -f patterns/structural/proxy/v2-refactor/pom.xml test
./mvnw -f patterns/structural/proxy/v3-pattern/pom.xml test
./mvnw -f patterns/structural/proxy/v4-spring/pom.xml test
./mvnw -f patterns/structural/proxy/v5-mini-framework/pom.xml test
./mvnw test
```

建议先运行 V1 的 testFailure 与 testReflectionDoesNotAddTransaction，再对比 V2/V3 的
testFailure。接着阅读 V4 的 SpringTransactionTest 与 CglibTransactionTest，
最后用 V5 的 MiniTransactionTest 对照相同业务结果，阅读 TransactionFailureTest 的异常清理路径。

源码与测试保留详细中文 Javadoc，作者 advance，时间为上海时区 yyyy-MM-dd HH:mm:ss。
测试使用直接调用、具名普通类和打印，没有断言、参数化测试、Lambda、Consumer 或 Function。
每个测试上方注明期望，数据库状态、实际异常类型和连接清理状态都直接打印。
框架原生警告与异常消息保留原文，便于识别 Spring 包装异常及原因链。

**JUnit 绿色只表示执行完成，不代表实际打印结果已自动判定正确。**
还必须检查实际 assignee/records、异常类型、事务活动标记及调用顺序。
