package com.advance.patterns.structural.flyweight.v3;

import com.advance.patterns.structural.flyweight.v3.domain.CollectionTask;
import com.advance.patterns.structural.flyweight.v3.flyweight.FlyweightFactory;
import com.advance.patterns.structural.flyweight.v3.service.CollectionService;
import org.junit.jupiter.api.Test;

/**
 * 只打印观察规则共享与案件外在状态隔离，不加入无关性能或并发测试。
 *
 * @author advance
 * @since 2026-10-28 21:22:00
 */
class FlyweightTest {
    /**
     * 两个案件使用同一套规则，但分别归集不同材料。
     * 期望：必需清单相同、归集状态独立，规则工厂只缓存 3 个享元。
     */
    @Test
    void shareRulesAndIsolateCaseState() {
        FlyweightFactory factory = new FlyweightFactory();
        CollectionService service = new CollectionService(factory);
        CollectionTask first = service.createTask("DEMO-001");
        CollectionTask second = service.createTask("DEMO-002");
        first.collect("CONTRACT");
        second.collect("REPAYMENT");

        System.out.println("案件一必需材料：" + first.getRequired());
        System.out.println("案件一已归集材料：" + first.getCollected());
        System.out.println("案件二必需材料：" + second.getRequired());
        System.out.println("案件二已归集材料：" + second.getCollected());
        System.out.println("共享享元数量（期望 3）：" + factory.size());
    }
}
