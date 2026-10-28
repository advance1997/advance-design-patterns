# 单例：催收规则对象的实例与共享状态

## 来源与约定

依据《01-1｜设计模式专项》阶段 2 大纲，会话
`01a0ba95-6029-74e0-a6e2-c68184b4d5db`，
大纲回合 `01a0ba95-6670-7ed0-8d27-9b2279bc770d`。
按用户的新要求，将课程设计要点改编为催收金融业务；下述类名、数据和细节是
本仓库工程化补充，不宣称是课程原始案例。

所有案件编号、配置和流程均为虚构。只模拟人工复核，不发送短信、不外呼，
不查询真实客户资料、不决定信用待遇或采取法律行动。教学参数不代表真实监管规则。

采用 Java 8、普通类与直接方法调用。每个版本独立持有源码和测试，
不共享业务类，不依赖其他版本。Javadoc 使用作者 advance 和上海时区创建时间。
测试仅由 JUnit 发现和运行，上方标注期望，直接打印实际值，不使用断言或函数式回调。
预期异常打印类型和信息，未抛出则提示人工核查；绿色测试不等于输出已被自动证明正确。

课程对应饿汉式、懒汉式、同步、DCL/volatile、内部类、枚举以及 Spring 单例边界。

## 版本与阅读顺序

| 版本 | 实现 | 观察重点 |
| --- | --- | --- |
| v1-naive | UnsafeRuleBook；另有 EagerRuleBook 对照 | 不安全懒加载；单次案件字段被另一次调用覆盖 |
| v2-refactor | synchronized 懒加载 SynchronizedRuleBook | 锁保护初始化，移除案件共享状态；每次获取都加锁 |
| v3-pattern | Holder、DCL、枚举与 Spring 上下文 | 不同初始化机制、并发引用、枚举反序列化、容器作用域 |

包前缀为 `com.advance.patterns.creational.singleton.v1/v2/v3`。
规则值 100 仅是教学批次上限，不是实际金融规则。

## 必须区分的问题

1. 实例数量：同一个类加载器下重复获取是否同一实例。
2. 安全发布：其他线程能否正确观察构造完成的对象。
3. 对象自身安全：方法和共享字段是否适合并发访问。
4. 生命周期和作用域：JVM 静态对象与容器 Bean 并不是同一个概念。

V1 testSharedStateLeak 使用顺序调用，确定性展示 DEMO-001 被 DEMO-002 覆盖；
不依赖线程竞态偶然发生。未同步懒加载本身仍不安全，
某次运行只看到一个对象不能证明实现正确。

V2 将单次案件数据移出规则对象；同步 getter 保护初始化，但不会自动修复别的共享可变字段。

V3：

- Holder 依赖 JVM 类初始化，首次主动使用 Holder 时创建。
- DCL 的内层检查避免重复构造，volatile 保障可见性和安全发布，不能省略。
- EnumRuleBook 的标准反序列化保留同一枚举常量。
- 并发测试使用普通具名 Callable，8 个任务分别获取三个实现，打印实例集合大小为 1。
  有限次数测试不是 Java 内存模型正确性的证明，且测试顺序不保证每次都覆盖冷启动。
- Spring 默认 singleton 是每个容器、每个 Bean 定义一个实例：
  同一上下文相同 Bean 为 true，不同上下文为 false。
- 普通类的 private 构造器不抵抗所有反射手段；Holder 和 DCL 未实现 Serializable，
  不声称解决任意序列化或反射破坏。枚举也不是跨类加载器、跨 JVM 或分布式单例。
- 不推荐用全局单例隐藏业务依赖；实际 Spring 应用通常优先构造器注入和容器作用域。

## Spring 与环境边界

使用 Spring Framework **5.3.39**，以兼容当前 JDK 1.8；该旧版本线仅用于学习，
不作为生产安全或维护版本推荐。生产选型需重新评估受支持的 JDK / Spring 版本。
未使用 Spring Boot，不启动 Web 服务，也不连接数据库或外部催收机构。
涉及容器的测试使用真实 AnnotationConfigApplicationContext，并在结束时关闭。
Spring 依赖只在需要的叶子模块声明，父 POM 仅管理版本。

## 运行

设置 JAVA_HOME 为 JDK 1.8，在仓库根目录执行：

```bash
./mvnw -f patterns/creational/singleton/pom.xml test
./mvnw -f patterns/creational/singleton/v1-naive/pom.xml test
./mvnw -f patterns/creational/singleton/v2-refactor/pom.xml test
./mvnw -f patterns/creational/singleton/v3-pattern/pom.xml test
```

全项目回归：`./mvnw test`。每个叶子模块可独立运行，无需先安装其他版本。
请先阅读测试上方期望，再对照类名、方法名、结果及异常路径。
