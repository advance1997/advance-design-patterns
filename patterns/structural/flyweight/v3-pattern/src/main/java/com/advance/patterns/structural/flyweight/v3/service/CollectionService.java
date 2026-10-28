package com.advance.patterns.structural.flyweight.v3.service;

import com.advance.patterns.structural.flyweight.v3.domain.CollectionTask;
import com.advance.patterns.structural.flyweight.v3.flyweight.FlyweightFactory;
import com.advance.patterns.structural.flyweight.v3.flyweight.MaterialRuleFlyweight;
import java.util.Arrays;
import java.util.List;

/**
 * 只组合共享内在状态与案件外在状态，客户端不把案件数据写入享元。
 *
 * @author advance
 * @since 2026-10-28 21:22:00
 */
public class CollectionService {
    private final FlyweightFactory factory;
    public CollectionService(FlyweightFactory factory) { this.factory = factory; }
    public CollectionTask createTask(String caseId) {
        System.out.println("[享元/v3] CollectionService.createTask ---> 获取共享规则并创建案件外在状态");
        List<String> definitions = Arrays.asList("CONTRACT:true", "REPAYMENT:true", "RECORDING:false");
        CollectionTask task = new CollectionTask(caseId);
        for (String definition : definitions) {
            String[] parts = definition.split(":");
            MaterialRuleFlyweight rule = factory.get(parts[0], Boolean.parseBoolean(parts[1]));
            if (rule.isRequired()) task.collect(rule.getCode());
        }
        return task;
    }
}
