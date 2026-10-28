# 适配器模式：统一催收材料平台接口

## 来源与范围

在代理、装饰器之后继续适配器，不改变此前确认的实践顺序。
《01-1｜设计模式专项》历史记录包含外部材料接口适配的话题，但本次读取对应
讲解仍只得到引用标记，没有正文。以下采用本轮用户确认的“不同材料平台统一调用”
场景独立实现，不宣称已核对或复刻课程代码。

只模拟归集借款合同的查询：不下载真实文件、不外呼、不操作资金、不判断材料合规。
找到材料不等于材料审核通过。平台客户端固定使用教学数据，不访问网络。
案件 10001 在两个平台都有模拟合同，10002 没有合同。

## 为什么需要适配器

归集服务希望统一调用 `fetchContract(caseId)`，但两个平台的 API 不一致：

| 差异 | Alpha 平台 | Beta 平台 | 内部统一契约 |
| --- | --- | --- | --- |
| 方法 | fetchContract(String loanNo) | queryFile(long businessId, int documentType) | fetchContract(long caseId) |
| 请求参数 | LOAN-10001 | 10001、10 | 10001 |
| 找到材料 | code=0000 | exists=true | found=true |
| 未找到 | code=4040 | exists=false | found=false、fileKey 为空字符串 |
| 文件字段 | fileToken | objectKey | fileKey |

适配器既转换调用参数，也解释状态语义并转换返回字段，而不只是把方法换个名字。
未知 Alpha 状态会抛异常，不能将技术异常或未知状态伪装成材料未找到。
本例的案件到借据映射及类型编码仅为教学约定；真实系统需采用经过确认的标识映射，
不能假定内部案件编号永远等于供应商业务编号。

## 四个独立版本

| 版本 | 结构 | 阅读重点 |
| --- | --- | --- |
| v1-naive | 归集服务直接调用两个 SDK 并转换 | 平台方法、参数和状态规则都侵入业务入口 |
| v2-refactor | 提取 AlphaMaterialReader、BetaMaterialReader | 转换职责移走，但服务仍依赖两个具体类和不同调用方法 |
| v3-pattern | 两个对象适配器实现 MaterialGateway | 业务服务只依赖目标接口，用同一个方法调用两个平台 |
| v4-spring-mvc | Spring 注入业务适配器，MVC 调度不同处理器 | 同时观察业务适配和框架 HandlerAdapter 两种边界 |

每个版本包含自己的 domain、external、service；v2 增加 reader，v3/v4 增加 port、adapter，
v4 再增加 web、config。各版本无业务代码共享或相互依赖。
external 中的客户端与响应类模拟第三方 SDK：观察演进时不要通过修改它们来统一接口。

### v3 的模式角色

- Target：MaterialGateway，内部业务需要的目标契约。
- Adaptee：AlphaMaterialClient、BetaMaterialClient，各自拥有不同接口。
- Adapter：AlphaMaterialAdapter、BetaMaterialAdapter，实现目标接口并组合原有客户端。
- Client：MaterialCollectionService，只认识 MaterialGateway 和 MaterialFile。

```java
MaterialCollectionService service = new MaterialCollectionService(
        new AlphaMaterialAdapter(new AlphaMaterialClient()));
MaterialFile material = service.collectContract(10001L);
```

切换为 BetaMaterialAdapter 时，服务类与 collectContract 调用方式都不变。
装配位置仍需要选择平台；适配器本身不负责自动路由或选择供应商。
本例用组合实现“对象适配器”，没有通过继承第三方类实现“类适配器”；
组合更适合已有 SDK、依赖注入及 Java 单继承约束，不另加一套无关版本。

## 为什么选择 Spring MVC

本次没有数据库读写需求，因此不引入 MyBatis。Spring Boot 主要简化应用启动与配置，
不是理解本例适配边界的必需项；使用 Spring MVC 自身能更直接看到 HandlerAdapter 的职责。
选择两种现成处理器契约是为了观察源码，不是建议新项目都采用旧式 Controller。
现代注解控制器通常由 RequestMappingHandlerAdapter 适配，本例不再扩展第三类处理器。

