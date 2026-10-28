# 外观模式：统一催收材料准备入口

## 业务场景

为案件准备诉讼材料预览，需要先读取案件信息，再按案件类型获取必需材料、核对已归集材料，最后生成预览。每个步骤都由一个简单的内存模拟子系统负责；不连接数据库、不调用远程服务，也不生成真实文件。

示例案件为 `DEMO-001`，类型为诉讼，模拟已归集借款合同和还款流水。重点不是实现材料审核规则，而是观察客户端如何使用多个相互协作的子系统。

## 外观模式解决什么问题

如果调用方必须了解每个子系统、调用顺序和中间结果，客户端就会与流程细节耦合。外观对象提供一个更简单的统一入口，并在内部按顺序协调子系统：

```text
客户端
  │ prepareForReview(caseId)
  ▼
MaterialCollectionFacade
  ├── CaseLookupService：获取案件
  ├── MaterialRuleService：读取必需材料
  ├── MaterialCheckService：核对归集情况
  └── MaterialPackageAssembler：生成内存预览
```

外观并不取代子系统，也不要求所有客户端只能经由外观访问它们；它为常见场景提供一个更易用的入口。

## 版本演进

| 版本 | 调用方式 | 观察点 |
| --- | --- | --- |
| v1-naive | 测试客户端依次调用四个子系统 | 调用方必须掌握完整步骤和顺序 |
| v2-refactor | 工作流服务整理准备步骤，调用方再调用组装器 | 部分流程已集中，但调用方仍知道中间结果和下一步骤 |
| v3-pattern | `MaterialCollectionFacade.prepareForReview` 内部协调所有子系统 | 客户端只需调用一个面向业务的入口 |

v3 的调用关系：

```text
MaterialCollectionFacade.prepareForReview(caseId)
    → CaseLookupService.find
    → MaterialRuleService.getRequiredMaterials
    → MaterialCheckService.check
    → MaterialPackageAssembler.assemble
```

## 与其他模式的区别

- 外观收拢多个子系统的调用方式；适配器解决接口不兼容，不负责定义完整业务流程。
- 外观强调简单入口与流程协调；中介者关注多个对象之间的交互关系和协作规则。
- 外观可以与其他模式共存，但本例只展示子系统组织，不额外引入 Spring 或其他框架。

## 测试与运行

每个版本一个打印式测试，期望路径写在测试方法上方，并在运行时打印。v1/v2 展示调用方需要了解的步骤逐步减少；v3 展示只调用外观一次后，子系统仍按固定顺序执行。没有断言、数据库、远程调用或无关测试。

```bash
mvn -f patterns/structural/facade/pom.xml test
mvn -f patterns/structural/facade/v3-pattern/pom.xml test
```

所有代码兼容 JDK 1.8，三个版本模块互相独立。
