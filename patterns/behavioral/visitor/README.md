# 访问者模式：催收诉讼材料操作扩展

## 专题案例与范围

本实践对应《01-1｜设计模式专项》第二十五讲。案例是虚构的催收诉讼材料目录树：诉讼材料包包含合同目录和催收目录；叶子节点为 PDF 与录音。业务需要对这组相对稳定的节点执行材料统计和示例规则校验。

样例数据与课程一致：3 个目录、2 个 PDF、2 段录音，总大小 7680 字节。`借款协议.pdf` 是要求签名但未签名的材料；`外呼录音B.mp3` 模拟为不可访问。所有数据均在内存中，不读取真实材料、不连接数据库或远程服务。校验规则仅供理解模式，不代表司法材料合规标准。

课程还提出材料清单导出作为一种可增加的操作。本次可运行实验按课程代码实验聚焦统计和校验；清单导出作为可新增 `ManifestVisitor` 的扩展点，不再引入第三个演示操作。

## 版本演进

| 版本 | 实现 | 观察重点 |
| --- | --- | --- |
| v1-naive | `MaterialNode` 将统计、校验操作定义在元素接口中，由目录递归委托 | 数据对象逐渐承载多种业务操作；新增操作需要修改元素接口及所有节点类 |
| v2-refactor | 元素只保存数据；`MaterialStatisticsService` 和 `MaterialValidationService` 分别用 `instanceof` 判断节点类型 | 操作与数据分离，但每种操作重复节点类型分支；增加节点时要修改所有 Service |
| v3-pattern | `MaterialNode.accept` + `MaterialVisitor.visit(具体节点)`，目录递归转交访问者 | 新增操作通过增加 Visitor 完成；增加元素类型则需扩展 Visitor 接口及已有实现 |

每个版本有独立 Maven 子模块、领域对象、源码和测试，不跨版本共享实现。

## 双重分派与模式协作

```text
MaterialNode node = new PdfDocument(...)
node.accept(visitor)
       │
       ├─ 第一次：运行时按实际节点类型调用 PdfDocument.accept(visitor)
       │
       └─ visitor.visit(this)
            ├─ 编译期按 this 的静态类型选择 visit(PdfDocument)
            └─ 运行时按访问者的实际类型执行 StatisticsVisitor 或 ValidationVisitor
```

重载选择不是运行时根据参数对象类型完成。`accept` 先通过多态进入具体元素实现；`this` 的静态类型让编译器选定正确的 `visit` 重载；随后访问者接口调用再按访问者实际类型进行动态分派。

本例与前序模式的关系：

- 组合模式负责构造目录树；v3 的 `MaterialFolder` 是组合节点，PDF 和录音是叶子。
- 迭代器负责逐个取得子节点；目录遍历使用 JDK `List` 的增强 for（底层迭代器），Visitor 不管理遍历游标。
- 访问者负责根据节点类型执行当前操作；统计和校验由不同 Visitor 完成。

结构与操作的取舍：元素类型稳定且操作经常增加时适合 Visitor；若节点类型本身不断增加，每个 Visitor 都需要适配新节点，代价较高。状态化访问者（本例中的计数与问题集合）应为单次操作新建，不应作为并发共享的有状态单例。

这不是策略模式：策略是在一个业务目标下替换一种算法；Visitor 则是在一组不同元素类型上增加操作，并通过双重分派为每种元素执行相应逻辑。

## 测试与运行

每个版本只有一个打印式用例，先打印期望，再展示执行路径与结果；JUnit 仅负责发现和运行，不使用断言。

```bash
mvn -f patterns/behavioral/visitor/pom.xml test
mvn -f patterns/behavioral/visitor/v3-pattern/pom.xml test
```

全部代码兼容 JDK 1.8。JDK 中的 `FileVisitor` / `Files.walkFileTree` 借鉴了遍历机制与操作分离的思想，但并不完全等同于本例使用 `accept` 和重载所实现的经典 GoF 双重分派。
