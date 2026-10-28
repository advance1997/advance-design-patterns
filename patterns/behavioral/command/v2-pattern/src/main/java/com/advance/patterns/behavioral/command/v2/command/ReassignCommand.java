package com.advance.patterns.behavioral.command.v2.command;

import com.advance.patterns.behavioral.command.v2.receiver.CaseActionService;

/**
 * 重新分配案件的具体命令。
 *
 * @author advance
 * @since 2026-10-29 00:20:42
 */
public class ReassignCommand implements CaseCommand {
    private final String commandId;
    private final CaseActionService receiver;
    private final long caseId;
    private final String targetAssigneeId;

    /**
     * 创建重新分案命令。
     *
     * @param commandId 命令编号
     * @param receiver 执行业务动作的接收者
     * @param caseId 案件编号
     * @param targetAssigneeId 目标作业员
     */
    public ReassignCommand(String commandId, CaseActionService receiver, long caseId,
            String targetAssigneeId) {
        this.commandId = commandId;
        this.receiver = receiver;
        this.caseId = caseId;
        this.targetAssigneeId = targetAssigneeId;
    }

    @Override
    public String getCommandId() { return commandId; }

    @Override
    public void execute() {
        System.out.println("[命令/v2] ReassignCommand.execute ---> 执行重新分案命令");
        receiver.reassign(caseId, targetAssigneeId);
    }
}
