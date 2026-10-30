package com.advance.patterns.comprehensive.material.v1;

import java.util.List;

/** 创建归集任务并冻结场景规则结果。 @author advance @since 2026-10-30 12:00:00 */
public class MaterialTaskService {
    private final RequirementResolver resolver;
    private final MockMaterialTaskRepository repository;
    /** @param resolver 场景规则解析器 @param repository 模拟任务存储 */
    public MaterialTaskService(RequirementResolver resolver, MockMaterialTaskRepository repository) {
        this.resolver = resolver; this.repository = repository;
    }
    /** @param request 归集请求 @return 新建或已存在的幂等任务 */
    public MaterialTask createIfAbsent(MaterialRequest request) {
        System.out.println("[最终架构/TaskService] createIfAbsent ---> 解析材料要求并创建任务");
        List<MaterialRequirement> requirements = resolver.resolve(request.getScene(), request.getCaseId());
        return repository.createIfAbsent(request, requirements);
    }
}
