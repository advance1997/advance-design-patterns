package com.advance.patterns.structural.flyweight.v1.service;

import com.advance.patterns.structural.flyweight.v1.domain.CollectionTask;
import com.advance.patterns.structural.flyweight.v1.domain.MaterialRule;
import java.util.Arrays;
import java.util.List;

/**
 * 每个案件都重新创建完整规则列表，展示重复对象的问题。
 *
 * @author advance
 * @since 2026-10-28 21:22:00
 */
public class CollectionService {
    public CollectionTask createTask(String caseId) {
        System.out.println("[享元/v1] CollectionService.createTask ---> 为案件重新创建诉讼规则");
        List<MaterialRule> rules = Arrays.asList(
                new MaterialRule("CONTRACT", true),
                new MaterialRule("REPAYMENT", true),
                new MaterialRule("RECORDING", false));
        CollectionTask task = new CollectionTask(caseId);
        for (MaterialRule rule : rules) {
            if (rule.isRequired()) task.collect(rule.getCode());
        }
        return task;
    }
}
