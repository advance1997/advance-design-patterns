# 工厂方法模式实践

## 业务场景

物流系统需要根据运输方式安排订单配送。目前支持公路运输和海运，未来还可能增加空运、铁路等方式。每种运输方式负责创建自己的运输工具，并返回统一的配送结果。

## 版本演进

| 版本 | 创建方式 | 主要特征 |
| --- | --- | --- |
| V1 | 业务服务直接 `new` 产品 | 简单直观，但服务同时负责业务流程和对象创建；新增类型必须修改服务。 |
| V2 | 简单工厂集中创建产品 | 客户端不再了解具体产品，但工厂中的分支仍会随着产品类型增加而修改。 |
| V3 | 工厂方法由具体 Creator 创建产品 | 稳定流程位于抽象 Creator 中；新增运输方式通过增加 Creator/Product 扩展。 |

V2 是演进过程中的简单工厂，并不是 GoF 工厂方法模式；V3 才是本次实践的目标结构。抽象工厂解决产品族问题，将作为单独模式实践。

## 角色映射

V3 中的模式角色：

- Creator：`Logistics`
- Concrete Creator：`RoadLogistics`、`SeaLogistics`
- Product：`Transport`
- Concrete Product：`TruckTransport`、`ShipTransport`
- Creator 中的稳定业务流程：`Logistics#planDelivery`
- Factory Method：`Logistics#createTransport`

## 测试

### 异常为什么没有显示为测试失败？

原实现已经会对空字段和空请求抛异常，但 JUnit 的 `assertThrows` 会捕获预期异常，
因此测试通过不等于没有抛异常。正常请求没有失败条件时，模拟运输产品会返回成功。

各版本的 ExceptionPathTest 现覆盖空单号、空目的地、空请求和直接调用产品时的空参数；
V1/V2 还检查空运输方式。V3 的 FailurePropagationTest 用测试替身模拟工厂创建失败、
产品执行失败和工厂返回 null，并验证异常原样传出、没有自动重试。
这些模拟只存在于测试中，不引入特殊订单号或随机失败逻辑。

关键路径会输出版本、入口、产品和失败原因，例如：

```text
[factory-method/v3] Logistics.planDelivery ---> start
[factory-method/v3] SeaLogistics ---> createTransport
[factory-method/v3] SHIP ---> deliver ---> start
```

异常测试保留断言并输出失败路径；如需在 IDE 中停在真正的抛出位置，
启用 Java 异常断点并勾选 caught（已捕获），即可观察被 assertThrows 捕获的异常。

从仓库根目录运行全部版本：

```bash
./mvnw -f patterns/creational/factory-method/pom.xml test
```

单独运行 V3：

```bash
./mvnw -f patterns/creational/factory-method/v3-pattern/pom.xml test
```
