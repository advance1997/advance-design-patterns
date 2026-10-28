# Spring 注册式工厂：催收案件人工复核处理器

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

课程对应“注册式工厂、工厂与 IOC 的关系”；业务改编为按阶段安排人工复核。

## 版本与阅读顺序

| 版本 | 阅读入口 | 变化 |
| --- | --- | --- |
| v1-naive | CollectionConfig → CollectionService | Spring 创建处理器，服务仍依赖具体类型和条件分支 |
| v2-refactor | CollectionHandlerFactory | 显式注册两个实例，业务服务不再判断阶段，但工厂新增类型仍要修改 |
| v3-pattern | CollectionConfig → CollectionHandlerFactory | 扫描 handler 包，集合注入全部 CollectionHandler，按阶段自动注册 |

包前缀为 `com.advance.patterns.creational.registeredfactory.v1/v2/v3`。
handler 包只实现案件处理；config 负责 Spring 装配；factory 负责注册与查找；
service 负责调用，不能以 ApplicationContext 作为隐藏的服务定位器。

此处“注册式工厂”是提供实例的业务入口，不是 GoF 独立模式，也不是工厂方法。
**实例创建、依赖注入和销毁归 Spring，注册表提供的是已有实例。**
它与此前策略注册表结构相似，但本节重点是容器创建与注册职责边界，
不把 Map 查询包装成新的对象构造算法。

## 关键观察

- 普通路径：Spring 创建处理器 → 工厂注册 → 服务查找 → 处理器返回人工待复核结果。
- EARLY 返回 EARLY_REVIEW，LATE 返回 LATE_REVIEW。
- LEGAL_REVIEW 默认未注册，不自动改走其他阶段。
- RegistryTest.testAutoDiscovery 向容器添加具名 ExtraHandler Bean；
  不修改工厂或业务服务，就可获得 LEGAL_PENDING。
- testDuplicateFailsRefresh 实际刷新容器，重复 EARLY 使启动失败；
  外层通常是 BeanCreationException，原因链包含“阶段重复注册”。
- null / 空列表、空元素、空阶段均被拒绝；注册时复制映射，
  清空原输入列表不影响工厂，不提供运行期变更入口。
- 每个处理器无单次案件共享字段；Spring singleton 本身不保证其未来实现线程安全。
- 未使用 Bean 名称作为业务路由，避免重命名 Bean 改变业务阶段含义。

## Spring 与环境边界

使用 Spring Framework **5.3.39**，以兼容当前 JDK 1.8；该旧版本线仅用于学习，
不作为生产安全或维护版本推荐。生产选型需重新评估受支持的 JDK / Spring 版本。
未使用 Spring Boot，不启动 Web 服务，也不连接数据库或外部催收机构。
涉及容器的测试使用真实 AnnotationConfigApplicationContext，并在结束时关闭。
Spring 依赖只在需要的叶子模块声明，父 POM 仅管理版本。

## 运行

设置 JAVA_HOME 为 JDK 1.8，在仓库根目录执行：

```bash
./mvnw -f patterns/creational/registered-factory/pom.xml test
./mvnw -f patterns/creational/registered-factory/v1-naive/pom.xml test
./mvnw -f patterns/creational/registered-factory/v2-refactor/pom.xml test
./mvnw -f patterns/creational/registered-factory/v3-pattern/pom.xml test
```

全项目回归：`./mvnw test`。每个叶子模块可独立运行，无需先安装其他版本。
请先阅读测试上方期望，再对照类名、方法名、结果及异常路径。
