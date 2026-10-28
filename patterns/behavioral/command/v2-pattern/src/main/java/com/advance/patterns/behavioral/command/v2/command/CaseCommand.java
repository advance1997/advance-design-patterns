package com.advance.patterns.behavioral.command.v2.command;

/**
 * 可由调用者统一提交和触发的催收运营命令。
 *
 * @author advance
 * @since 2026-10-29 00:20:42
 */
public interface CaseCommand {
    /**
     * 获取命令编号。
     *
     * @return 命令编号
     */
    String getCommandId();

    /**
     * 执行命令封装的操作。
     */
    void execute();
}
