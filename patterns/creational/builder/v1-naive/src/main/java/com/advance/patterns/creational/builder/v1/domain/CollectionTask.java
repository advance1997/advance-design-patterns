package com.advance.patterns.creational.builder.v1.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 长构造器版本：数据合法但同类型参数多，调用处难辨含义。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public final class CollectionTask {
    /** 教学批次上限，不是实际机构的监管或经营限额。 */
    private static final int MAX_BATCH_SIZE = 1000;
    /** 任务名称。 */
    private final String taskName;
    /** 最小逾期天数（含）。 */
    private final int minOverdueDays;
    /** 最大逾期天数（含）。 */
    private final int maxOverdueDays;
    /** 模拟执行窗口起始小时（含）。 */
    private final int startHour;
    /** 模拟执行窗口结束小时（不含）。 */
    private final int endHour;
    /** 最大批次案件数。 */
    private final int batchSize;
    /** 虚构案件编号列表。 */
    private final List<String> caseIds;

    /**
     * 使用位置参数创建完整任务，参数顺序容易混淆。
     * @param taskName 任务名称
     * @param minOverdueDays 最小逾期天数（含）
     * @param maxOverdueDays 最大逾期天数（含）
     * @param startHour 模拟执行窗口起始小时（含）
     * @param endHour 模拟执行窗口结束小时（不含）
     * @param batchSize 最大批次案件数
     * @param caseIds 虚构案件编号列表
     * @throws IllegalArgumentException 参数或组合不合法
     */
    public CollectionTask(String taskName, int minOverdueDays, int maxOverdueDays,
            int startHour, int endHour, int batchSize, List<String> caseIds) {
        this.taskName = taskName;
        this.minOverdueDays = minOverdueDays;
        this.maxOverdueDays = maxOverdueDays;
        this.startHour = startHour;
        this.endHour = endHour;
        this.batchSize = batchSize;
        this.caseIds = caseIds == null ? null : new ArrayList<String>(caseIds);
        validate();
    }

    /**
     * 返回案件列表的只读快照，调用方不能修改任务内部集合。
     * @return 独立只读列表；未设置列表时返回空列表
     */
    public List<String> getCaseIds() {
        if (caseIds == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(new ArrayList<String>(caseIds));
    }

    /**
     * 校验任务并输出模拟配置；不调度、不联系客户。
     * @return 包含配置值的教学摘要
     * @throws IllegalArgumentException 配置未完成或已被改成非法组合
     */
    public String preview() {
        validate();
        System.out.println("[builder/v1] CollectionTask.preview ---> 仅预览人工复核配置，不触达客户");
        return "taskName=" + taskName + ", overdueDays=" + minOverdueDays + "-" + maxOverdueDays
                + ", window=" + startHour + "-" + endHour + ", batchSize=" + batchSize + ", caseIds=" + caseIds;
    }

    /**
     * 校验单字段与组合约束，示例时段不代表任何真实法定触达许可。
     * @throws IllegalArgumentException 名称、天数范围、时间窗口、批次或案件列表非法
     */
    private void validate() {
        if (taskName == null || taskName.trim().isEmpty()) {
            throw new IllegalArgumentException("taskName 不能为空");
        }
        if (minOverdueDays < 0 || maxOverdueDays < minOverdueDays) {
            throw new IllegalArgumentException("逾期天数范围必须非负且下限不大于上限");
        }
        if (startHour < 0 || endHour > 24 || startHour >= endHour) {
            throw new IllegalArgumentException("执行窗口必须在 0 至 24 小时内且开始早于结束");
        }
        if (batchSize <= 0 || batchSize > MAX_BATCH_SIZE) {
            throw new IllegalArgumentException("batchSize 必须在 1 至 1000 之间");
        }
        if (caseIds == null || caseIds.isEmpty() || caseIds.size() > batchSize) {
            throw new IllegalArgumentException("caseIds 必须非空且数量不超过 batchSize");
        }
        Set<String> uniqueIds = new HashSet<String>();
        for (String caseId : caseIds) {
            if (caseId == null || caseId.trim().isEmpty() || !uniqueIds.add(caseId)) {
                throw new IllegalArgumentException("案件编号不能为空或重复");
            }
        }
    }
}
