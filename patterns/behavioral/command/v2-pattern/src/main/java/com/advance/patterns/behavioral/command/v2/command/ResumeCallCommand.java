package com.advance.patterns.behavioral.command.v2.command;

import com.advance.patterns.behavioral.command.v2.receiver.CaseActionService;

/**
 * 恢复案件外呼的具体命令。
 *
 * @author advance
 * @since 2026-10-29 00:20:42
 */
public class ResumeCallCommand implements CaseCommand {
    private final String commandId;
    private final CaseActionService receiver;
    private final long caseId;

    /**
     * 创建恢复外呼命令。
     *
     * @param commandId 命令编号
     * @param receiver 执行业务动作的接收者
     * @param caseId 案件编号
     */
    public ResumeCallCommand(String commandId, CaseActionService receiver, long caseId) {
        this.commandId = commandId;
        this.receiver = receiver;
        this.caseId = caseId;
    }

    @Override
    public String getCommandId() { return commandId; }

    @Override
    public void execute() {
        System.out.println("[命令/v2] ResumeCallCommand.execute ---> 执行恢复外呼命令");
        receiver.resumeCall(caseId);
    }
}
