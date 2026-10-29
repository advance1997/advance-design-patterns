package com.advance.patterns.behavioral.iterator.v3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 保留案件分组结构，并为每次遍历创建独立的自定义迭代器。
 *
 * @author advance
 * @since 2026-10-29 09:17:03
 */
public class GroupedCasePool implements CasePool {
    /** 保持原始分组关系的只读快照。 */
    private final List<List<CaseInfo>> groups;

    /**
     * 创建只读案件快照，避免调用方在遍历期间改变集合结构。
     *
     * @param source 按业务组组织的案件；外层列表及每个分组均不允许为 null
     * @throws IllegalArgumentException 外层列表或分组为 null 时抛出
     */
    public GroupedCasePool(List<List<CaseInfo>> source) {
        if (source == null) {
            throw new IllegalArgumentException("案件分组不能为空");
        }
        List<List<CaseInfo>> copiedGroups = new ArrayList<List<CaseInfo>>();
        for (List<CaseInfo> group : source) {
            if (group == null) {
                throw new IllegalArgumentException("案件组不能为空");
            }
            copiedGroups.add(Collections.unmodifiableList(new ArrayList<CaseInfo>(group)));
        }
        this.groups = Collections.unmodifiableList(copiedGroups);
        System.out.println("[迭代器/v3] GroupedCasePool 构造方法 ---> 保存分组结构只读快照");
    }

    /**
     * 创建新的遍历器，使同一案件池可被多次独立遍历。
     *
     * @return 从案件池开头开始的独立迭代器
     */
    @Override
    public Iterator<CaseInfo> iterator() {
        System.out.println("[迭代器/v3] GroupedCasePool.iterator ---> 创建独立游标");
        return new GroupedCaseIterator(groups);
    }
}
