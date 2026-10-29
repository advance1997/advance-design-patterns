# 迭代器模式：催收待分案案件池

## 专题来源与案例

本实践对应《01-1｜设计模式专项》第二十一讲“迭代器模式”。专题案例是催收系统的待分案案件池：案件先按业务组存储，分案服务需要依次处理案件，但不应依赖案件池内部究竟采用分组、链表或其他结构。

本例使用虚构案件编号和内存集合，不连接数据库或远程服务。核心目标是观察遍历职责如何从业务服务转移到统一的 `Iterable` / `Iterator` 协议。JDK 集合本身已经实现了迭代器；只有在遍历规则特殊、确有隐藏内部结构的需要时，才值得自定义迭代器。

## 版本演进

| 版本 | 做法 | 演进结果与代价 |
| --- | --- | --- |
| v1-naive | `AssignmentService` 使用两层下标循环访问 `List<List<CaseInfo>>` | 空组可被自然处理，但业务服务了解分组结构，存储结构变化会影响遍历代码 |
| v2-refactor | 提取 `CasePool extends Iterable<CaseInfo>`；分组池先展平为只读列表，再使用 JDK 的 `Iterator` | 分案服务只依赖 `Iterable`；展平复制占用额外内存，也尚未展示自定义遍历状态 |
| v3-pattern | 保留分组快照，由 `GroupedCaseIterator` 用 `groupIndex`、`caseIndex` 跨组遍历 | 遍历游标属于每个迭代器，空组由迭代器跳过，调用方不需要了解内部结构；不支持删除 |

## 角色关系

```text
AssignmentService
       │ 只依赖 Iterable<CaseInfo>
       ▼
   GroupedCasePool ── iterator() ──> GroupedCaseIterator
       │                                  │
       │ 持有分组快照                     ├── hasNext()：跳过空组并检查剩余元素
       └──────────────────────────────────└── next()：返回案件并推进本迭代器游标
```

多个迭代器可以独立遍历同一个案件池，因为位置保存在各自的迭代器对象中，而不是保存在案件池或分案服务中。v3 使用只读快照是为了隔离本次教学中的集合修改；大数据量生产场景不应无条件复制整个集合。数据库分页、游标与流式读取还涉及连接生命周期、事务和失败恢复，不能仅凭实现 `Iterator` 就视为已解决。

## 测试与运行

各版本均提供独立打印式测试，测试 Javadoc 描述期望顺序，运行输出用于人工核对，不使用断言。

```bash
mvn -f patterns/behavioral/iterator/pom.xml test
mvn -f patterns/behavioral/iterator/v3-pattern/pom.xml test
```

全部源码兼容 JDK 1.8。迭代器将遍历机制与业务处理分离；它不负责分案策略、并发安全或数据持久化。
