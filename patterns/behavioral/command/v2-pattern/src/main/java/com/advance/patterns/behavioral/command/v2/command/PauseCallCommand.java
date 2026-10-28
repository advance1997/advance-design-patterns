package com.advance.patterns.behavioral.command.v2.command;

import com.advance.patterns.behavioral.command.v2.receiver.CaseActionService;

/**
 * 暂停案件外呼的具体命令。
 *
 * @author advance
 * @since 2026-10-29 00:20:42
 */
public class PauseCallCommand implements CaseCommand {
    private final String commandId;
    private final CaseActionService receiver;
    private final long caseId;
    private final String reason;

    /**
     * 创建暂停外呼命令。
     *
     * @param commandId 命令编号
     * @param receiver 执行业务动作的接收者
     * @param caseId 案件编号
     * @param reason 暂停原因
     */
    public PauseCallCommand(String commandId, CaseActionService receiver, long caseId, String reason) {
        this.commandId = commandId;
        this.receiver = receiver;
        this.caseId = caseId;
        this.reason = reason;
    }

    @Override
    public String getCommandId() { return commandId; }

    @Override
    public void execute() {
        System.out.println("[命令/v2] PauseCallCommand.execute ---> 执行暂停外呼命令");
        receiver.pauseCall(caseId, reason);
    }
}
