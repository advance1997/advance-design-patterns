package com.advance.patterns.behavioral.iterator.v2;

/**
 * 只依赖统一的可遍历案件池，不了解案件分组结构。
 *
 * @author advance
 * @since 2026-10-29 09:17:03
 */
public class AssignmentService {
    /**
     * 遍历并分配案件；内部存储结构由 Iterable 实现隐藏。
     *
     * @param casePool 可遍历的案件池
     */
    public void assignAll(Iterable<CaseInfo> casePool) {
        System.out.println("[迭代器/v2] AssignmentService.assignAll ---> 仅依赖 Iterable 案件池");
        for (CaseInfo caseInfo : casePool) {
            System.out.println("[迭代器/v2] AssignmentService.assignAll ---> 分配案件：" + caseInfo.getCaseId());
        }
    }
}
