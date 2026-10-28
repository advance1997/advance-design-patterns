package com.advance.patterns.structural.proxy.v5.tx;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 通过 Spring BeanPostProcessor 自动包装事务目标；不依赖 Spring 事务拦截器或事务管理器。
 *
 * @author advance
 * @since 2026-10-28 10:15:29
 */
public final class MiniTransactionAutoProxyCreator implements BeanPostProcessor, BeanFactoryAware {
    /** 只供基础设施获取事务管理器，不暴露给业务服务。 */
    private BeanFactory beanFactory;

    /**
     * 接收基础设施容器，避免在后处理器创建期间提前初始化业务 Bean。
     * @param beanFactory Spring 容器
     * @throws BeansException 容器回调失败
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * 对含实现方法事务注解的 Bean 生成 JDK 代理，其他 Bean 原样返回。
     * @param bean 已初始化的原始 Bean
     * @param beanName Bean 名称
     * @return 接口代理或原 Bean
     * @throws BeansException 获取基础设施失败
     * @throws IllegalArgumentException 带注解方法没有接口入口
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        boolean transactional = false;
        Class<?>[] interfaces = bean.getClass().getInterfaces();
        for (Method method : bean.getClass().getMethods()) {
            if (method.getAnnotation(MiniTransactional.class) != null) {
                transactional = true;
                boolean exposed = false;
                for (Class<?> contract : interfaces) {
                    try {
                        contract.getMethod(method.getName(), method.getParameterTypes());
                        exposed = true;
                    } catch (NoSuchMethodException ignored) {
                        // 当前接口没有此方法，继续检查其他接口。
                    }
                }
                if (!exposed) {
                    throw new IllegalArgumentException("MiniTransactional 方法必须通过接口暴露：" + method.getName());
                }
            }
        }
        if (!transactional) {
            return bean;
        }
        LocalTransactionManager manager = beanFactory.getBean(LocalTransactionManager.class);
        System.out.println("[proxy/v5] MiniTransactionAutoProxyCreator ---> 自动创建 JDK 事务代理，beanName=" + beanName);
        return Proxy.newProxyInstance(bean.getClass().getClassLoader(), interfaces,
                new MiniTransactionInterceptor(bean, manager));
    }
}
