package com.advance.patterns.structural.proxy.v5.tx;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 迷你事务拦截器：解析实现方法注解、建立事务、反射调用、按异常规则完成事务。
 *
 * @author advance
 * @since 2026-10-28 10:15:29
 */
public final class MiniTransactionInterceptor implements InvocationHandler {
    /** 原始业务目标，避免对代理自身反射调用导致递归。 */
    private final Object target;
    /** 本地事务管理器。 */
    private final LocalTransactionManager manager;

    /**
     * 保存代理依赖。
     * @param target 原始目标
     * @param manager 本地事务管理器
     * @throws IllegalArgumentException 依赖为空
     */
    public MiniTransactionInterceptor(Object target, LocalTransactionManager manager) {
        if (target == null || manager == null) {
            throw new IllegalArgumentException("拦截器依赖不能为空");
        }
        this.target = target;
        this.manager = manager;
    }

    /**
     * 实现类注解决定是否启用事务；未注解方法直接委托，自调用不重新经过本方法。
     * @param proxy JDK 代理
     * @param method 接口方法
     * @param args 调用参数
     * @return 目标返回值
     * @throws Throwable 解包后的原始异常或事务完成异常
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            if ("equals".equals(method.getName())) {
                return proxy == args[0];
            }
            if ("hashCode".equals(method.getName())) {
                return System.identityHashCode(proxy);
            }
            return "MiniTransactionProxy(" + target.getClass().getSimpleName() + ")";
        }
        Method implementation = target.getClass().getMethod(method.getName(), method.getParameterTypes());
        MiniTransactional annotation = implementation.getAnnotation(MiniTransactional.class);
        if (annotation == null) {
            return invokeTarget(implementation, args);
        }
        System.out.println("[proxy/v5] MiniTransactionInterceptor.invoke ---> 解析事务注解，method=" + method.getName());
        LocalTransactionManager.Scope scope = manager.begin();
        Object result = null;
        Throwable failure = null;
        try {
            result = invokeTarget(implementation, args);
        } catch (Throwable exception) {
            failure = exception;
        }
        try {
            manager.complete(scope, shouldRollback(failure, annotation));
        } catch (Throwable completionFailure) {
            if (failure == null) {
                failure = completionFailure;
            } else {
                failure.addSuppressed(completionFailure);
            }
        }
        if (failure != null) {
            throw failure;
        }
        return result;
    }

    /**
     * 对真实目标调用并去除反射异常包装。
     * @param method 目标实现方法
     * @param args 调用参数
     * @return 目标结果
     * @throws Throwable 原始目标异常或反射访问失败
     */
    private Object invokeTarget(Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(target, args);
        } catch (InvocationTargetException exception) {
            throw exception.getCause();
        }
    }

    /**
     * 判断默认或显式回滚规则，不支持 noRollbackFor 或异常名称匹配。
     * @param failure 目标异常，无异常时为 null
     * @param annotation 方法注解
     * @return 是否要求回滚
     */
    private boolean shouldRollback(Throwable failure, MiniTransactional annotation) {
        if (failure == null) {
            return false;
        }
        if (failure instanceof RuntimeException || failure instanceof Error) {
            return true;
        }
        for (Class<? extends Throwable> type : annotation.rollbackFor()) {
            if (type.isInstance(failure)) {
                return true;
            }
        }
        return false;
    }
}
