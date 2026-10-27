# 抽象工厂：同渠道的支付与退款产品族

## 课程来源与补充边界

已核对《01-1｜设计模式专项》历史会话：

- 会话标识：`01a0ba95-6029-74e0-a6e2-c68184b4d5db`。
- 大纲回合：`01a0ba95-6670-7ed0-8d27-9b2279bc770d`。
- 阶段 2「工厂体系」要求依次推导简单工厂、工厂方法、抽象工厂、注册式工厂。
- 本次对应的问题是：“多个相互匹配的产品如何成组创建？”
- 课程初始支付示例和综合重构主线使用支付宝、微信支付业务背景。

可读的大纲没有指定抽象工厂的具体产品组合。因此本实践沿用支付背景，
将“同渠道支付客户端 + 退款客户端”作为工程化补充，不宣称这是课程原始代码。
元/分转换、模拟退款、产品族一致性检查和边界测试也是本仓库补充。
不沿用尚未完成课程对齐的旧物流示例，也不引入无关的 GUI 控件案例。

所有客户端只打印模拟调用并返回教学流水号，不连接平台，不操作真实资金。

## 产品种类和产品族

| 产品族 | 支付产品 PaymentClient | 退款产品 RefundClient |
| --- | --- | --- |
| 支付宝 | AlipayPaymentClient | AlipayRefundClient |
| 微信 | WechatPaymentClient | WechatRefundClient |

支付和退款是两种产品职责；支付宝和微信是两个产品族。
只把支付宝与微信当作两种支付算法替换，重点仍是策略模式，
不能因为使用了接口或工厂名称就称为抽象工厂。

## 三个独立版本

| 版本 | 阅读入口 | 变化和问题 |
| --- | --- | --- |
| v1-naive | service/PaymentService | 支付和退款各自分支并直接 new；服务依赖四个具体类 |
| v2-refactor | factory/PaymentClientFactory、RefundClientFactory | 引入产品接口，分别集中创建；调用方仍可独立选择不同渠道 |
| v3-pattern | factory/PaymentComponentFactory | 一次选择产品族，一个工厂提供两类匹配产品 |

目录按 `patterns/creational/abstract-factory/版本` 分层。
包前缀分别为 `com.advance.patterns.creational.abstractfactory.v1`、`v2`、`v3`。
各版本独立拥有 domain、product、service、测试；V2/V3 另有 factory 包。
没有跨版本或跨模式业务依赖，不复用策略或模板方法中的支付对象。

### V1：耦合具体创建

PaymentService 绑定一个渠道，pay 与 refund 分别创建具体客户端。
现有映射是正确的，但增加渠道需要修改两个方法；
复制分支或漏改其中一处就可能使支付和退款选择不一致。

### V2：移动 new，不等于解决产品族问题

两个简单工厂各自正确地生产一种产品。
但是下面的装配仍然合法：

```java
PaymentClient paymentClient = PaymentClientFactory.create(PaymentChannel.ALIPAY);
RefundClient refundClient = RefundClientFactory.create(PaymentChannel.WECHAT_PAY);
PaymentService service = new PaymentService(paymentClient, refundClient);
```

testMismatchedProducts 展示：支付宝模拟支付先完成，直到调用微信退款才发现渠道不一致。
退款客户端会拒绝错误请求，不会伪造退款成功。
V2 刻意暴露“创建已集中，配套关系却交给调用方”的过渡问题，
不是推荐的生产实现，也不表示这一步在所有维度都优于 V1。

### V3：创建整组产品

PaymentComponentFactory 同时声明：

- getChannel：整个产品族的渠道。
- createPaymentClient：支付产品。
- createRefundClient：退款产品。

AlipayComponentFactory 和 WechatComponentFactory 各自提供匹配的两个具体产品。
PaymentService 构造时从同一个工厂各获取一次产品，校验空值和渠道一致性；
之后只通过抽象产品执行支付或退款，不再包含渠道创建分支。

PaymentFactories 是装配入口的简单工厂选择器，它仍然有渠道判断；
消除的是业务方法里分散的产品选择，而不是禁止项目中出现任何 if。

