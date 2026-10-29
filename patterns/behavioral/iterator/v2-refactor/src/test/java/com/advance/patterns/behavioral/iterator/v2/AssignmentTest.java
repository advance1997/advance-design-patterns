package com.advance.patterns.behavioral.iterator.v2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * 验证通过统一 Iterable 接口遍历案件池。
 *
 * @author advance
 * @since 2026-10-29 09:17:03
 */
public class AssignmentTest {
    /**
     * 输入包含空业务组；期望服务无需了解分组结构，仍依次打印 10001、10002、20001、20002。
     */
    @Test
    public void shouldAssignThroughIterableCasePool() {
        List<List<CaseInfo>> groups = new ArrayList<List<CaseInfo>>();
        groups.add(Arrays.asList(new CaseInfo(10001L), new CaseInfo(10002L)));
        groups.add(Collections.<CaseInfo>emptyList());
        groups.add(Arrays.asList(new CaseInfo(20001L), new CaseInfo(20002L)));
        System.out.println("期望路径：GroupedCasePool 隐藏分组并提供 Iterable，AssignmentService 只处理案件");
        new AssignmentService().assignAll(new GroupedCasePool(groups));
    }
}
