package com.advance.patterns.behavioral.iterator.v3;

/**
 * 仅按统一遍历协议逐个处理案件。
 *
 * @author advance
 * @since 2026-10-29 09:17:03
 */
public class AssignmentService {
    /**
     * 遍历案件池并打印模拟分配路径，不访问案件池内部集合。
     *
     * @param casePool 可遍历案件池
     */
    public void assignAll(Iterable<CaseInfo> casePool) {
        System.out.println("[迭代器/v3] AssignmentService.assignAll ---> 开始逐个处理案件");
        for (CaseInfo caseInfo : casePool) {
            System.out.println("[迭代器/v3] AssignmentService.assignAll ---> 分配案件：" + caseInfo.getCaseId());
        }
    }
}
