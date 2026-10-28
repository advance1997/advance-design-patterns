package com.advance.patterns.behavioral.state.v4;

import com.advance.patterns.behavioral.state.v4.domain.SettlementRecord;
import com.advance.patterns.behavioral.state.v4.domain.SettlementStatus;
import com.advance.patterns.behavioral.state.v4.mapper.SettlementMapper;
import com.advance.patterns.behavioral.state.v4.outbox.MockEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * 启动真实 Boot 自动配置、MyBatis、H2 与事务代理，通过 MockMvc 打印接口和数据库结果；不启动网络端口。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
@SpringBootTest(classes = SettlementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class SettlementBootTest {

    /** Boot 创建的完整 MVC 上下文。 */
    @Autowired
    private WebApplicationContext context;

    /** 与应用事务共用数据源，仅用于测试准备与结果读取。 */
    @Autowired
    private JdbcTemplate jdbc;

    /** 用于单独观察乐观锁 SQL 的实际行数。 */
    @Autowired
    private SettlementMapper mapper;

    /** 模拟投递终点，不连接真实 MQ。 */
    @Autowired
    private MockEventPublisher publisher;

    /** 每个测试重新创建的模拟 HTTP 入口。 */
    private MockMvc mvc;

    /**
     * 每个测试重新准备两个虚构草稿；测试方法自身不加事务，才能观察服务真实提交与回滚。
     */
    @BeforeEach
    void prepare() {
        jdbc.update("DELETE FROM settlement_outbox");
        jdbc.update("DELETE FROM settlement_plan");
        jdbc.update("INSERT INTO settlement_plan(id, status, version) VALUES (10001, 'DRAFT', 0)");
        jdbc.update("INSERT INTO settlement_plan(id, status, version) VALUES (10002, 'DRAFT', 0)");
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    /**
     * 输入 10001 的完整四步事件与版本 0、1、2、3。
     * 期望每步 HTTP 200，最终 COMPLETED/version=4，NEW 事件为 4 条。
     * 手动投递后本次模拟发送 4 次、SENT 为 4；再投递返回 0，不重复选择 SENT。
     *
     * @throws Exception 模拟请求执行失败时交给 JUnit 报告
     */
    @Test
    void completeLifecycleAndDispatch() throws Exception {
        send("SUBMIT", 0);
        send("APPROVE", 1);
        send("LEGAL_PASS", 2);
        send("CONFIRM_COMPLETION", 3);
        printDatabase();
        int before = publisher.getSentCount();
        MvcResult first = mvc.perform(MockMvcRequestBuilders.post("/demo/outbox/dispatch")).andReturn();
        System.out.println("首次投递 HTTP 状态：" + first.getResponse().getStatus()
                + "，标记数量：" + first.getResponse().getContentAsString());
        System.out.println("本次模拟发送次数：" + (publisher.getSentCount() - before));
        MvcResult second = mvc.perform(MockMvcRequestBuilders.post("/demo/outbox/dispatch")).andReturn();
        System.out.println("再次投递数量：" + second.getResponse().getContentAsString());
        printDatabase();
    }

    /**
     * 输入草稿直接 APPROVE，期望 HTTP 409、DRAFT/version=0、事件为 0。
     * 随后 SUBMIT、REJECT 各 HTTP 200，再次 SUBMIT 为 409；最终 REJECTED/version=2、事件为 2。
     *
     * @throws Exception 模拟请求执行失败时交给 JUnit 报告
     */
    @Test
    void rejectIllegalAndTerminalEvents() throws Exception {
        send("APPROVE", 0);
        printDatabase();
        send("SUBMIT", 0);
        send("REJECT", 1);
        send("SUBMIT", 2);
        printDatabase();
    }

    /**
     * 输入 SUBMIT、APPROVE、REJECT，版本依次为 0、1、2。
     * 期望全部 HTTP 200，法核状态允许驳回；最终 REJECTED/version=3、事件为 3。
     *
     * @throws Exception 模拟请求执行失败时交给 JUnit 报告
     */
    @Test
    void rejectDuringLegalReview() throws Exception {
        send("SUBMIT", 0);
        send("APPROVE", 1);
        send("REJECT", 2);
        printDatabase();
    }

    /**
     * 对 10001 先成功 SUBMIT/version=0，再以旧版本 0 请求 APPROVE。
     * 期望第二次 HTTP 409，保持 PENDING_APPROVAL/version=1、事件为 1。
     * 对独立方案 10002 模拟两个调用方先后读到同一快照，再分别执行同一条件 SQL。
     * 期望更新行数依次 1、0；这是数据库乐观锁验证，不是额外业务入口，也不是线程压力测试。
     *
     * @throws Exception 模拟请求执行失败时交给 JUnit 报告
     */
    @Test
    void preventStaleVersionOverwrite() throws Exception {
        send("SUBMIT", 0);
        send("APPROVE", 0);
        printDatabase();
        SettlementRecord first = mapper.find(10002L);
        SettlementRecord stale = mapper.find(10002L);
        int winner = mapper.change(10002L, first.getStatus(), SettlementStatus.PENDING_APPROVAL, first.getVersion());
        int loser = mapper.change(10002L, stale.getStatus(), SettlementStatus.PENDING_APPROVAL, stale.getVersion());
        System.out.println("相同快照的两次条件更新行数：" + winner + "、" + loser);
        System.out.println("10002 最终数据库状态：" + jdbc.queryForMap(
                "SELECT status, version FROM settlement_plan WHERE id = 10002"));
    }

    /**
     * 故意预放一条 10001/version=1 的事件作为唯一键冲突故障，再提交 SUBMIT/version=0。
     * 期望 HTTP 409；方案 UPDATE 虽已执行，但后续 INSERT 失败导致事务回滚到 DRAFT/version=0。
     * Outbox 只有原先故障夹具的 1 条记录，没有新增本次事件。该夹具不代表合法业务数据。
     *
     * @throws Exception 模拟请求执行失败时交给 JUnit 报告
     */
    @Test
    void rollbackWhenOutboxInsertFails() throws Exception {
        jdbc.update("INSERT INTO settlement_outbox "
                + "(event_id, plan_id, plan_version, event, from_status, to_status, delivery_status) "
                + "VALUES ('FAULT-FIXTURE', 10001, 1, 'SUBMIT', 'DRAFT', 'PENDING_APPROVAL', 'SENT')");
        send("SUBMIT", 0);
        printDatabase();
    }

    /**
     * 通过真实 MVC 调度发送指定事件并打印响应，不包装回调或执行断言。
     *
     * @param event 合法枚举文本
     * @param version 客户端预期版本
     * @throws Exception 请求执行失败时抛出
     */
    private void send(String event, long version) throws Exception {
        String json = "{\"event\":\"" + event + "\",\"expectedVersion\":" + version + "}";
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/settlements/10001/events")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn();
        System.out.println("事件 " + event + "，实际 HTTP 状态：" + result.getResponse().getStatus()
                + "，响应：" + result.getResponse().getContentAsString(java.nio.charset.StandardCharsets.UTF_8));
    }

    /**
     * 通过事务方法返回后的独立查询打印方案与事件快照，观察提交或回滚后的实际数据。
     */
    private void printDatabase() {
        System.out.println("方案实际结果：" + jdbc.queryForMap(
                "SELECT status, version FROM settlement_plan WHERE id = 10001"));
        System.out.println("Outbox 实际结果：" + jdbc.queryForList(
                "SELECT plan_version, event, from_status, to_status, delivery_status "
                        + "FROM settlement_outbox WHERE plan_id = 10001 ORDER BY plan_version"));
    }
}