v4 注册两条教学请求路径：

| 路径 | 处理器契约 | Spring MVC 内置适配器 | 下层业务适配器 |
| --- | --- | --- | --- |
| /materials/alpha?caseId=10001 | Controller | SimpleControllerHandlerAdapter | AlphaMaterialAdapter |
| /materials/beta?caseId=10001 | HttpRequestHandler | HttpRequestHandlerAdapter | BetaMaterialAdapter |

```text
MockMvc 模拟请求
  → DispatcherServlet
  → SimpleUrlHandlerMapping 找到具体 handler
  → HandlerAdapter.supports(handler) 找到匹配的适配器
  → HandlerAdapter.handle(...) 调用不同类型的 handler
  → MaterialCollectionService.collectContract
  → MaterialGateway 的具体业务适配器
  → 平台模拟 SDK
  ← 统一 MaterialFile → UTF-8 文本响应
```

HandlerMapping 负责“找到谁”，HandlerAdapter 负责“怎样调用它”。
框架适配器统一的是处理器调用契约，业务适配器统一的是材料平台接口；两者不是同一个对象。
@Qualifier 区分 Alpha/Beta 的端口和业务服务注入，不是适配器模式本身。
MockMvc 使用真实 DispatcherServlet 调度，并非测试直接调用 Controller 方法；不启动监听端口。
未增加 Web 参数校验、认证或统一异常响应等生产功能，请按测试中的教学输入运行。

建议在以下位置打断点，逐步观察转换：

1. MaterialCollectionService.collectContract 与两个业务适配器的 fetchContract。
2. DispatcherServlet.getHandlerAdapter，观察 supports 如何选中适配器。
3. SimpleControllerHandlerAdapter.handle 与 HttpRequestHandlerAdapter.handle，观察它们调用不同处理器接口。

Spring MVC 使用项目的 Java 8 兼容版本 5.3.39，Servlet API 使用 4.0.1。
旧版本仅为教学兼容，不作为生产安全选型建议；不引入 Boot BOM 或变更其他模式依赖。

## 与已学模式的区别及边界

- 装饰器保持原来的接口，逐层增加职责；适配器把不兼容的接口变成业务需要的接口。
- 代理控制对目标的访问；适配器主要转换调用契约。
- 策略关注选择不同算法；这里两个实现主要是在翻译平台协议，不是更换归集算法。
- 收益是隔离外部变化；代价是多出转换类，且需要持续维护正确的字段与状态映射。
  如果上游本来就遵守统一接口，没有必要机械增加一层转发类。

## 测试与运行

仅 9 个打印式测试：v1 两个平台各 1 个；v2 两个平台各 1 个；v3 两个平台各 1 个，
加 1 个“不同缺失状态转成同一语义”；v4 两种 HandlerAdapter 调度各 1 个。
测试只覆盖模式核心，不增加鉴权、数据库、并发、重试、文件导出等无关场景。

方法上方标明输入、期望结果和执行路径。JUnit 仅发现并执行测试，不使用断言框架、
参数化测试或函数式回调；成功不表示结果被自动判定，需对照控制台输出。
正常查询的预期为 caseId=10001、found=true，fileKey 分别是 alpha-contract-10001、beta-contract-10001。
MVC 测试额外输出匹配适配器类名、HTTP 200 以及统一响应。

使用 JDK 1.8 和 Maven 3.9.x，在仓库根目录运行：

```bash
mvn test
mvn -f patterns/structural/adapter/pom.xml test
mvn -f patterns/structural/adapter/v3-pattern/pom.xml test
mvn -f patterns/structural/adapter/v4-spring-mvc/pom.xml test
```

v1、v2 同样可通过各自 pom.xml 独立运行。使用本机 Maven，不恢复已清理的 Wrapper 配置。
