package com.advance.patterns.structural.proxy.v2.proxy;

import com.advance.patterns.structural.proxy.v2.service.AssignmentService;
import com.advance.patterns.structural.proxy.v2.tx.LocalTransactionManager;

/**
 * 手写静态代理：实现同一接口，在目标方法前后处理事务；每新增方法都需维护代理代码。
 *
 * @author advance
 * @since 2026-10-28 10:15:29
 */
public final class StaticTransactionProxy implements AssignmentService {
    /** 被代理的业务对象。 */
    private final AssignmentService target;
    /** 本地事务管理器。 */
    private final LocalTransactionManager manager;

    /**
     * 组合目标对象和事务能力。
     * @param target 业务目标
     * @param manager 事务管理器
     * @throws IllegalArgumentException 依赖为空
     */
    public StaticTransactionProxy(AssignmentService target, LocalTransactionManager manager) {
        if (target == null || manager == null) {
            throw new IllegalArgumentException("代理依赖不能为空");
        }
        this.target = target;
        this.manager = manager;
    }

    /**
     * 在运行时异常或 Error 时回滚，保留原始异常。
     * @param caseId 案件编号
     * @param collectorId 复核组
     * @param failAfterUpdate 是否模拟失败
     * @throws IllegalArgumentException 目标输入非法
     * @throws IllegalStateException 业务或事务失败
     */
    @Override
    public void assign(String caseId, String collectorId, boolean failAfterUpdate) {
        System.out.println("[proxy/v2] StaticTransactionProxy.assign ---> 进入静态事务代理");
        LocalTransactionManager.Scope scope = manager.begin();
        try {
            target.assign(caseId, collectorId, failAfterUpdate);
        } catch (RuntimeException | Error exception) {
            try {
                manager.complete(scope, true);
            } catch (RuntimeException completionFailure) {
                exception.addSuppressed(completionFailure);
            }
            throw exception;
        }
        manager.complete(scope, false);
    }
}
