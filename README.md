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

- 工厂方法模式：直接创建 → 简单工厂 → 工厂方法
- [模板方法模式](patterns/behavioral/template-method/README.md)：支付重复流程 → 公共步骤 → 固定骨架与钩子

## 构建与测试

```bash
./mvnw test
```

只运行工厂方法模式：

```bash
./mvnw -f patterns/creational/factory-method/pom.xml test
```

只运行某一个版本：

```bash
./mvnw -f patterns/creational/factory-method/v1-naive/pom.xml test
```
