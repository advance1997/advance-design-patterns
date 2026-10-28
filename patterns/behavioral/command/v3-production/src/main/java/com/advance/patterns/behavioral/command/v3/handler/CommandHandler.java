package com.advance.patterns.behavioral.command.v3.handler;

import com.advance.patterns.behavioral.command.v3.command.OperationCommand;

/**
 * 命令执行处理器接口；处理行为与可保存的命令数据分离。
 *
 * @author advance
 * @since 2026-10-29 00:20:42
 */
public interface CommandHandler {
    /** @return 此处理器支持的命令类型 */
    OperationCommand.Type supportType();

    /**
     * 执行命令对应的业务动作。
     *
     * @param command 操作数据
     */
    void execute(OperationCommand command);
}
