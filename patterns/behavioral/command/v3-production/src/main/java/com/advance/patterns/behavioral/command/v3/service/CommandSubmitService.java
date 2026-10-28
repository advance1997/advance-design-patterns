package com.advance.patterns.behavioral.command.v3.service;

import com.advance.patterns.behavioral.command.v3.command.OperationCommand;
import com.advance.patterns.behavioral.command.v3.repository.InMemoryCommandRepository;

/**
 * 生产案例中的命令提交服务：接收操作请求、保存命令数据并立即返回编号。
 *
 * @author advance
 * @since 2026-10-29 00:20:42
 */
public class CommandSubmitService {
    private final InMemoryCommandRepository repository;

    /**
     * 创建命令提交服务。
     *
     * @param repository 内存模拟命令仓库
     */
    public CommandSubmitService(InMemoryCommandRepository repository) {
        this.repository = repository;
    }

    /**
     * 提交暂停外呼操作。
     *
     * @param commandId 操作编号
     * @param caseId 案件编号
     * @param reason 暂停原因
     * @return 操作编号
     */
    public String submitPauseCall(String commandId, long caseId, String reason) {
        return submit(new OperationCommand(commandId, OperationCommand.Type.PAUSE_CALL,
                caseId, reason, null));
    }

    /**
     * 提交重新分案操作。
     *
     * @param commandId 操作编号
     * @param caseId 案件编号
     * @param targetAssigneeId 目标作业员
     * @return 操作编号
     */
    public String submitReassign(String commandId, long caseId, String targetAssigneeId) {
        return submit(new OperationCommand(commandId, OperationCommand.Type.REASSIGN,
                caseId, null, targetAssigneeId));
    }

    private String submit(OperationCommand command) {
        System.out.println("[命令/v3] CommandSubmitService.submit ---> 接收请求并保存操作意图");
        repository.save(command);
        System.out.println("[命令/v3] CommandSubmitService.submit ---> 立即返回操作编号："
                + command.getCommandId());
        return command.getCommandId();
    }
}
