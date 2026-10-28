# 责任链模式：催收外呼申请规则链

## 专题来源与推进位置

根据《01-1｜设计模式专项》的实际学习记录，责任链在代理之后、装饰器之前。
本次补齐此前漏掉的责任链，不继续外观模式，也不同时实现观察者或状态。

已读取专题进度总结，其中明确记录“责任链：外呼规则链、Servlet Filter”；
并核对了用户关于 `ApplicationFilterChain#doFilter` 没有显式循环却能依次调用过滤器的提问。
对应详细讲解目前只返回内容引用，无法核对正文。因此本模块围绕已确认主题独立实现，
不宣称逐行复刻课程案例。具体业务规则与教学参数属于本仓库的简化设计。

## 场景与边界

虚构案件 DEMO-001 申请模拟外呼，依次经过两个检查：

1. Permission：是否有模拟案件操作权限。
2. ContactWindow：教学小时是否处于 `[9,18)`。

只有全部通过才到达 MockCallGateway，记录一次模拟申请。
不包含电话号码、不调用供应商、不发起真实外呼，也不操作资金。
9 至 18 点只是固定教学参数，不是监管要求，更不是完整的外呼合规方案。
权限结果与小时由测试构造，使行为可重复；生产权限必须由服务端可信系统判定，
不能相信前端传入的 permitted 标记。材料合规、真实触达权限等不在此模块范围内。

## 独立版本

| 版本 | 实现 | 要观察的差别 |
| --- | --- | --- |
| v1-naive | OutboundService 内直接写两个检查与提前返回 | 规则、顺序与短路控制都集中在业务服务 |
| v2-refactor | 提取 PermissionRule、ContactWindowRule | 规则职责独立，但服务仍负责每个分支和下一步调用 |
| v3-pattern | RuleHandler + 请求专用 RuleChain | 节点决定是否传递，服务只启动链，不认识具体规则 |
| v4-servlet-filter | 标准 Filter + Spring MVC + MockMvc | 用框架的 FilterChain 对照手写链的继续调用与短路 |

每个版本拥有自己的 domain、gateway 和测试；v1/v2/v3 使用 service，
v2 增加 rule，v3 使用 chain，v4 使用 filter、web、config。
没有跨版本或跨模式的业务代码依赖。

## 责任链的关键点

v3 中 RuleHandler 是节点契约，两个具体 Handler 分别负责一种检查。
RuleChain 保存节点列表与下一节点索引，服务每次 submit 都创建新的 RuleChain。
MockCallGateway 是所有节点放行后才到达的业务终点。

```java
if (!request.isPermitted()) {
    return "拒绝：无案件操作权限";
}
String result = chain.proceed(request);
return result;
```

**当前节点决定是否调用下一节点。** 拒绝时直接返回，不调用 proceed，就不会继续处理。
因此不能把“按顺序调用若干独立方法”都叫作责任链：v2 仍由服务集中编排，
v3 才把传递决定交给节点，并通过统一的链对象连接处理者。

本例是“所有规则必须通过”的过滤型责任链，不是“找到一个能处理请求的节点就结束”的查找型责任链。
这两种形式都可以体现责任链思想，不能机械认为责任链只能由一个节点处理。

### 为什么没有循环也会依次执行

RuleChain.proceed 每次只取一个节点，并在调用前将 nextIndex 加一。
当前节点通过时再次调用同一个 chain.proceed，就会读取下一个节点。

```text
PermissionHandler 进入并放行
  → RuleChain.proceed 读取下一节点
    → ContactWindowHandler 进入并放行
      → RuleChain.proceed 到达 MockCallGateway
    ← ContactWindowHandler 返回
← PermissionHandler 返回
```

这是嵌套调用形成的调用栈，不是后台并发，也不需要节点自己遍历整个集合。
若时间节点拒绝，仍会返回先前放行的权限节点，但不会执行终点。
日志中的“后续处理返回”只说明返回了结果，并不保证所有节点都通过。

### 装配与状态边界

```java
OutboundService service = new OutboundService(
        Arrays.<RuleHandler>asList(new PermissionHandler(), new ContactWindowHandler()),
        gateway);
```

