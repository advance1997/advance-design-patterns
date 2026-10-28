package com.advance.patterns.behavioral.command.v3.handler;

import com.advance.patterns.behavioral.command.v3.command.OperationCommand;
import com.advance.patterns.behavioral.command.v3.receiver.CaseActionService;

/**
 * 重新分案命令处理器。
 *
 * @author advance
 * @since 2026-10-29 00:20:42
 */
public class ReassignHandler implements CommandHandler {
    private final CaseActionService caseActionService;

    /**
     * 创建重新分案处理器。
     *
     * @param caseActionService 案件业务接收者
     */
    public ReassignHandler(CaseActionService caseActionService) {
        this.caseActionService = caseActionService;
    }

    @Override
    public OperationCommand.Type supportType() { return OperationCommand.Type.REASSIGN; }

    @Override
    public void execute(OperationCommand command) {
        System.out.println("[命令/v3] ReassignHandler.execute ---> 处理 REASSIGN");
        caseActionService.reassign(command.getCaseId(), command.getTargetAssigneeId());
    }
}
