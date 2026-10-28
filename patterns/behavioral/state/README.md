# 状态模式：和解方案审批、法核与履约

## 来源与授权范围

本次按已确认专题学习顺序补齐状态模式，不同时推进其他设计模式。
由于《01-1｜设计模式专项》的早期状态方案正文当前无法读取，用户已明确授权：
“用和解方案状态模式及 Spring Boot 落地方案开始实施”。
以下状态表是本次独立设计的教学规则，不宣称复刻原文或代表实际金融机构审批流程。

只使用虚构方案编号，不包含真实客户、金额、借据、资金划扣或真实履约认定。
CONFIRM_COMPLETION 是人工模拟事件，不是支付回调或真实债务结清操作。
审批权限、审批额度、材料校验、法律效力与履约核验均需要真实业务另行定义。

## 先确定状态与事件

| 当前状态 | 允许的事件 | 下一状态 |
| --- | --- | --- |
| DRAFT 草稿 | SUBMIT 提交审批 | PENDING_APPROVAL |
| PENDING_APPROVAL 待审批 | APPROVE 审批通过 | PENDING_LEGAL |
| PENDING_APPROVAL 待审批 | REJECT 驳回 | REJECTED |
| PENDING_LEGAL 待法核 | LEGAL_PASS 法核通过 | PERFORMING |
| PENDING_LEGAL 待法核 | REJECT 驳回 | REJECTED |
| PERFORMING 履约中 | CONFIRM_COMPLETION 模拟确认完成 | COMPLETED |
| COMPLETED / REJECTED 终态 | 无 | 拒绝全部后续事件 |

没有出现在表中的状态与事件组合，一律拒绝并保持原状态。
本例刻意不加入撤回、重提、逾期、违约、部分履约等分支，防止业务细节掩盖模式。
例如草稿不能直接审批；已驳回不能再次提交；履约中不能使用审批驳回事件。

```text
DRAFT --SUBMIT--> PENDING_APPROVAL --APPROVE--> PENDING_LEGAL
                     │ REJECT                       │ LEGAL_PASS
                     ▼                              ▼
                  REJECTED                      PERFORMING
                     ▲                              │ CONFIRM_COMPLETION
                     └──── REJECT ─ PENDING_LEGAL    ▼
                                                 COMPLETED
```

## 四个独立版本

| 版本 | 实现 | 关键变化 |
| --- | --- | --- |
| v1-naive | 服务中集中判断全部状态和事件 | 状态增加导致同一方法不断膨胀 |
| v2-refactor | 迁移规则提取到 TransitionRules | 职责拆开，但状态分支仍集中 |
| v3-pattern | 每种状态独立实现 SettlementState | 当前状态决定事件行为，服务无需知道各状态分支 |
| v4-spring-boot | Boot + MVC + MyBatis + H2 + 事务 Outbox | 通过真实 HTTP 调度、事务代理与 SQL 落地迁移 |

各版本分别持有自己的 domain、service、测试；v2 增加 policy，v3/v4 增加 state。
v4 额外使用 web、mapper、outbox 和独立 resources，不导入其他版本的业务类型。
父项目仍只管理依赖与插件，不提供业务代码。

## 模式在哪里，而不是只看框架

- 上下文：方案当前 status；v3 的 SettlementService 负责按当前状态委托并更新方案。
- 状态接口：SettlementState，提供 supports 与 onEvent。
- 具体状态：DraftState、PendingApprovalState、PendingLegalState、PerformingState、CompletedState、RejectedState。
- 查找组件：StateHandlerRegistry，根据当前状态找到具体行为对象；注册表本身不是状态模式。

```java
SettlementStatus before = plan.getStatus();
SettlementStatus after = registry.get(before).onEvent(event);
plan.changeStatus(after);
```

关键是“当前状态决定行为”。同一 REJECT 事件在待审批、待法核时允许，在草稿或终态时拒绝。
客户端提交业务事件，不直接传入下一状态；状态处理器的返回值才是合法目标。
v3 的模型方法用于纯 Java 学习，不能当作安全访问边界；v4 的数据库写入口由服务事务控制。

本例具体状态对象无请求状态，可由 Spring 单例复用。不能把某个方案的状态、版本保存在共享 Handler 字段里。
新增状态需要增加具体实现、枚举值并装配，注册表会检查重复和缺失；不是声称扩展时“完全零修改”。

### 与策略、责任链的区别

策略通常由外部选择算法；这里由方案自己的当前状态决定允许的行为，事件还会改变后续行为。
责任链让多个节点依次处理或短路；这里每个事件只委托当前状态的一个处理器，不遍历所有状态。
没有使用继承模板或复杂状态机库，便于直接观察状态对象的多态。
如果只有少量长期稳定的状态，集中判断也可能足够；状态模式的代价是增加处理器类和装配关系。

## Spring Boot 落地调用路径

```text
POST /settlements/{id}/events
  → SettlementController：读取 event、expectedVersion
  → Spring 事务代理
    → SettlementService：读取方案、检查旧版本
    → StateHandlerRegistry：按数据库当前状态选择处理器
    → 具体 SettlementState：决定是否允许并返回目标状态
    → SettlementMapper：带状态和版本条件的 UPDATE
    → OutboxMapper：同一事务 INSERT 待投递事件
  ← 两项写入提交后返回新状态、版本、eventId
```

使用 Spring Boot 2.7.18、Spring Framework 5.3.39、MyBatis Starter 2.3.2（MyBatis 3.5.14）、H2 2.2.224。
这些版本用于保持 JDK 1.8 教学兼容，不是生产安全选型建议。
Boot BOM 只在 v4 生效，并显式保持项目统一 Spring/JUnit 版本，不改变其他模式依赖。
使用 Boot 自动配置，不是手工 new 一个服务后声称完成 Spring Boot 集成。

