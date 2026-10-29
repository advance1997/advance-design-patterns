package com.advance.patterns.behavioral.iterator.v1;

import java.util.List;

/**
 * 直接遍历分组案件池并逐个执行模拟分案。
 *
 * @author advance
 * @since 2026-10-29 09:17:03
 */
public class AssignmentService {
    /**
     * 按业务组逐个分配案件；此版本让业务服务直接了解两层集合结构。
     *
     * @param groups 按业务组组织的案件列表，允许存在空组
     */
    public void assignAll(List<List<CaseInfo>> groups) {
        System.out.println("[迭代器/v1] AssignmentService.assignAll ---> 开始按组遍历案件");
        for (int groupIndex = 0; groupIndex < groups.size(); groupIndex++) {
            List<CaseInfo> group = groups.get(groupIndex);
            System.out.println("[迭代器/v1] AssignmentService.assignAll ---> 读取第" + (groupIndex + 1) + "个案件组");
            for (int caseIndex = 0; caseIndex < group.size(); caseIndex++) {
                CaseInfo caseInfo = group.get(caseIndex);
                System.out.println("[迭代器/v1] AssignmentService.assignAll ---> 分配案件：" + caseInfo.getCaseId());
            }
        }
    }
}
