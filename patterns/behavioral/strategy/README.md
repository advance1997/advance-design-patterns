# 策略模式：支付渠道算法的选择与执行

## 课程来源与实现范围

已核对《01-1｜设计模式专项》历史会话：

- 会话：`01a0ba95-6029-74e0-a6e2-c68184b4d5db`。
- 大纲回合：`01a0ba95-6670-7ed0-8d27-9b2279bc770d`。
- 阶段 1「策略模式」：支付渠道场景、从 if-else 推导策略抽象、
  策略选择与执行分离、Map 注册表、枚举路由边界和策略无状态化。
- 阶段 10「Version 1 / Version 2」：隔离渠道支付算法，分离创建、选择和执行。
- 初始支付示例：支付宝与微信有不同的调用方式、成功标识和交易号字段。

本次是依据上述场景编写的可运行工程化补充，不是逐字复刻完整课程讲义。
使用模拟客户端演示支付宝 code/tradeNo 与微信 success/transactionId 差异；
微信按分调用、精确金额转换、错误输入和注册边界测试属于仓库补充。
不连接真实平台，不实现签名、证书、数据库、事务或 Spring。

## 分层与版本

| 版本 | 实现方式 | 暴露的问题或解决的问题 |
| --- | --- | --- |
| v1-naive | PaymentService 内直接分支、调用网关、转换响应 | 新增渠道或协议变更均修改核心服务 |
| v2-refactor | 提取 AlipayPayment、WechatPayment | 协议变化被隔离，但服务仍依赖具体类及渠道分支 |
| v3-pattern | PaymentStrategy + PaymentStrategyRegistry + PaymentService | 创建在装配端、选择在注册表、执行在具体策略 |

三个版本包前缀分别为：

```text
com.advance.patterns.behavioral.strategy.v1
com.advance.patterns.behavioral.strategy.v2
com.advance.patterns.behavioral.strategy.v3
```

各自持有 domain、gateway、service 和测试；V2 增加 payment 包，
V3 增加 strategy、registry 包。版本之间、模式之间不共享业务类或相互依赖，
相同领域对象的重复代码是学习隔离的有意选择。

## 建议阅读顺序

1. 阅读 V1 的 PaymentService.pay：选择与算法为什么耦合在一起？
2. 对比 V2 的 PaymentService 和两个 payment 类：拆类为什么仍未消除扩展修改？
3. 阅读 V3 的 PaymentStrategy：渠道算法如何通过同一契约替换？
4. 阅读 PaymentStrategyRegistry：为什么重复注册要启动即失败，而不是后者覆盖前者？
5. 阅读 V3 的 PaymentService：它只依赖策略接口，不认识支付宝或微信实现。
6. 运行 StrategyRegistryTest.testNewChannel：仅装配具名 UnionPayStrategy，
   不修改服务和注册表即可执行新算法。

V3 正常路径：

```text
PaymentTest 显式 new 策略和模拟客户端
→ PaymentStrategyRegistry 注册策略
→ PaymentService.pay 校验请求
→ PaymentStrategyRegistry.getStrategy 选择渠道
→ AlipayStrategy.pay / WechatPayStrategy.pay 执行算法
→ 模拟客户端 → 响应转换 → PaymentService 返回统一结果
```

PaymentCommand.validate 在服务入口与具体算法入口均有保护，
所以 V2/V3 正常路径会看到两次校验；不会重复发起支付。
这里只用构造器和普通集合装配，没有自动扫描或隐藏注册机制。

## 行为契约与异常边界

- 支付单号非空，渠道非空；金额使用 BigDecimal，为正数且最多两位有效小数。
- 12.34 元转换为 1234 分；12.340 元可接受；0.001 元拒绝，不做隐式四舍五入。
- 微信元转分使用 longValueExact，溢出在网关调用前拒绝。
- 支付宝 code 为 10000、微信 success 为 true，且交易号有效，才产生成功结果。
- 空响应、拒绝响应、空白交易号抛出 PaymentException。
- 未注册渠道、重复渠道、空策略列表、空元素、空渠道都明确抛异常。
- V3 同时拒绝策略返回 null 或返回其他渠道的结果。
- 超时异常原样传播，不自动重试，不默认降级到另一支付渠道。
- 注册表构造时复制输入列表的映射关系，不保留列表引用，不提供动态修改入口；
  清空原始列表不影响已注册策略。
- 策略只保存客户端依赖，不保存当前请求；真实并发安全仍取决于客户端。
  测试替身的调用计数仅用于单线程观察，不代表生产客户端实现。

## 打印式测试与运行

将 JAVA_HOME 指向 JDK 1.8，在仓库根目录执行：

```bash
# 全项目
./mvnw test

# 策略模式全部版本
./mvnw -f patterns/behavioral/strategy/pom.xml test

# 每个版本均可独立运行，无需先 install 其他版本
./mvnw -f patterns/behavioral/strategy/v1-naive/pom.xml test
./mvnw -f patterns/behavioral/strategy/v2-refactor/pom.xml test
./mvnw -f patterns/behavioral/strategy/v3-pattern/pom.xml test

# 只观察新增渠道如何接入
./mvnw -f patterns/behavioral/strategy/v3-pattern/pom.xml \
  -Dtest=StrategyRegistryTest#testNewChannel test
```

每个版本的 PaymentTest 分别演示两种渠道成功、1 分金额、尾随零、
空请求、空白单号、空渠道、空/零/负金额、小数精度、分金额溢出、
未支持渠道、渠道拒绝、缺少交易号、空响应和超时。
V3 的 StrategyRegistryTest 额外观察新增渠道、重复注册、非法装配、
列表隔离、错误策略返回值和空依赖。

每个测试方法上方的中文 Javadoc 都标注输入、期望值或异常及关键路径，
方法内再次打印期望并输出实际值。正常路径应观察对应客户端恰好调用一次；
非法输入路径不应出现客户端调用。
所有测试都是普通 Java 方法和具名模拟类，无断言、参数化测试、Lambda、
方法引用、Consumer 或 Function。异常用 try/catch 打印，
若没有抛出则输出“未抛出异常，请对照期望检查”。

**JUnit 绿色和 BUILD SUCCESS 只表示执行完成，不自动证明打印结果符合期望。**
尤其是预期异常测试捕获 RuntimeException 后会输出实际异常类型，
需要对照注释检查类型、消息和调用路径是否一致。

## 设计收益与边界

- 策略替换的是整体渠道算法；模板方法约束的是算法内部的固定步骤。
  本次不复用模板方法模块，也不引入抽象父类或回调函数。
- 注册表只管理已创建的策略，并不是工厂方法模式：
  没有让子类通过工厂方法决定具体产品，不能把 Map 查找称为工厂方法。
- 扩展已有枚举渠道只需新增实现并在装配端注册；引入全新渠道标识仍要修改
  PaymentChannel 枚举。这不是无限动态插件机制，也不是零修改系统。
- 银联仅为测试扩展示例，不代表已经实现真实银联支付。
- 少量且稳定的简单分支未必需要策略；抽象会增加类数量和调用间接性。
- 不把所有 if 都消除：参数校验和响应成功判断仍是必要的业务约束。
- 不提供默认支付策略：渠道拼错或未注册应暴露问题，不应替用户换支付方式。
- 不实现动态配置、Spring IOC 自动注册、Strategy + Factory 独立演进版本、
  策略组合、框架源码训练；这些是课程后续主题，不将基础实践标记为全部完成。
- 真实支付还需要异步回调、幂等、验签、对账、补偿与事务边界。
  超时可能意味着支付已发生，异常不代表外部资金操作已回滚。