### 乐观锁保护什么

```sql
UPDATE settlement_plan
SET status = :after, version = version + 1
WHERE id = :id AND status = :before AND version = :expectedVersion;
```

服务先检查请求版本，提供易理解的冲突说明；最终数据库条件 UPDATE 防止读取后又被其他请求改动。
更新行数必须为 1，0 表示旧状态或旧版本已经失效。不能无条件更新，也不能忽略行数。
发生冲突返回 HTTP 409，不自动重试原业务事件；调用方应重新读取并让业务决定下一步。
expectedVersion 不是幂等键。客户端丢失响应后重发可能得到冲突，不提供原响应重放能力。

### 为什么要有同事务 Outbox

SettlementService.handle 的 @Transactional 由 HTTP 入口调用时经过 Spring 代理。
方案 UPDATE 与 Outbox INSERT 使用同一数据源、同一事务；任一步抛出运行时异常，整体回滚。
状态处理器只负责规则，不发送消息、不接收 Mapper、不自行开启事务。
Outbox 以 eventId 为主键，并通过 `(plan_id, plan_version)` 唯一约束保证一个迁移版本只对应一条事件。
该约束不是业务请求的完整幂等方案。

Outbox 记录包含 event、fromStatus、toStatus、planVersion，成功提交后初始标记为 NEW。
本地手动调用 `/demo/outbox/dispatch` 后，单工作者依次模拟发送并标记 SENT。
这是独立的后续动作，不在状态迁移事务内提前发送。

**边界必须分清：**

- 本例真正验证的是数据库状态与待投递记录的事务原子性。
- 模拟发布者只打印，不连接 MQ；不是完整的生产消息系统。
- 发送成功但标记前发生故障，重试可能重复发送，需要消费者幂等。
- 多工作者抢占、定时重试、死信、消息顺序和监控均未实现，手动接口只支持单工作者演示。
- H2 是内存库，应用重启即丢失数据；这不代表真实持久化 Outbox 的宕机恢复能力。
- 手动投递接口没有认证，仅默认绑定 127.0.0.1，不得直接作为生产管理接口发布。

## 精简测试范围

共 14 个打印式测试：v1/v2/v3 各 3 个，v4 为 5 个。

| 范围 | 观察内容 |
| --- | --- |
| 三个基础版本 | 正常四步流转、审批与法核驳回、非法事件与终态拒绝 |
| Boot 正常流程 | HTTP 四步流转、版本递增、4 条 NEW 事件、手动投递转 SENT |
| Boot 非法与驳回 | HTTP 409 不改状态、不增事件；合法驳回到终态 |
| Boot 乐观锁 | 过期请求拒绝；两个相同数据库快照的条件更新行数为 1、0 |
| Boot 原子性 | 故意制造 Outbox 唯一键冲突，观察已执行的 UPDATE 回滚 |

乐观锁测试使用可重复的两个旧快照验证 SQL 条件，不是并发压力测试。
回滚测试预插一条冲突事件作为故障夹具，最终仍存在该 1 条旧记录，但没有新增本次事件；
夹具故意与草稿状态不一致，不代表正常业务数据。
Boot 测试使用完整自动配置与真实 H2/MyBatis，通过 MockMvc 运行 MVC 调度，不启动网络监听。
测试自身不加 @Transactional，以便读取服务实际提交或回滚后的结果。

每个测试上方有中文期望值，直接打印状态、异常、HTTP 响应和数据库结果，不使用断言框架。
JUnit 成功只说明执行完成，仍需对照输出；不扩展权限系统、支付、贷款计算等无关测试。

## 运行与接口操作

在仓库根目录设置 JAVA_HOME 为本机 JDK 1.8，使用已安装的 Maven 3.9.x：

```bash
mvn test
mvn -f patterns/behavioral/state/pom.xml test
mvn -f patterns/behavioral/state/v3-pattern/pom.xml test
mvn -f patterns/behavioral/state/v4-spring-boot/pom.xml test
mvn -f patterns/behavioral/state/v4-spring-boot/pom.xml package
java -jar patterns/behavioral/state/v4-spring-boot/target/state-v4-spring-boot-1.0.0-SNAPSHOT.jar
```

应用默认监听 `127.0.0.1:8080`，首次启动初始化虚构方案 10001，状态 DRAFT、版本 0。
可通过 `--server.port=8081` 更换端口；重启后数据库重新初始化。

```bash
curl http://127.0.0.1:8080/settlements/10001

curl -X POST http://127.0.0.1:8080/settlements/10001/events \
  -H 'Content-Type: application/json' \
  -d '{"event":"SUBMIT","expectedVersion":0}'

curl -X POST http://127.0.0.1:8080/settlements/10001/events \
  -H 'Content-Type: application/json' \
  -d '{"event":"APPROVE","expectedVersion":1}'

curl -X POST http://127.0.0.1:8080/settlements/10001/events \
  -H 'Content-Type: application/json' \
  -d '{"event":"LEGAL_PASS","expectedVersion":2}'

curl -X POST http://127.0.0.1:8080/settlements/10001/events \
  -H 'Content-Type: application/json' \
  -d '{"event":"CONFIRM_COMPLETION","expectedVersion":3}'

curl -X POST http://127.0.0.1:8080/demo/outbox/dispatch
```

正常迁移返回 HTTP 200、status、version、eventId；非法状态事件或过期版本返回 409，
缺失方案返回 404，缺少必要参数或未知事件文本返回 400。
所有接口都是教学接口，不提供真实审批身份校验、前端页面或生产运维能力。

建议先调试 v3 的 `registry.get(plan.getStatus()).onEvent(event)`，
再调试 v4 的同一规则如何进入事务、MyBatis 条件更新和 Outbox INSERT。
