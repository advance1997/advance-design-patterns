package com.advance.patterns.behavioral.command.v3.repository;

import com.advance.patterns.behavioral.command.v3.command.OperationCommand;
import com.advance.patterns.behavioral.command.v3.record.CommandRecord;
import com.advance.patterns.behavioral.command.v3.record.CommandStatus;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 用内存集合模拟命令记录存储；不提供数据库持久化或跨进程锁。
 *
 * @author advance
 * @since 2026-10-29 00:20:42
 */
public class InMemoryCommandRepository {
    private final Map<String, CommandRecord> records = new LinkedHashMap<String, CommandRecord>();

    /**
     * 保存一条待执行命令。
     *
     * @param command 操作数据
     */
    public void save(OperationCommand command) {
        if (records.containsKey(command.getCommandId())) {
            throw new IllegalArgumentException("命令编号重复：" + command.getCommandId());
        }
        records.put(command.getCommandId(), new CommandRecord(command));
        System.out.println("[命令/v3] InMemoryCommandRepository.save ---> 保存 PENDING 命令");
    }

    /**
     * 获取当前待执行记录快照。
     *
     * @return 当前状态为 PENDING 的记录
     */
    public List<CommandRecord> findPending() {
        List<CommandRecord> pending = new ArrayList<CommandRecord>();
        for (CommandRecord record : records.values()) {
            if (record.getStatus() == CommandStatus.PENDING) {
                pending.add(record);
            }
        }
        return pending;
    }

    /**
     * 按命令编号查找记录。
     *
     * @param commandId 命令编号
     * @return 命令记录
     */
    public CommandRecord findById(String commandId) {
        CommandRecord record = records.get(commandId);
        if (record == null) {
            throw new IllegalArgumentException("命令不存在：" + commandId);
        }
        return record;
    }
}
