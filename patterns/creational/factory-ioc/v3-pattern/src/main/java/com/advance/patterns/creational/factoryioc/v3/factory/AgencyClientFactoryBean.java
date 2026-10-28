package com.advance.patterns.creational.factoryioc.v3.factory;

import com.advance.patterns.creational.factoryioc.v3.client.MockAgencyClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 受容器管理的 FactoryBean，负责产品创建并显式承担产品关闭责任。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public final class AgencyClientFactoryBean
        implements FactoryBean<MockAgencyClient>, InitializingBean, DisposableBean {
    /** 延迟创建的单例产品；访问由同步方法保护，不代表产品方法线程安全。 */
    private MockAgencyClient client;
    /** 工厂销毁后不允许重新创建产品。 */
    private boolean destroyed;

    /**
     * 容器初始化工厂，不提前创建产品。
     */
    @Override
    public void afterPropertiesSet() {
        System.out.println("[factory-ioc/v3] AgencyClientFactoryBean.afterPropertiesSet ---> 工厂初始化");
    }

    /**
     * 延迟构造并初始化一个产品；重复调用返回同一实例。
     * @return 已打开的模拟客户端
     * @throws IllegalStateException 工厂已销毁
     */
    @Override
    public synchronized MockAgencyClient getObject() {
        if (destroyed) {
            throw new IllegalStateException("工厂已销毁，不能获取产品");
        }
        if (client == null) {
            System.out.println("[factory-ioc/v3] AgencyClientFactoryBean.getObject ---> 首次创建产品");
            client = new MockAgencyClient();
            client.open();
        }
        return client;
    }

    /**
     * 提供产品类型，容器不必为类型判断提前创建产品。
     * @return MockAgencyClient 类型
     */
    @Override
    public Class<?> getObjectType() {
        return MockAgencyClient.class;
    }

    /**
     * 声明产品在本工厂中共享；不是声明工厂 Bean 的作用域。
     * @return true
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

    /**
     * 显式关闭产品；Spring 不会自动对 FactoryBean 产品调用销毁回调。
     */
    @Override
    public synchronized void destroy() {
        destroyed = true;
        System.out.println("[factory-ioc/v3] AgencyClientFactoryBean.destroy ---> 销毁工厂并关闭已创建产品");
        if (client != null) {
            client.close();
        }
    }
}
