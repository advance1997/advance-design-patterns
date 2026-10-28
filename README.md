# advance-design-patterns

使用 Java 8 通过可运行的独立版本，学习并实践经典设计模式。

所有示例使用普通类、明确类型和常规方法调用；测试仅用 JUnit 发现和运行，
不使用断言、Lambda 或函数式事件记录器。每个测试方法的中文 Javadoc 标注期望结果，
运行后请对照控制台实际输出。测试绿色只代表执行完成，不代表自动判定结果正确。

运行前将 JAVA_HOME 指向 JDK 1.8。云环境本次验证使用
`/workspace/.local/jdk8u504-b01`；切换环境后请确认实际安装位置。

## 项目结构

项目采用分层 Maven 多模块结构：

```text
patterns/
├── creational/    # 创建型模式
├── structural/    # 结构型模式
└── behavioral/    # 行为型模式
```

每个设计模式目录是一个聚合模块，每个推演版本则是独立的叶子模块。版本之间不共享业务代码，也不互相声明依赖，以便直接比较每次演进带来的结构变化。

详细约定见 [docs/conventions.md](docs/conventions.md)，学习进度见 [docs/roadmap.md](docs/roadmap.md)。

## 当前实践

后续新增实践统一采用虚构催收金融场景；旧支付、物流示例保留。
Spring 示例采用兼容 Java 8 的 Framework 5.3.39，仅用于教学，不作为生产版本推荐。

- 工厂方法模式：直接创建 → 简单工厂 → 工厂方法
- [模板方法模式](patterns/behavioral/template-method/README.md)：支付重复流程 → 公共步骤 → 固定骨架与钩子
- [策略模式](patterns/behavioral/strategy/README.md)：支付渠道条件分支 → 提取渠道实现 → 策略接口与注册表路由
- [抽象工厂模式](patterns/creational/abstract-factory/README.md)：直接创建 → 独立工厂 → 同渠道支付与退款产品族
- [Spring 注册式工厂](patterns/creational/registered-factory/README.md)：案件处理分支 → 显式注册 → 自动注册
- [工厂与 IOC](patterns/creational/factory-ioc/README.md)：服务定位器 → 构造器注入 → 产品工厂与会话生命周期
- [建造者模式](patterns/creational/builder/README.md)：长构造器 → setter → 不可变人工复核任务
- [单例模式](patterns/creational/singleton/README.md)：共享状态反例 → 同步懒加载 → 安全发布与 Spring 作用域
- [原型模式](patterns/creational/prototype/README.md)：模板共享 → 浅拷贝 → 可变步骤深拷贝
- [代理模式](patterns/structural/proxy/README.md)：反射与直接调用 → 静态事务代理 → JDK 动态代理 → Spring 事务 → 自制简化事务框架
