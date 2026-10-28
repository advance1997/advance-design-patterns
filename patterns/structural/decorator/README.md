# 装饰器模式：催收材料清单导出

## 来源与本次范围

按此前确认的专题顺序，在代理模式之后实践装饰器模式，不推进其他模式。
《01-1｜设计模式专项》的历史记录可确认包含 Java IO 与催收材料归集话题，
但本次接口未返回对应讲解正文，不能宣称已核对原文示例。
用户本次明确授权自行设计材料归集案例，并要求测试只围绕模式思想。
因此以下是本仓库的独立教学案例，不是课程代码复刻。

业务输入是已归集的虚构材料名称：DEMO-001 的借款合同、还款流水。
输出是内存中的清单文本，可追加“人工复核说明”和“教学水印”。
不实现材料下载、外部接口、真实 PDF 水印、加密、合规审核或文件外发。
附加说明不代表材料已审核通过，文本水印也不提供防泄露保证。

## 独立版本与演进

| 版本 | 实现 | 要感受的问题或收益 |
| --- | --- | --- |
| v1-naive | 一个服务使用两个 boolean 分支拼装文本 | 增强职责和执行顺序都集中在服务中 |
| v2-refactor | 拆出 ReviewNoteAppender、WatermarkAppender | 职责清楚了，但服务仍控制分支和顺序；还不是装饰器 |
| v3-pattern | 同接口组件逐层包装 | 基础组件不变，调用方通过组合选择增强与顺序 |
| v4-spring | Spring 的 @Bean、@Qualifier 和构造器注入 | 将组合决策移到配置中，业务服务仅依赖 MaterialExporter |

新增 v4 是为了学习框架如何装配完整业务组件，不添加新的业务功能。
四个版本各自拥有领域对象、实现和测试，不导入其他版本的业务代码。
domain 保存材料快照，service 承载业务入口，export 定义组件与装饰器，
config 仅在 Spring 版本负责对象装配。

## 模式的关键结构

- Component：MaterialExporter，所有组件都遵守 export 契约。
- ConcreteComponent：PlainMaterialExporter，只生成基础材料清单。
- Decorator：MaterialExporterDecorator，实现同一接口并持有 MaterialExporter。
- ConcreteDecorator：ReviewNoteDecorator、WatermarkDecorator，各追加一种内容。

最重要的不是类名，而是“与被包装对象使用同一接口，并通过委托叠加职责”。
基类使用继承统一委托方式；增强能力的组合则依靠持有组件，而非为每种组合创建子类。

```java
MaterialExporter exporter = new WatermarkDecorator(
        new ReviewNoteDecorator(new PlainMaterialExporter()));
String text = exporter.export(bundle);
```

调用与返回顺序：

```text
WatermarkDecorator 进入
  → ReviewNoteDecorator 进入
    → PlainMaterialExporter 生成基础清单
  ← ReviewNoteDecorator 追加复核说明
← WatermarkDecorator 追加水印
```

最终输出：

```text
案件：DEMO-001
材料清单：
- 借款合同（模拟）
- 还款流水（模拟）
复核说明：材料仅供人工复核，不代表审核通过。
水印：内部教学材料，禁止外传。
```

交换两个装饰器后，清单不变，但附加文本变为先水印、后复核说明。
这是因为本例的增强发生在委托调用返回之后，而不是进入之前。
客户端始终调用同一个 export 方法，无需了解内部包了几层。

## Spring 如何落地

MaterialExportConfig 显式声明 plainExporter、reviewExporter、watermarkedExporter。
每层参数使用 @Qualifier 指定内层 Bean，避免多个同接口 Bean 注入歧义。
MaterialExportService 通过构造器获得 watermarkedExporter，直接调用接口。
另外可按 Bean 名获取 reviewExporter，得到不带水印的复核清单。

Spring 管理实例与依赖关系，装饰器负责增加导出职责。这里没有使用 AOP 动态代理；
不是加上 @Bean 就自动成为装饰器。包装顺序由配置中的依赖关系决定，
并不是方法声明顺序或 @Order 的排序结果。
组件只保存不可变的委托引用，单次导出的文本在方法内生成，不放到共享 Bean 字段中。
使用项目统一的 Spring Framework 5.3.39 兼容 Java 8，仅供教学，不作为生产版本建议。

## 与相邻概念的区别及代价

- 代理主要控制对目标的访问，例如上一模式的事务边界；装饰器主要给组件叠加职责。
  两者结构可能相似，应按设计意图区分，不能只看是否持有 delegate。
- 策略通常选择一种算法，装饰器可将多个增强一起组合。
- 本例每层都调用内层并在返回时增强，不是根据条件转交或终止处理的责任链。
- Java IO 中 FilterInputStream 持有 InputStream，BufferedInputStream 在同一读取契约上增强缓冲，
  可以对照“同接口、持有内层、委托增强”的结构理解；本模块不额外增加 IO 实验。

适合基础职责稳定、增强可独立组合的情况。代价是对象层数增加、调试路径更长、
顺序影响结果，重复包装也会重复追加内容。本例不引入自动排序或去重机制。
如果只有固定的少量步骤，简单方法调用可能更清楚，不需要强行装饰。

## 阅读与测试

建议按 v1 → v2 → v3 → v4 阅读。先在 v3 的 export 方法打断点，
观察进入与返回，再看 v4 的配置如何替代客户端手工组装。

仅保留 10 个打印式测试：

| 版本 | 用例数 | 观察点 |
| --- | --- | --- |
| v1 | 2 | 基础输出、集中追加两个增强 |
| v2 | 2 | 基础输出、职责拆分但仍集中编排 |
| v3 | 4 | 基础组件、单层装饰、多层组合、交换顺序 |
| v4 | 2 | 业务服务使用容器装配链、选择不同导出链 |

每个测试方法上方均写明输入、期望结果和执行路径，不使用断言、参数化测试或函数式回调。
JUnit 成功仅说明执行完成，需对照打印结果理解行为；不扩展无关异常或生产可靠性测试。

设置 JAVA_HOME 为本机 JDK 1.8，使用已安装的 Maven 3.9.x：

```bash
# 仓库根目录，全量运行
mvn test

# 本模式全部版本
mvn -f patterns/structural/decorator/pom.xml test

# 独立运行装饰器版本
mvn -f patterns/structural/decorator/v3-pattern/pom.xml test

# 独立运行 Spring 落地版本
mvn -f patterns/structural/decorator/v4-spring/pom.xml test
```

其他版本也可通过各自的 pom.xml 独立运行。当前主分支已清理 Maven Wrapper 配置，
因此这里使用本机 mvn，不要求恢复该配置文件。
