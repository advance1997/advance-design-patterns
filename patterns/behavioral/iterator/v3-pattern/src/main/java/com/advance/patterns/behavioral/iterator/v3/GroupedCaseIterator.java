package com.advance.patterns.behavioral.iterator.v3;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 跨越案件分组逐个读取案件，并在遍历过程中跳过空组。
 *
 * @author advance
 * @since 2026-10-29 09:17:03
 */
public class GroupedCaseIterator implements Iterator<CaseInfo> {
    /** 待遍历的只读分组数据。 */
    private final List<List<CaseInfo>> groups;
    /** 当前所在的案件组下标。 */
    private int groupIndex;
    /** 当前案件组内下一个待返回案件的下标。 */
    private int caseIndex;

    /**
     * 创建游标初始位于第一个分组开头的迭代器。
     *
     * @param groups 已复制的只读案件分组
     */
    public GroupedCaseIterator(List<List<CaseInfo>> groups) {
        this.groups = groups;
    }

    /**
     * 将游标推进到仍有案件可读的分组，跳过已耗尽或空分组。
     */
    private void moveToAvailableGroup() {
        while (groupIndex < groups.size()) {
            List<CaseInfo> currentGroup = groups.get(groupIndex);
            if (caseIndex < currentGroup.size()) {
                return;
            }
            System.out.println("[迭代器/v3] GroupedCaseIterator.moveToAvailableGroup ---> 跳过已遍历或空案件组");
            groupIndex++;
            caseIndex = 0;
        }
    }

    /**
     * 判断当前快照中是否还有下一个案件。
     *
     * @return 有剩余案件时返回 true，否则返回 false
     */
    @Override
    public boolean hasNext() {
        moveToAvailableGroup();
        return groupIndex < groups.size();
    }

    /**
     * 返回下一个案件并推进当前迭代器自己的游标。
     *
     * @return 下一个案件
     * @throws NoSuchElementException 没有剩余案件时抛出
     */
    @Override
    public CaseInfo next() {
        if (!hasNext()) {
            System.out.println("[迭代器/v3] GroupedCaseIterator.next ---> 无剩余案件，报告遍历结束");
            throw new NoSuchElementException("案件池已遍历完毕");
        }
        CaseInfo caseInfo = groups.get(groupIndex).get(caseIndex);
        caseIndex++;
        System.out.println("[迭代器/v3] GroupedCaseIterator.next ---> 返回案件：" + caseInfo.getCaseId());
        return caseInfo;
    }

    /**
     * 当前快照迭代器不支持删除案件，避免遍历行为隐式改变案件池。
     *
     * @throws UnsupportedOperationException 始终抛出，表示删除操作不受支持
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("案件快照迭代器不支持删除");
    }
}
