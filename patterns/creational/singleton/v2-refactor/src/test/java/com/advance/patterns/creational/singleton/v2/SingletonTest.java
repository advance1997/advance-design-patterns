package com.advance.patterns.creational.singleton.v2;

import com.advance.patterns.creational.singleton.v2.domain.SynchronizedRuleBook;
import org.junit.jupiter.api.Test;

/**
 * 同步懒加载的直观引用与规则值对照。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public class SingletonTest {
    /**
     * testIdentity：直接调用观察业务边界。
     * <p>期望结果：两次获取引用相同为 true，批次上限 100；不保存案件信息。</p>
     */
    @Test
    public void testIdentity() {
        System.out.println("\ntestIdentity ---> 期望：两次获取引用相同为 true，批次上限 100；不保存案件信息");
        SynchronizedRuleBook first = SynchronizedRuleBook.getInstance();
        SynchronizedRuleBook second = SynchronizedRuleBook.getInstance();
        System.out.println("引用相同：" + (first == second));
        System.out.println("教学批次上限：" + first.getMaxBatchSize());
    }

}
