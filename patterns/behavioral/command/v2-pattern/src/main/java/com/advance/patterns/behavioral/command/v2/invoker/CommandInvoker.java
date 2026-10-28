package com.advance.patterns.behavioral.command.v2.invoker;

import com.advance.patterns.behavioral.command.v2.command.CaseCommand;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * 命令调用者负责排队和触发执行，不判断具体命令类型。
 *
 * @author advance
 * @since 2026-10-29 00:20:42
 */
public class CommandInvoker {
    private final Queue<CaseCommand> commands = new ArrayDeque<CaseCommand>();

    /**
     * 提交命令但不立即执行。
     *
     * @param command 待执行命令
     */
    public void submit(CaseCommand command) {
        commands.offer(command);
        System.out.println("[命令/v2] CommandInvoker.submit ---> 命令已排队：" + command.getCommandId());
    }

    /**
     * 按提交顺序执行当前队列中的命令。
     */
    public void executeAll() {
        while (!commands.isEmpty()) {
            CaseCommand command = commands.poll();
            System.out.println("[命令/v2] CommandInvoker.executeAll ---> 调用统一 execute 接口");
            command.execute();
        }
    }
}
