package com.advance.patterns.behavioral.state.v4.service;

import com.advance.patterns.behavioral.state.v4.domain.InvalidTransitionException;
import com.advance.patterns.behavioral.state.v4.domain.OutboxRecord;
import com.advance.patterns.behavioral.state.v4.domain.PlanNotFoundException;
import com.advance.patterns.behavioral.state.v4.domain.SettlementEvent;
import com.advance.patterns.behavioral.state.v4.domain.SettlementRecord;
import com.advance.patterns.behavioral.state.v4.domain.SettlementStatus;
import com.advance.patterns.behavioral.state.v4.domain.StateConflictException;
import com.advance.patterns.behavioral.state.v4.mapper.OutboxMapper;
import com.advance.patterns.behavioral.state.v4.mapper.SettlementMapper;
import com.advance.patterns.behavioral.state.v4.state.StateHandlerRegistry;
import com.advance.patterns.behavioral.state.v4.domain.TransitionResult;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 事务边界位于业务服务，状态处理器只计算规则，数据库更新与 Outbox 插入必须一起成功。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
@Service
public class SettlementService {

    /** 当前状态行为注册表。 */
    private final StateHandlerRegistry registry;

    /** 方案数据库访问入口。 */
    private final SettlementMapper plans;

    /** 与方案共用数据源的事件访问入口。 */
    private final OutboxMapper outbox;

    /**
     * 注入无状态规则和两个 MyBatis 映射接口。
     *
     * @param registry 状态注册表
     * @param plans 方案映射接口
     * @param outbox 事件映射接口
     */
    public SettlementService(StateHandlerRegistry registry, SettlementMapper plans, OutboxMapper outbox) {
        this.registry = registry;
        this.plans = plans;
        this.outbox = outbox;
    }

    /**
     * 读取虚构方案。
     *
     * @param id 方案编号
     * @return 当前数据库状态
     * @throws PlanNotFoundException 方案不存在时抛出
     */
    public SettlementRecord find(long id) {
        SettlementRecord plan = plans.find(id);
        if (plan == null) {
            throw new PlanNotFoundException("和解方案不存在：" + id);
        }
        return plan;
    }

    /**
     * 验证事件与版本，完成原子状态迁移及 Outbox 入库。
     *
     * @param id 方案编号
     * @param event 业务事件
     * @param expectedVersion 客户端预期旧版本
     * @return 新状态、版本和待投递事件编号
     * @throws IllegalArgumentException 参数缺失或无效时抛出
     * @throws InvalidTransitionException 当前状态拒绝事件时抛出
     * @throws StateConflictException 版本过期或并发条件更新失败时抛出
     */
    @Transactional
    public TransitionResult handle(long id, SettlementEvent event, Long expectedVersion) {
        if (id <= 0 || event == null || expectedVersion == null || expectedVersion < 0) {
            throw new IllegalArgumentException("id 必须为正数，event 必填，expectedVersion 必须显式提供且不小于 0");
        }
        SettlementRecord plan = find(id);
        if (plan.getVersion() != expectedVersion.longValue()) {
            throw new StateConflictException("方案版本已变化，请重新读取当前状态；不自动重试业务事件");
        }
        System.out.println("[状态/v4] SettlementService.handle ---> 读取当前 status="
                + plan.getStatus() + "，version=" + plan.getVersion());
        SettlementStatus after = registry.get(plan.getStatus()).onEvent(event);
        int changed = plans.change(id, plan.getStatus(), after, expectedVersion);
        if (changed != 1) {
            throw new StateConflictException("方案状态或版本已被其他操作修改，本次迁移未生效");
        }
        System.out.println("[状态/v4] SettlementService.handle ---> 条件更新成功，在同一事务写入 Outbox");
        OutboxRecord record = new OutboxRecord();
        record.setEventId(UUID.randomUUID().toString());
        record.setPlanId(id);
        record.setPlanVersion(expectedVersion + 1);
        record.setEvent(event);
        record.setFromStatus(plan.getStatus());
        record.setToStatus(after);
        outbox.insert(record);
        TransitionResult result = new TransitionResult();
        result.setPlanId(id);
        result.setStatus(after);
        result.setVersion(record.getPlanVersion());
        result.setEventId(record.getEventId());
        System.out.println("[状态/v4] SettlementService.handle ---> 事务方法结束，提交由 Spring 代理执行");
        return result;
    }
}
