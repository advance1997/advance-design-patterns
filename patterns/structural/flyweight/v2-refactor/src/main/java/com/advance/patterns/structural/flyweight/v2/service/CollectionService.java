package com.advance.patterns.structural.flyweight.v2.service;

import com.advance.patterns.structural.flyweight.v2.domain.CollectionTask;
import com.advance.patterns.structural.flyweight.v2.domain.MaterialRule;
import java.util.Arrays;
import java.util.List;

/**
 * 把规则构建提取到服务方法，仍然每次创建规则对象，问题只是被隐藏。
 *
 * @author advance
 * @since 2026-10-28 21:22:00
 */
public class CollectionService {
    public CollectionTask createTask(String caseId) {
        System.out.println("[享元/v2] CollectionService.createTask ---> 调用规则构建方法");
        List<MaterialRule> rules = buildRules();
        CollectionTask task = new CollectionTask(caseId);
        for (MaterialRule rule : rules) if (rule.isRequired()) task.require(rule.getCode());
        return task;
    }
    private List<MaterialRule> buildRules() {
        System.out.println("[享元/v2] CollectionService.buildRules ---> 每次构建相同规则");
        return Arrays.asList(new MaterialRule("CONTRACT", true),
                new MaterialRule("REPAYMENT", true), new MaterialRule("RECORDING", false));
    }
}
