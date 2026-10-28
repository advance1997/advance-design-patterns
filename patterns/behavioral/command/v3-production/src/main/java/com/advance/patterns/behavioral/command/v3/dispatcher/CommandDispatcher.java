package com.advance.patterns.behavioral.command.v3.dispatcher;

import com.advance.patterns.behavioral.command.v3.command.OperationCommand;
import com.advance.patterns.behavioral.command.v3.handler.CommandHandler;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据命令类型定位 Handler，分派器不包含具体业务分支。
 *
 * @author advance
 * @since 2026-10-29 00:20:42
 */
public class CommandDispatcher {
    private final Map<OperationCommand.Type, CommandHandler> handlers =
            new LinkedHashMap<OperationCommand.Type, CommandHandler>();

    /**
     * 注册已提供的命令处理器。
     *
     * @param handlerList 命令处理器列表
     */
    public CommandDispatcher(List<CommandHandler> handlerList) {
        for (CommandHandler handler : handlerList) {
            CommandHandler previous = handlers.put(handler.supportType(), handler);
            if (previous != null) {
                throw new IllegalArgumentException("命令类型重复注册：" + handler.supportType());
            }
            System.out.println("[命令/v3] CommandDispatcher.register ---> 注册 " + handler.supportType());
        }
    }

    /**
     * 将命令分派给对应处理器。
     *
     * @param command 待执行命令
     */
    public void dispatch(OperationCommand command) {
        CommandHandler handler = handlers.get(command.getType());
        if (handler == null) {
            throw new IllegalArgumentException("没有命令处理器：" + command.getType());
        }
        System.out.println("[命令/v3] CommandDispatcher.dispatch ---> 按类型分派 " + command.getType());
        handler.execute(command);
    }
}
