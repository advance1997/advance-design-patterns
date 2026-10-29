# 综合实战：电商营销优惠引擎

## 专题场景

本案例落地《01-1｜设计模式专项》的“综合实战｜电商营销优惠引擎：4 种设计模式如何协同工作”。本场景按专题要求使用电商优惠券，不改成催收场景。营销活动配置原本适合从数据库读取；按用户要求，完整示例改用内存模拟目录，不连接 MySQL 或外部服务。

三张互斥优惠券如下：

| 优惠券 | 使用条件 | 优惠方式 |
| --- | --- | --- |
| VIP立减券 | VIP 用户且订单满 500 元 | 固定立减 20 元 |
| VIP折扣券 | VIP 用户且订单满 500 元 | 9 折，最多优惠 50 元 |
| 新客折扣券 | 首单，且订单满 500 元或购买电子产品 | 9 折，最多优惠 30 元 |

两笔专题订单的业务期望为：VIP 用户购买 600 元商品，可用前两张券；因互斥且选择最大优惠，最终选 VIP 折扣券，优惠 50 元、实付 550 元。新用户购买 300 元电子产品时，新客条件成立，优惠 30 元、实付 270 元。

## 版本结构

| 版本 | 实现 | 目的 |
| --- | --- | --- |
| v1-naive | 一个服务通过优惠券类型的 `if-else` 同时判断资格和计算优惠 | 建立基线，观察优惠规则与计算算法耦合、加券需扩充分支 |
| v2-pattern | 解释器执行 DSL；组合节点构造 AST；享元池缓存不可变 AST；策略按优惠类型计算金额 | 四种模式处理同一业务链路中不同的职责，且分别可以变化 |

两个版本源码、测试和 Maven 依赖完全独立。组合模式与解释器的 AST 只在 v2 内部使用，不依赖仓库里各个 GoF 模式练习模块。

## 四种模式如何协作

```text
内存模拟活动目录
       │ 活动配置：conditionRule + discountType + 优惠参数
       ▼
PromotionEngine
       ├── conditionRule ──> RuleAstPool ──> RuleParser ──> 不可变 Expression AST
       │                                      解释器          组合模式
       │                                                        │
       │                         当前 OrderContext ────────────┘
       │                         每次解释时传入，不保存在 AST
       │
       └── 资格成立 ──> DiscountStrategyRegistry
                              ├── FIXED ──> FixedDiscountStrategy
                              └── PERCENTAGE ──> PercentageDiscountStrategy
```

| 模式 | 在本案例中的职责 | 不负责什么 |
| --- | --- | --- |
| 解释器（Interpreter） | 解析 DSL 并用当前订单上下文执行条件表达式 | 不计算优惠金额，也不选择最优优惠券 |
| 组合（Composite） | 用统一 `Expression` 接口递归组织叶子条件和 AND / OR / NOT 节点 | 不把字符串解析成 token；解析是 `RuleParser` 的职责 |
| 享元（Flyweight） | `RuleAstPool` 按规则文本复用不可变 AST | 不缓存订单或用户上下文，不执行优惠策略 |
| 策略（Strategy） | 资格成立后按活动类型选择固定立减或比例封顶算法 | 不判断优惠券资格，也不决定优惠券之间的互斥政策 |

两条规则解析成树后，`vip && amount500` 对应 AND 与两个条件叶子；`firstOrder && (amount500 || electronics)` 对应 AND、嵌套 OR 和三个叶子。解析器支持 NOT、AND、OR 和括号，优先级为 `!` 高于 `&&` 高于 `||`。

同一 VIP 条件被两张券引用，享元池返回同一个 AST 实例。AST 只保存规则结构，订单属性通过每次调用 `interpret(orderContext)` 传入。该分离避免并发计算时共享表达式节点持有或覆盖其他订单状态。

## 金额与业务政策

金额统一使用 `BigDecimal`。百分比优惠先按两位小数 `HALF_UP` 处理，再应用活动封顶及订单金额上限；固定立减也不会超过订单总额。示例没有计算支付手续费、税额或复杂分摊。

引擎额外采用专题假设的“优惠券互斥，选择优惠最高的一张”政策。它只是该营销业务的选择规则，不是 Interpreter、Composite、Flyweight 或 Strategy 自动提供的能力。系统不会叠加多张券，也不会把这项政策藏进表达式 AST 或单个计算策略。

## 运行与测试

测试用例通过打印期望值与实际路径供人工比对，不使用断言。覆盖原始分支基线、两笔订单、重复规则 AST 身份复用，以及包含 NOT / AND / OR 的嵌套表达式。

```bash
mvn -f examples/comprehensive/ecommerce-marketing-engine/pom.xml test
mvn -f examples/comprehensive/ecommerce-marketing-engine/v2-pattern/pom.xml test
```

全部代码兼容 JDK 1.8。示例的 `InMemoryPromotionCatalog` 仅模拟 MySQL 中的活动配置读取。生产实现还应考虑 DSL 语法/活动版本、规则复杂度限制、缓存淘汰、灰度发布、审计、金额舍入约定，以及规则发布和生效时间；这些不由四种设计模式自动解决。
