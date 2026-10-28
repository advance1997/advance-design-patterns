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
     * 输入两个虚构案件，使用同一套诉讼材料规则。
     * 期望：v3输出两份相同必需材料；规则缓存只保留 3 个享元，案件一追加 RECORDING 不影响另一个案件。
     */
    @Test
    void shareRulesAndIsolateCaseState() {
        FlyweightFactory factory = new FlyweightFactory();
CollectionService service = new CollectionService(factory);
CollectionTask first = service.createTask("DEMO-001");
CollectionTask second = service.createTask("DEMO-002");
first.collect("RECORDING");
System.out.println("案件一材料：" + first.getCollected());
System.out.println("案件二材料：" + second.getCollected());
System.out.println("共享享元数量：" + factory.size());
System.out.println("同一规则对象由工厂缓存复用；案件归集列表保持独立。");
    }
}
