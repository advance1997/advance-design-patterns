package com.advance.patterns.structural.flyweight.v1;

import com.advance.patterns.structural.flyweight.v1.domain.CollectionTask;
import com.advance.patterns.structural.flyweight.v1.domain.MaterialRule;
import com.advance.patterns.structural.flyweight.v1.service.CollectionService;
import org.junit.jupiter.api.Test;

/**
 * 只打印观察规则共享与案件外在状态隔离，不加入无关性能或并发测试。
 *
 * @author advance
 * @since 2026-10-28 21:22:00
 */
class FlyweightTest {
    /**
     * 两个案件使用相同规则，但分别归集不同材料。
     * 期望：两案必需清单相同，各自归集状态不同；v1 创建 6 个规则对象。
     */
    @Test
    void shareRulesAndIsolateCaseState() {
        CollectionService service = new CollectionService();
        CollectionTask first = service.createTask("DEMO-001");
        CollectionTask second = service.createTask("DEMO-002");
        first.collect("CONTRACT");
        second.collect("REPAYMENT");

        System.out.println("案件一必需材料：" + first.getRequired());
        System.out.println("案件一已归集材料：" + first.getCollected());
        System.out.println("案件二必需材料：" + second.getRequired());
        System.out.println("案件二已归集材料：" + second.getCollected());
        System.out.println("规则对象创建数量（期望 6）：" + MaterialRule.getCreatedCount());
    }
}
