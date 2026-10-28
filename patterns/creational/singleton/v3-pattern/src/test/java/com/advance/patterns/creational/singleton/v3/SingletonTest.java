package com.advance.patterns.creational.singleton.v3;

import com.advance.patterns.creational.singleton.v3.config.RuleConfig;
import com.advance.patterns.creational.singleton.v3.domain.ContextRuleBook;
import com.advance.patterns.creational.singleton.v3.domain.DclRuleBook;
import com.advance.patterns.creational.singleton.v3.domain.EnumRuleBook;
import com.advance.patterns.creational.singleton.v3.domain.HolderRuleBook;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 单例发布、并发引用、序列化和 Spring 作用域的打印式对照。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public class SingletonTest {
    /**
     * testSequentialIdentity：直接调用观察业务边界。
     * <p>期望结果：Holder、DCL 和枚举各自重复获取引用相同均为 true；Holder 批次上限 100。</p>
     */
    @Test
    public void testSequentialIdentity() {
        System.out.println("\ntestSequentialIdentity ---> 期望：Holder、DCL 和枚举各自重复获取引用相同均为 true；Holder 批次上限 100");
        System.out.println("Holder 引用相同：" + (HolderRuleBook.getInstance() == HolderRuleBook.getInstance()));
        System.out.println("DCL 引用相同：" + (DclRuleBook.getInstance() == DclRuleBook.getInstance()));
        System.out.println("枚举引用相同：" + (EnumRuleBook.INSTANCE == EnumRuleBook.INSTANCE));
        System.out.println("教学批次上限：" + HolderRuleBook.getInstance().getMaxBatchSize());
    }

    /**
     * testConcurrentIdentity：直接调用观察业务边界。
     * <p>期望结果：8 个并发任务获取后，Holder、DCL 和枚举各自实例集合大小均为 1。</p>
     * @throws Exception 测试等待或资源操作异常
     */
    @Test
    public void testConcurrentIdentity() throws Exception {
        System.out.println("\ntestConcurrentIdentity ---> 期望：8 个并发任务获取后，Holder、DCL 和枚举各自实例集合大小均为 1");
        ExecutorService executor = Executors.newFixedThreadPool(4);
        try {
            List<Future<List<Object>>> futures = new ArrayList<Future<List<Object>>>();
            for (int i = 0; i < 8; i++) {
                futures.add(executor.submit(new RuleLookup()));
            }
            Set<Object> holders = new HashSet<Object>();
            Set<Object> dcls = new HashSet<Object>();
            Set<Object> enums = new HashSet<Object>();
            for (Future<List<Object>> future : futures) {
                List<Object> values = future.get(5, TimeUnit.SECONDS);
                holders.add(values.get(0));
                dcls.add(values.get(1));
                enums.add(values.get(2));
            }
            System.out.println("Holder 实例数：" + holders.size());
            System.out.println("DCL 实例数：" + dcls.size());
            System.out.println("枚举实例数：" + enums.size());
        } finally {
            executor.shutdownNow();
            System.out.println("线程池已结束：" + executor.awaitTermination(5, TimeUnit.SECONDS));
        }
    }

    /**
     * testSpringScope：直接调用观察业务边界。
     * <p>期望结果：同一容器相同 Bean 引用相同为 true；不同容器的该 Bean 引用相同为 false。</p>
     */
    @Test
    public void testSpringScope() {
        System.out.println("\ntestSpringScope ---> 期望：同一容器相同 Bean 引用相同为 true；不同容器的该 Bean 引用相同为 false");
        try (AnnotationConfigApplicationContext first = new AnnotationConfigApplicationContext(RuleConfig.class);
                AnnotationConfigApplicationContext second = new AnnotationConfigApplicationContext(RuleConfig.class)) {
            ContextRuleBook one = first.getBean(ContextRuleBook.class);
            ContextRuleBook again = first.getBean(ContextRuleBook.class);
            ContextRuleBook other = second.getBean(ContextRuleBook.class);
            System.out.println("同一容器：" + (one == again));
            System.out.println("不同容器：" + (one == other));
        }
    }

    /**
     * testEnumSerialization：直接调用观察业务边界。
     * <p>期望结果：枚举经内存序列化和反序列化后，引用相同为 true。</p>
     * @throws Exception 测试等待或资源操作异常
     */
    @Test
    public void testEnumSerialization() throws Exception {
        System.out.println("\ntestEnumSerialization ---> 期望：枚举经内存序列化和反序列化后，引用相同为 true");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream output = new ObjectOutputStream(bytes)) {
            output.writeObject(EnumRuleBook.INSTANCE);
        }
        try (ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            Object restored = input.readObject();
            System.out.println("反序列化后引用相同：" + (restored == EnumRuleBook.INSTANCE));
        }
    }

    /**
     * 普通具名并发任务，不使用 Lambda；只读取三个单例入口。
     *
     * @author advance
     * @since 2026-10-27 20:33:03
     */
    private static final class RuleLookup implements Callable<List<Object>> {
        /**
         * 收集当前线程观察到的实例。
         * @return 按 Holder、DCL、枚举顺序排列的实例列表
         */
        @Override
        public List<Object> call() {
            List<Object> values = new ArrayList<Object>();
            values.add(HolderRuleBook.getInstance());
            values.add(DclRuleBook.getInstance());
            values.add(EnumRuleBook.INSTANCE);
            return values;
        }
    }
}
