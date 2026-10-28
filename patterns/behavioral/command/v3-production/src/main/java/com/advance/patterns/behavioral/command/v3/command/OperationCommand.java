package com.advance.patterns.behavioral.command.v3.command;

/**
 * 可保存和传递的操作数据，只描述要做什么，不携带 Spring Bean 或执行行为。
 *
 * @author advance
 * @since 2026-10-29 00:20:42
 */
public class OperationCommand {
    /** 催收运营支持的操作类型。 */
    public enum Type { PAUSE_CALL, REASSIGN }

    private final String commandId;
    private final Type type;
    private final long caseId;
    private final String reason;
    private final String targetAssigneeId;

    /**
     * 创建不可变操作数据。
     *
     * @param commandId 操作编号
     * @param type 操作类型
     * @param caseId 案件编号
     * @param reason 暂停原因；重新分案时为空
     * @param targetAssigneeId 目标作业员；暂停外呼时为空
     */
    public OperationCommand(String commandId, Type type, long caseId,
            String reason, String targetAssigneeId) {
        this.commandId = commandId;
        this.type = type;
        this.caseId = caseId;
        this.reason = reason;
        this.targetAssigneeId = targetAssigneeId;
    }

    /** @return 操作编号 */
    public String getCommandId() { return commandId; }

    /** @return 操作类型 */
    public Type getType() { return type; }

    /** @return 案件编号 */
    public long getCaseId() { return caseId; }

    /** @return 暂停原因 */
    public String getReason() { return reason; }

    /** @return 目标作业员 */
    public String getTargetAssigneeId() { return targetAssigneeId; }
}
