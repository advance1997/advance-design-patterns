# advance-design-patterns

使用 Java 21 通过可运行、可测试的独立版本，学习并实践经典设计模式。

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
