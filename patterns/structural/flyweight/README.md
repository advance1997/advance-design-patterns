# 享元模式：共享催收材料规则，隔离案件执行状态

## 场景与范围

后续新增实践遵循本轮约定：只使用内存对象和具名模拟类，不实现真实数据库、远程服务或外部接口。
本例模拟多个催收案件使用同一套“诉讼材料规则”：借款合同、还款流水为必需材料，外呼录音为可选材料。
规则定义对所有案件相同；每个案件实际归集了哪些材料则不同，必须独立保存。

享元不是简单的全局单例，也不是把所有案件对象放进一个缓存。它的核心是区分：

- 内在状态（Intrinsic State）：材料编码、是否必需，多个案件可以共享。
- 外在状态（Extrinsic State）：案件编号、已归集材料列表，必须由案件任务自己保存。

本例不下载文件、不访问 OSS、不判断真实合规、不做性能压测或并发安全验证。对象创建次数只用于直观教学，
不能据此推导生产内存收益。规则数量很少时直接创建完全可以；只有大量重复、可安全共享的对象才值得考虑享元。

## 三个独立版本

| 版本 | 做法 | 观察点 |
| --- | --- | --- |
| v1-naive | 每个案件重新创建三条 MaterialRule | 同样的内在规则重复占用对象 |
| v2-refactor | 抽取 buildRules 构建方法 | 代码更集中，但仍然每次创建，没有共享 |
| v3-pattern | FlyweightFactory 缓存不可变 MaterialRuleFlyweight | 规则对象复用，CollectionTask 保存案件独立状态 |

每个版本拥有自己的 domain、service、测试；v3 额外包含 flyweight 包。版本之间无业务代码依赖，均为 Java 8 普通类。
本次不添加 Spring 或数据库版本：用户已明确后续练习直接使用模拟场景，且享元的核心更适合先观察对象边界。

## v3 关键结构

```text
CollectionService
      │ 组合内在状态和外在状态
      ├── FlyweightFactory ── MaterialRuleFlyweight（共享、不可变）
      └── CollectionTask（案件独立的 collected 列表）
```

`FlyweightFactory.get("CONTRACT", true)` 对相同键返回同一个享元对象；`CollectionTask` 不进入工厂缓存。
案件 DEMO-001 追加 RECORDING 后，DEMO-002 的列表不变化，这证明外在状态没有放进共享对象。
工厂按 `code:required` 组成教学键；生产系统还应明确规则版本、租户和配置发布边界，不能只用材料编码做键。

不要把可变的案件编号、文件地址、归集结果放进 `MaterialRuleFlyweight`。享元被多个请求使用，任何可变字段都会造成跨案件污染。
本例工厂使用普通 `LinkedHashMap`，仅为单线程教学；生产并发场景需要根据生命周期和并发要求选择安全的缓存实现。

## 与其他模式的区别

- 单例保证某个对象实例数量，享元允许按键共享一批对象；多个享元对象仍然存在。
- 原型从已有复杂对象复制出独立副本；享元刻意共享不变内在状态，避免复制。
- 工厂负责创建或获取对象；`FlyweightFactory` 是享元结构中的创建与缓存协作者，不等同于工厂模式本身。
- 享元不负责案件流程、材料校验或远程查询；这些职责不能因为使用缓存而混入共享对象。

## 测试与运行

仅 3 个打印式测试，每个版本 1 个：输入 DEMO-001、DEMO-002，观察两案相同规则和独立列表。
测试不做断言、参数化、性能基准或并发压力；方法 Javadoc 写明期望结果，运行后直接对照控制台。

v1/v2 期望每次创建三条规则；v3 期望工厂缓存 3 个享元，追加可选材料只改变第一个案件。

```bash
mvn test
mvn -f patterns/structural/flyweight/pom.xml test
mvn -f patterns/structural/flyweight/v3-pattern/pom.xml test
```

使用 JDK 1.8 和 Maven 3.9.x；本模式不需要数据库、Spring、MyBatis 或网络服务。