接口不能自动证明第三方工厂实现正确。因此 FactoryContractTest 还故意提供
返回空产品、混合渠道、错误声明渠道的工厂，在发起业务操作前拒绝它们。
渠道标识检查只是教学防线，不能验证真实商户号、证书、API 版本是否匹配。

## 运行与对照

将 JAVA_HOME 指向 JDK 1.8，在仓库根目录运行：

```bash
./mvnw test
./mvnw -f patterns/creational/abstract-factory/pom.xml test
./mvnw -f patterns/creational/abstract-factory/v1-naive/pom.xml test
./mvnw -f patterns/creational/abstract-factory/v2-refactor/pom.xml test
./mvnw -f patterns/creational/abstract-factory/v3-pattern/pom.xml test

# 观察 V2 的错配何时才被发现
./mvnw -f patterns/creational/abstract-factory/v2-refactor/pom.xml \
  -Dtest=PaymentFamilyTest#testMismatchedProducts test

# 对比 V3 在装配阶段即拒绝错误产品族
./mvnw -f patterns/creational/abstract-factory/v3-pattern/pom.xml \
  -Dtest=FactoryContractTest#testMixedProducts test
```

每个测试方法的中文 Javadoc 标明输入、期望结果或异常以及关键路径；
方法内部直接调用和打印。测试不用断言、参数化测试、Lambda、方法引用或 Consumer。
预期异常使用 try/catch 打印实际类型和信息；未抛异常会明确提示人工核查。
JUnit 绿色或 BUILD SUCCESS 仅代表执行完成，不自动证明打印值符合预期。

建议按以下顺序观察：

1. 各版本 testAlipayFamily、testWechatFamily：支付与退款是否使用同渠道产品。
2. 微信支付 12.34 元是否打印 1234 分，退款 2.34 元是否打印 234 分。
3. V2 testMismatchedProducts：能看到模拟支付，但看不到成功的模拟退款调用。
4. V3 testMixedProducts：只看到装配过程，不应出现 PaymentService.pay 或模拟渠道调用。
5. V3 testProductsCreatedOnce：两个创建方法各调用一次，两笔支付的凭证不混淆。
6. 超时测试：异常向上传播，不自动换渠道、退款或重试。

## 参数与资金语义的边界

- 支付单号、交易号、渠道和原支付凭证不能为空。
- 金额使用 BigDecimal，必须为正且最多两位有效小数；12.340 合法，0.001 非法。
- 微信按分模拟调用，使用精确转换，超出 long 范围时拒绝，不截断或舍入。
- 退款必须匹配原支付渠道，单次退款金额不能超过原支付金额。
- 测试允许独立构造凭证以隔离退款场景，这不意味着生产环境可以信任调用方传入的凭证。
- 没有累计退款账本、退款申请号、幂等、并发控制、验签、回调或对账。
  重复调用退款会重复打印；教学流水号不保证唯一，不能用于真实业务。
- pay 与 refund 是两个独立操作；支付不会自动触发退款，异常也不意味着资金已回滚。
- V3 客户端依赖只在装配时保存，不保存单次请求状态；
  替换为真实 SDK 后仍需核对线程安全、连接释放和生命周期管理。

## 收益、代价与相关模式

- 工厂方法通常围绕一个抽象产品，由子类决定实例化哪一种具体产品；
  抽象工厂统一提供多种相关产品，强调配套关系。
- 新增产品族：增加对应具体产品与具体工厂，调整装配入口；
  使用现有 PaymentChannel 枚举之外的标识时，还需要扩展枚举。
  PaymentService 的支付/退款方法不用随之增加分支。
- 新增产品种类，例如查询客户端：需要修改抽象工厂接口及所有具体工厂。
  抽象工厂适合产品种类相对稳定、产品族需要替换的场景。
- 只有单个产品，或产品之间不存在配套约束时，普通构造器、简单工厂可能更清晰。
- 策略关注运行算法替换；模板方法关注稳定流程；本实践只聚焦产品族创建，
  不将已有模式全部叠加。
- 注册式工厂、IOC 生命周期和 BeanFactory/FactoryBean 的区分属于课程后续内容，
  本次未实现 Spring 集成或框架源码练习。
