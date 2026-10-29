package com.advance.patterns.behavioral.iterator.v3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * 验证自定义迭代器隐藏分组结构并维护独立游标。
 *
 * @author advance
 * @since 2026-10-29 09:17:03
 */
public class AssignmentTest {
    /**
     * 输入包含中间空组；期望首次遍历按 10001、10002、20001、20002 输出，第二个迭代器从 10001 重新开始。
     */
    @Test
    public void shouldIterateAcrossGroupsWithIndependentCursors() {
        List<List<CaseInfo>> groups = new ArrayList<List<CaseInfo>>();
        groups.add(Arrays.asList(new CaseInfo(10001L), new CaseInfo(10002L)));
        groups.add(Collections.<CaseInfo>emptyList());
        groups.add(Arrays.asList(new CaseInfo(20001L), new CaseInfo(20002L)));
        CasePool casePool = new GroupedCasePool(groups);
        System.out.println("期望路径：自定义 Iterator 隐藏分组细节，跳过空组；每次 iterator() 都从第一件案件重新遍历");
        System.out.println("第一次遍历（分配）：");
        new AssignmentService().assignAll(casePool);
        System.out.println("第二次遍历（复核）：");
        Iterator<CaseInfo> iterator = casePool.iterator();
        while (iterator.hasNext()) {
            System.out.println("[迭代器/v3] AssignmentTest.shouldIterateAcrossGroupsWithIndependentCursors ---> 复核案件：" + iterator.next().getCaseId());
        }
    }
}
