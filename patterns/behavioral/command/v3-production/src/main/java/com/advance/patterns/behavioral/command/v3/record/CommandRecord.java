package com.advance.patterns.behavioral.command.v3.record;

import com.advance.patterns.behavioral.command.v3.command.OperationCommand;

/**
 * 模拟命令表中的记录及其执行状态。
 *
 * @author advance
 * @since 2026-10-29 00:20:42
 */
public class CommandRecord {
    private final OperationCommand command;
    private CommandStatus status;
    private String lastError;

    /**
     * 创建待执行命令记录。
     *
     * @param command 操作数据
     */
    public CommandRecord(OperationCommand command) {
        this.command = command;
        this.status = CommandStatus.PENDING;
    }

    /**
     * 尝试将待执行记录标记为运行中。
     *
     * @return 仅状态为 PENDING 时返回 {@code true}
     */
    public boolean claim() {
        if (status != CommandStatus.PENDING) {
            return false;
        }
        status = CommandStatus.RUNNING;
        return true;
    }

    /** 标记执行成功。 */
    public void markSuccess() { status = CommandStatus.SUCCESS; }

    /**
     * 标记执行失败并保存简短错误信息。
     *
     * @param error 错误信息
     */
    public void markFailed(String error) {
        status = CommandStatus.FAILED;
        lastError = error;
    }

    /** @return 操作数据 */
    public OperationCommand getCommand() { return command; }

    /** @return 当前执行状态 */
    public CommandStatus getStatus() { return status; }

    /** @return 最近一次错误信息 */
    public String getLastError() { return lastError; }

    @Override
    public String toString() {
        return "命令=" + command.getCommandId() + "，类型=" + command.getType()
                + "，状态=" + status + (lastError == null ? "" : "，失败原因=" + lastError);
    }
}