顺序由列表决定，不依赖类名、扫描顺序或隐式排序。新增规则需要实现接口并调整装配，
不必修改 OutboundService 的规则分支，但并不意味着系统完全零修改。
每个节点放行时最多调用一次 proceed，不能重复调用或拒绝后继续调用。
RuleChain 有请求游标，必须每请求新建，不是可复用的共享单例；本例不支持异步、多次继续或跨线程调度。
MockCallGateway 的计数只为单线程测试观察，不是生产计数器。

## Servlet Filter 框架对照

v4 的两个过滤器实现 javax.servlet.Filter，而不是自定义同名接口。
放行调用 `chain.doFilter(request, response)`；拒绝写 HTTP 403 后 return，不调用链。
全部通过后进入 DispatcherServlet、OutboundController，最终到达模拟网关并返回 HTTP 200。

```java
MockMvc mvc = MockMvcBuilders.webAppContextSetup(context)
        .addFilters(permissionFilter, contactWindowFilter)
        .build();
```

Spring 管理过滤器、控制器和网关；测试用 addFilters 显式注册过滤器及其顺序。
仅把 Filter 声明为普通 Spring Bean，不等于已经把它注册到 Servlet 容器。
此处没有 Spring Boot 的自动注册行为，也没有启动 HTTP 端口。

MockMvc 会通过 Spring Test 的 MockFilterChain 调用这些真实 Filter 实例，
再进入测试 DispatcherServlet；并非直接调用 Controller，也并非运行 Tomcat 的 ApplicationFilterChain。
两者都能帮助理解“过滤器调用后续链”的契约，但不能把此测试当作 Tomcat 集成验证。

调试建议：

1. 先在 RuleChain.proceed、PermissionHandler.handle、ContactWindowHandler.handle 打断点，观察 nextIndex 和调用栈。
2. 再调试 PermissionFilter.doFilter、ContactWindowFilter.doFilter、MockFilterChain.doFilter。
3. 若后续单独启动 Tomcat，再对照 ApplicationFilterChain.doFilter/internalDoFilter 的过滤器位置推进逻辑。

本次选择 Servlet Filter + Spring MVC，是为了直接解释专题中的链式传递问题。
不需要数据库，因此不引入 MyBatis；也不为启动方便额外加入 Spring Boot。
沿用 Java 8 兼容的 Spring 5.3.39、Servlet API 4.0.1，仅用于教学，不作为生产版本推荐。

## 与相邻模式区别

- 装饰器强调保持接口并叠加职责；责任链强调按顺序处理、选择传递或短路。
  两者都可能出现嵌套调用，不能仅根据调用栈形状判定模式。
- 策略选择一种算法；这里多个规则按既定顺序参与同一次请求。
- 模板方法通常固定父类流程并允许子类扩展步骤；这里通过组合节点改变处理链。

收益是新增节点和调整顺序更独立；代价是调用路径更间接，顺序改变会影响拒绝原因，
节点漏调或重复调用后续链也会改变行为。规则很少且长期不变时，v1 的直接写法可能更易理解。
责任链本身不提供事务、幂等、合规保证或失败重试。

## 仅围绕模式的测试

四个版本各 3 个打印式测试，共 12 个，不扩展其他业务场景。

| 输入 | 期望路径 | 结果 | 终点调用次数 |
| --- | --- | --- | --- |
| permitted=true、hour=10 | 权限 → 时间 → 终点 | 模拟申请通过 | 1 |
| permitted=false、hour=20 | 权限拒绝；时间节点不执行 | 无案件操作权限 | 0 |
| permitted=true、hour=20 | 权限放行 → 时间拒绝 | 不在教学时间窗口 | 0 |

每个方法的中文 Javadoc 写明期望路径、结果与次数；v4 还标注 HTTP 200/403。
JUnit 仅发现并运行测试，没有断言框架、参数化用例或函数式回调。
执行成功不代表结果被自动判定，需对照日志和打印次数；业务拒绝是预期返回，不伪装成异常测试。

使用 JDK 1.8 和已安装的 Maven 3.9.x，在仓库根目录运行：

```bash
mvn test
mvn -f patterns/behavioral/chain-of-responsibility/pom.xml test
mvn -f patterns/behavioral/chain-of-responsibility/v3-pattern/pom.xml test
mvn -f patterns/behavioral/chain-of-responsibility/v4-servlet-filter/pom.xml test
```

v1、v2 同样可以通过各自 pom.xml 独立运行，不恢复已经清理的 Maven Wrapper 配置。
