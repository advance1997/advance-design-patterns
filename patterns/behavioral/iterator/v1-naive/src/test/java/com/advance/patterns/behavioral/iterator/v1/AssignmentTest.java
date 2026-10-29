package com.advance.patterns.behavioral.iterator.v1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * 验证直接遍历分组案件的基础实现。
 *
 * @author advance
 * @since 2026-10-29 09:17:03
 */
public class AssignmentTest {
    /**
     * 输入包含两个有案件组和一个空组；期望按顺序打印 10001、10002、20001、20002，且跳过空组。
     */
    @Test
    public void shouldAssignCasesByNestedLoops() {
        List<List<CaseInfo>> groups = new ArrayList<List<CaseInfo>>();
        groups.add(Arrays.asList(new CaseInfo(10001L), new CaseInfo(10002L)));
        groups.add(Collections.<CaseInfo>emptyList());
        groups.add(Arrays.asList(new CaseInfo(20001L), new CaseInfo(20002L)));
        System.out.println("期望路径：AssignmentService 读取分组，按 10001、10002、20001、20002 顺序分配并自然跳过空组");
        new AssignmentService().assignAll(groups);
    }
}
