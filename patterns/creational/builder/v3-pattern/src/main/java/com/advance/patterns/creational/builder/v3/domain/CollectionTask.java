package com.advance.patterns.creational.builder.v3.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 不可变催收人工复核任务；Builder 完成配置，build 一次校验并复制快照。
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
     * 从建造者创建隔离的任务快照。
     * @param builder 已填入参数的建造者
     * @throws IllegalArgumentException 参数或组合不合法
     */
    private CollectionTask(Builder builder) {
        this.taskName = builder.taskName;
        this.minOverdueDays = builder.minOverdueDays;
        this.maxOverdueDays = builder.maxOverdueDays;
        this.startHour = builder.startHour;
        this.endHour = builder.endHour;
        this.batchSize = builder.batchSize;
        this.caseIds = builder.caseIds == null ? null : new ArrayList<String>(builder.caseIds);
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
        System.out.println("[builder/v3] CollectionTask.preview ---> 仅预览人工复核配置，不触达客户");
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

    /**
     * 普通可复用建造者，非线程安全；build 返回独立不可变任务。
     *
     * @author advance
     * @since 2026-10-27 20:33:03
     */
    public static final class Builder {
        /** 任务名称。 */
        private String taskName;
        /** 最小逾期天数（含），教学默认值 1。 */
        private int minOverdueDays = 1;
        /** 最大逾期天数（含），教学默认值 30。 */
        private int maxOverdueDays = 30;
        /** 模拟执行窗口起始小时（含），教学默认值 9。 */
        private int startHour = 9;
        /** 模拟执行窗口结束小时（不含），教学默认值 18。 */
        private int endHour = 18;
        /** 最大批次案件数，教学默认值 100。 */
        private int batchSize = 100;
        /** 虚构案件编号列表。 */
        private List<String> caseIds;

        /**
         * 显式提供任务名称，最终校验在 build 进行。
         * @param taskName 任务名称
         */
        public Builder(String taskName) {
            this.taskName = taskName;
        }

        /**
         * 设置任务名称，完整约束在build 时校验。
         * @param taskName 任务名称
         * @return 当前建造者
         */
        public Builder taskName(String taskName) {
            this.taskName = taskName;
            return this;
        }

        /**
         * 设置最小逾期天数（含），完整约束在build 时校验。
         * @param minOverdueDays 最小逾期天数（含）
         * @return 当前建造者
         */
        public Builder minOverdueDays(int minOverdueDays) {
            this.minOverdueDays = minOverdueDays;
            return this;
        }

        /**
         * 设置最大逾期天数（含），完整约束在build 时校验。
         * @param maxOverdueDays 最大逾期天数（含）
         * @return 当前建造者
         */
        public Builder maxOverdueDays(int maxOverdueDays) {
            this.maxOverdueDays = maxOverdueDays;
            return this;
        }

        /**
         * 设置模拟执行窗口起始小时（含），完整约束在build 时校验。
         * @param startHour 模拟执行窗口起始小时（含）
         * @return 当前建造者
         */
        public Builder startHour(int startHour) {
            this.startHour = startHour;
            return this;
        }

        /**
         * 设置模拟执行窗口结束小时（不含），完整约束在build 时校验。
         * @param endHour 模拟执行窗口结束小时（不含）
         * @return 当前建造者
         */
        public Builder endHour(int endHour) {
            this.endHour = endHour;
            return this;
        }

        /**
         * 设置最大批次案件数，完整约束在build 时校验。
         * @param batchSize 最大批次案件数
         * @return 当前建造者
         */
        public Builder batchSize(int batchSize) {
            this.batchSize = batchSize;
            return this;
        }

        /**
         * 设置虚构案件编号列表，完整约束在build 时校验。
         * @param caseIds 虚构案件编号列表
         * @return 当前建造者
         */
        public Builder caseIds(List<String> caseIds) {
            this.caseIds = caseIds == null ? null : new ArrayList<String>(caseIds);
            return this;
        }

        /**
         * 完整校验并创建快照，后续修改建造者不影响已有任务。
         * @return 新的不可变任务
         * @throws IllegalArgumentException 参数或组合不合法
         */
        public CollectionTask build() {
            System.out.println("[builder/v3] CollectionTask.Builder.build ---> 校验配置并生成不可变快照");
            return new CollectionTask(this);
        }
    }
}
