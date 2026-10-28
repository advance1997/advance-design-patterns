# 桥接模式：拆开催收材料包与输出渠道

## 业务场景

催收案件需要生成不同用途的材料包，例如诉讼材料包和仲裁材料包；材料包又可能交给内部运营或外部合作机构。材料包种类与输出渠道是两个独立变化的维度。

本例只生成内存中的文本，并用具名 Java 类模拟渠道，不生成文件、不加密、不上传，也不调用远程服务。

## 问题与模式结构

若为每种材料包分别编写内部和外部渠道类，新增一种材料包或渠道都会增加一组组合类。桥接模式把两个变化维度拆成抽象层和实现层，并通过组合关联：

```text
材料包抽象（Abstraction）                 输出渠道实现（Implementor）

LitigationMaterialPackage ─┬──────────> InternalChannelBridge
                            └──────────> ExternalAgencyChannelBridge
ArbitrationMaterialPackage ┬──────────> InternalChannelBridge
                            └──────────> ExternalAgencyChannelBridge
```

`MaterialPackageAbstraction` 持有 `OutputChannel`，具体材料包类型决定输出内容，具体渠道负责如何输出。更换任一侧时不需要为每个组合新增专用类。本例用诉讼/仲裁与内部/外部两两组合，测试直接打印四种组合的路径和结果。

## 版本演进

| 版本 | 实现 | 尚存问题 |
| --- | --- | --- |
| v1-naive | `PackageService` 根据渠道字符串选择渠道类 | 业务服务集中选择实现，调用方传字符串；新增材料包和渠道时分支增长 |
| v2-refactor | 把案件编号和场景整理为 `MaterialPackage` 数据对象 | 数据表达更清楚，但服务仍集中选择渠道，变化维度没有拆开 |
| v3-pattern | 材料包抽象持有 `OutputChannel` 接口；诉讼、仲裁和各渠道分别实现自己的职责 | 两个维度可以独立扩展和组合 |

```text
v3-pattern
MaterialPackageAbstraction
├── LitigationMaterialPackage
└── ArbitrationMaterialPackage
          │ 持有
          ▼
OutputChannel
├── InternalChannelBridge
└── ExternalAgencyChannelBridge
```

## 与相近模式的区别

- 桥接从设计时就拆开两个独立变化维度；适配器则让已有的不兼容接口能够协作。
- 桥接建立抽象与实现的长期关联；策略通常是在同一职责下替换一组算法。
- 装饰器在同一接口上逐层增加职责；桥接不会把渠道包装成材料包的附加行为。

## 测试与运行

每个版本一个打印式用例。v1/v2 用同一诉讼材料包走两个渠道，观察服务分支；v3 组合两种材料包与两个渠道，观察四种组合都通过同一抽象接口调用。测试不使用断言框架，也不扩展到文件、网络或性能测试。

```bash
mvn -f patterns/structural/bridge/pom.xml test
mvn -f patterns/structural/bridge/v3-pattern/pom.xml test
```

所有源码兼容 JDK 1.8，版本相互独立。该示例聚焦桥接结构，因此不引入 Spring 或其他框架。
