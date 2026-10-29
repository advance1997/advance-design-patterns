package com.advance.patterns.behavioral.iterator.v2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 将分组结构转换为只读线性列表，并通过 JDK Iterator 提供遍历。
 *
 * @author advance
 * @since 2026-10-29 09:17:03
 */
public class GroupedCasePool implements CasePool {
    /** 展平后供 JDK Iterator 遍历的只读案件快照。 */
    private final List<CaseInfo> cases;

    /**
     * 创建案件池并按组顺序复制案件，空组不会产生案件元素。
     *
     * @param groups 按业务组组织的模拟案件
     */
    public GroupedCasePool(List<List<CaseInfo>> groups) {
        List<CaseInfo> flattenedCases = new ArrayList<CaseInfo>();
        for (List<CaseInfo> group : groups) {
            flattenedCases.addAll(group);
        }
        this.cases = Collections.unmodifiableList(flattenedCases);
    }

    /**
     * 为本次遍历提供新的 JDK 集合迭代器。
     *
     * @return 独立的案件迭代器
     */
    @Override
    public Iterator<CaseInfo> iterator() {
        System.out.println("[迭代器/v2] GroupedCasePool.iterator ---> 使用 JDK Iterator 遍历已展平的案件快照");
        return cases.iterator();
    }
}
