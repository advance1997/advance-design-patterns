package com.advance.patterns.comprehensive.material.v1;

/** 保存材料级状态，以便任务部分成功后只恢复失败明细。 @author advance @since 2026-10-30 12:00:00 */
public class MaterialTaskItem {
    /** 单项材料状态。 */
    public enum Status { PENDING, SUCCESS, MISSING, RETRY_WAIT, FAILED }
    private final MaterialRequirement requirement;
    private Status status = Status.PENDING;
    private FetchResult result;
    private int attempts;
    /** @param requirement 本项冻结的材料要求 */
    public MaterialTaskItem(MaterialRequirement requirement) { this.requirement = requirement; }
    /** @return 材料要求 */ public MaterialRequirement getRequirement() { return requirement; }
    /** @return 当前状态 */ public Status getStatus() { return status; }
    /** @return 最近一次来源结果 */ public FetchResult getResult() { return result; }
    /** @return 已查询次数 */ public int getAttempts() { return attempts; }
    /**
     * 记录一次查询结果。
     * @param result FOUND/MISSING/ERROR 结果
     */
    public void record(FetchResult result) {
        this.result = result; attempts++;
        if (result.getStatus() == FetchResult.Status.FOUND) { status = Status.SUCCESS; }
        else if (result.getStatus() == FetchResult.Status.MISSING) { status = Status.MISSING; }
        else { status = Status.RETRY_WAIT; }
    }
}
