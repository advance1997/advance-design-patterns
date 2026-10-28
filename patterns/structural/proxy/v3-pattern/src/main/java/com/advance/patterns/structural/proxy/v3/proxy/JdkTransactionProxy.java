package com.advance.patterns.structural.proxy.v3.proxy;

import com.advance.patterns.structural.proxy.v3.service.AssignmentService;
import com.advance.patterns.structural.proxy.v3.tx.LocalTransactionManager;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 具名 InvocationHandler：JDK 生成接口代理，反射委托目标；本版增强全部业务接口方法。
 *
 * @author advance
 * @since 2026-10-28 10:15:29
 */
public final class JdkTransactionProxy implements InvocationHandler {
    /** 真实业务对象，不是代理对象。 */
    private final AssignmentService target;
    /** 本地事务管理器。 */
    private final LocalTransactionManager manager;

    /**
     * 保存依赖，只允许通过 create 构造。
     * @param target 目标
     * @param manager 事务管理器
     */
    private JdkTransactionProxy(AssignmentService target, LocalTransactionManager manager) {
        this.target = target;
        this.manager = manager;
    }

    /**
     * 生成 AssignmentService 接口代理，不生成目标类子类。
     * @param target 业务目标
     * @param manager 事务管理器
     * @return 运行时生成的接口代理
     * @throws IllegalArgumentException 依赖为空
     */
    public static AssignmentService create(AssignmentService target, LocalTransactionManager manager) {
        if (target == null || manager == null) {
            throw new IllegalArgumentException("代理依赖不能为空");
        }
        return (AssignmentService) Proxy.newProxyInstance(AssignmentService.class.getClassLoader(),
                new Class<?>[] {AssignmentService.class}, new JdkTransactionProxy(target, manager));
    }

    /**
     * 拦截接口调用；Object 方法使用代理自身身份，不创建事务。
     * @param proxy 代理实例
     * @param method 被调用的接口方法
     * @param args 调用参数，无参数时可为 null
     * @return 目标返回值
     * @throws Throwable 目标原始异常或事务异常，不泄露 InvocationTargetException 包装
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
            return "JdkTransactionProxy(" + target.getClass().getSimpleName() + ")";
        }
        System.out.println("[proxy/v3] JdkTransactionProxy.invoke ---> 拦截接口方法 " + method.getName());
        LocalTransactionManager.Scope scope = manager.begin();
        Object result = null;
        Throwable failure = null;
        try {
            result = method.invoke(target, args);
        } catch (InvocationTargetException exception) {
            failure = exception.getCause();
        } catch (Throwable exception) {
            failure = exception;
        }
        try {
            manager.complete(scope, failure instanceof RuntimeException || failure instanceof Error);
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
}
