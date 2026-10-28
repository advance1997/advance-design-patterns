# Spring 工厂与 IOC：模拟催收协作客户端

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

课程对应 BeanFactory、FactoryBean、ObjectFactory 的职责区分及对象生命周期。

## 版本与阅读顺序

| 版本 | 实现 | 边界 |
| --- | --- | --- |
| v1-naive | ReviewService 注入 ApplicationContext 后主动 getBean | 可运行的服务定位器反例：隐藏业务依赖 |
| v2-refactor | 构造器注入 MockAgencyClient，@Bean 配置 open/close | 容器负责装配生命周期，服务只认识客户端 |
| v3-pattern | AgencyClientFactoryBean + ObjectFactory&lt;ReviewSession&gt; | 区分产品工厂、延迟获取及 prototype 资源责任 |

包前缀为 `com.advance.patterns.creational.factoryioc.v1/v2/v3`。
这是工厂与框架的关系实践，不声称为一个额外的 GoF 模式。

## 三个接口不能混淆

| 名称 | 本例角色 | 不负责什么 |
| --- | --- | --- |
| BeanFactory | Spring 容器的基础访问接口；测试从中取 Bean | 不等同于创建客户端的业务类 |
| FactoryBean&lt;MockAgencyClient&gt; | AgencyClientFactoryBean 为容器提供产品 | isSingleton 不决定工厂 Bean 自身作用域 |
| ObjectFactory&lt;ReviewSession&gt; | 服务每次需要会话时调用 getObject | 本身不保证新对象；是否新建由目标作用域决定 |

`getBean("agencyClient")` 返回产品；
`getBean("&agencyClient")` 返回工厂本身。
工厂的 getObjectType 可在不创建产品的情况下说明类型。
本例 getObject 缓存产品，isSingleton 返回 true。

## 生命周期顺序

V2：容器构造客户端 → open → 服务使用 → context.close → close。

V3：首次获取延迟服务或产品 → 工厂初始化 → getObject 首次构造并 open →
每次 review 从 ObjectFactory 获取 prototype 会话 → bind → 模拟查询 →
try-with-resources 关闭会话 → 上下文关闭时工厂 destroy 关闭产品。

特别注意：

- Spring 不替 FactoryBean 产品自动调用销毁回调；本例由工厂显式 close 产品。
- prototype Bean 的销毁不由容器自动负责，本例由服务或测试显式关闭。
- @Lazy 不代表依赖被请求时还不初始化；获取服务会触发其所需产品初始化。
- MockAgencyClient 和 ReviewSession 只演示单线程生命周期，不能据此推断真实 SDK 线程安全。
- testFailureClosesSession 故意关闭客户端；查询失败也必须看到会话 close。
- 上下文关闭后，已取得的客户端和工厂均拒绝重新使用。
- 案件编号为空时在创建会话前拒绝，不发起模拟查询。

## Spring 与环境边界

使用 Spring Framework **5.3.39**，以兼容当前 JDK 1.8；该旧版本线仅用于学习，
不作为生产安全或维护版本推荐。生产选型需重新评估受支持的 JDK / Spring 版本。
未使用 Spring Boot，不启动 Web 服务，也不连接数据库或外部催收机构。
涉及容器的测试使用真实 AnnotationConfigApplicationContext，并在结束时关闭。
Spring 依赖只在需要的叶子模块声明，父 POM 仅管理版本。

## 运行

设置 JAVA_HOME 为 JDK 1.8，在仓库根目录执行：

```bash
./mvnw -f patterns/creational/factory-ioc/pom.xml test
./mvnw -f patterns/creational/factory-ioc/v1-naive/pom.xml test
./mvnw -f patterns/creational/factory-ioc/v2-refactor/pom.xml test
./mvnw -f patterns/creational/factory-ioc/v3-pattern/pom.xml test
```

全项目回归：`./mvnw test`。每个叶子模块可独立运行，无需先安装其他版本。
请先阅读测试上方期望，再对照类名、方法名、结果及异常路径。
