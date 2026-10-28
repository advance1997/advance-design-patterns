package com.advance.patterns.behavioral.command.v3.worker;

import com.advance.patterns.behavioral.command.v3.dispatcher.CommandDispatcher;
import com.advance.patterns.behavioral.command.v3.record.CommandRecord;
import com.advance.patterns.behavioral.command.v3.repository.InMemoryCommandRepository;
import java.util.List;

/**
 * 后台执行器的一次内存模拟；生产调度器可以定期触发同一处理流程。
 *
 * @author advance
 * @since 2026-10-29 00:20:42
 */
public class CommandWorker {
    private final InMemoryCommandRepository repository;
    private final CommandDispatcher dispatcher;

    /**
     * 创建后台命令执行器。
     *
     * @param repository 命令仓库
     * @param dispatcher 命令分派器
     */
    public CommandWorker(InMemoryCommandRepository repository, CommandDispatcher dispatcher) {
        this.repository = repository;
        this.dispatcher = dispatcher;
    }

    /**
     * 查询并处理当前待执行命令。
     */
    public void runOnce() {
        System.out.println("[命令/v3] CommandWorker.runOnce ---> 查询 PENDING 命令");
        List<CommandRecord> pending = repository.findPending();
        for (CommandRecord record : pending) {
            if (!record.claim()) {
                continue;
            }
            System.out.println("[命令/v3] CommandWorker.runOnce ---> 认领命令："
                    + record.getCommand().getCommandId());
            try {
                dispatcher.dispatch(record.getCommand());
                record.markSuccess();
                System.out.println("[命令/v3] CommandWorker.runOnce ---> 标记 SUCCESS");
            } catch (RuntimeException exception) {
                record.markFailed(exception.getMessage());
                System.out.println("[命令/v3] CommandWorker.runOnce ---> 标记 FAILED："
                        + exception.getMessage());
            }
        }
    }
}
