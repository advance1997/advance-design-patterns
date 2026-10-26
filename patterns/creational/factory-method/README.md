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

从仓库根目录运行全部版本：

```bash
./mvnw -f patterns/creational/factory-method/pom.xml test
```

单独运行 V3：

```bash
./mvnw -f patterns/creational/factory-method/v3-pattern/pom.xml test
```
