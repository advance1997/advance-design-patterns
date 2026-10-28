package com.advance.patterns.behavioral.command.v3.handler;

import com.advance.patterns.behavioral.command.v3.command.OperationCommand;
import com.advance.patterns.behavioral.command.v3.receiver.CaseActionService;

/**
 * 暂停外呼命令处理器。
 *
 * @author advance
 * @since 2026-10-29 00:20:42
 */
public class PauseCallHandler implements CommandHandler {
    private final CaseActionService caseActionService;

    /**
     * 创建暂停外呼处理器。
     *
     * @param caseActionService 案件业务接收者
     */
    public PauseCallHandler(CaseActionService caseActionService) {
        this.caseActionService = caseActionService;
    }

    @Override
    public OperationCommand.Type supportType() { return OperationCommand.Type.PAUSE_CALL; }

    @Override
    public void execute(OperationCommand command) {
        System.out.println("[命令/v3] PauseCallHandler.execute ---> 处理 PAUSE_CALL");
        caseActionService.pauseCall(command.getCaseId(), command.getReason());
    }
}
