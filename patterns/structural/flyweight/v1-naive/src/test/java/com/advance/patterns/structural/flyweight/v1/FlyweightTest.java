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
     * 输入两个虚构案件，使用同一套诉讼材料规则。
     * 期望：v1输出两份相同必需材料；每个案件都会重新构建规则。
     */
    @Test
    void shareRulesAndIsolateCaseState() {
        CollectionService service = new CollectionService();
CollectionTask first = service.createTask("DEMO-001");
CollectionTask second = service.createTask("DEMO-002");
System.out.println("案件一材料：" + first.getCollected());
System.out.println("案件二材料：" + second.getCollected());
System.out.println("期望：两个案件规则内容相同，但 v1 每次创建完整规则对象。");
    }
}
