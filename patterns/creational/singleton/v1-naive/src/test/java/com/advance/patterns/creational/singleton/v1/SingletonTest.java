package com.advance.patterns.creational.singleton.v1;

import com.advance.patterns.creational.singleton.v1.domain.EagerRuleBook;
import com.advance.patterns.creational.singleton.v1.domain.UnsafeRuleBook;
import org.junit.jupiter.api.Test;

/**
 * 观察单例引用与共享状态是两个不同问题；不依赖概率性的竞态重现。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public class SingletonTest {
    /**
     * testSharedStateLeak：直接调用观察业务边界。
     * <p>期望结果：顺序获取引用相同为 true；第二次设置后，第一次引用读取到 DEMO-002，说明状态串用。</p>
     */
    @Test
    public void testSharedStateLeak() {
        System.out.println("\ntestSharedStateLeak ---> 期望：顺序获取引用相同为 true；第二次设置后，第一次引用读取到 DEMO-002，说明状态串用");
        UnsafeRuleBook first = UnsafeRuleBook.getInstance();
        UnsafeRuleBook second = UnsafeRuleBook.getInstance();
        first.beginReview("DEMO-001");
        second.beginReview("DEMO-002");
        System.out.println("引用相同：" + (first == second));
        System.out.println("第一次调用方读到的案件：" + first.getCurrentCaseId());
    }

    /**
     * testEagerIdentity：直接调用观察业务边界。
     * <p>期望结果：两次饿汉式获取引用相同为 true。</p>
     */
    @Test
    public void testEagerIdentity() {
        System.out.println("\ntestEagerIdentity ---> 期望：两次饿汉式获取引用相同为 true");
        System.out.println("饿汉式引用相同：" + (EagerRuleBook.getInstance() == EagerRuleBook.getInstance()));
    }

    /**
     * testBlankCase：直接调用观察业务边界。
     * <p>期望结果：空案件编号抛出 IllegalArgumentException。</p>
     */
    @Test
    public void testBlankCase() {
        System.out.println("\ntestBlankCase ---> 期望：空案件编号抛出 IllegalArgumentException");
        try {
            UnsafeRuleBook.getInstance().beginReview(" ");
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

}
