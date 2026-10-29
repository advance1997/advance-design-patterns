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
examples/
└── comprehensive/ # 多种设计模式协作的综合案例
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
- [装饰器模式](patterns/structural/decorator/README.md)：材料清单集中拼装 → 职责拆分 → 可组合增强 → Spring 装配
- [适配器模式](patterns/structural/adapter/README.md)：材料平台直接调用 → 转换职责拆分 → 统一接口 → Spring MVC 适配调度
- [桥接模式](patterns/structural/bridge/README.md)：材料包与输出渠道分支 → 维度拆分 → 抽象与实现独立扩展
- [组合模式](patterns/structural/composite/README.md)：材料目录树 → 递归职责拆分 → 统一处理材料与目录
- [外观模式](patterns/structural/facade/README.md)：调用方逐个编排材料子系统 → 工作流整理 → 统一流程入口
- [享元模式](patterns/structural/flyweight/README.md)：材料规则重复创建 → 构建职责提取 → 共享规则与案件状态分离
- [责任链模式](patterns/behavioral/chain-of-responsibility/README.md)：外呼集中检查 → 提取规则 → 传递与短路 → Servlet Filter
- [命令模式](patterns/behavioral/command/README.md)：直接同步操作 → GoF 命令对象与排队执行 → 数据命令、后台 Worker 与 Handler
- [迭代器模式](patterns/behavioral/iterator/README.md)：分案服务直接遍历分组 → 提取统一案件池 → 自定义迭代器跨组遍历并跳过空组
- [中介者模式](patterns/behavioral/mediator/README.md)：工作台组件直接互调 → 抽取协调者 → 通过中介者接口协调案件选择、合规校验、外呼与回调
- [备忘录模式](patterns/behavioral/memento/README.md)：和解草稿手工备份 → 独立快照与历史栈 → 封装状态的 Memento，并补充 Command/Mediator/State 组合与版本历史边界
- [综合实战：电商营销优惠引擎](examples/comprehensive/ecommerce-marketing-engine/README.md)：解释器执行优惠规则、组合组织 AST、享元复用 AST、策略计算优惠；MySQL 由内存促销目录模拟
- [观察者模式](patterns/behavioral/observer/README.md)：分派后直接通知 → 职责拆分 → 订阅与广播 → Spring 事件
- [状态模式](patterns/behavioral/state/README.md)：和解方案集中判断 → 规则拆分 → 状态对象 → Spring Boot/MyBatis/事务 Outbox
