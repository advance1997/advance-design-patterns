package com.advance.patterns.behavioral.command.v2;

import com.advance.patterns.behavioral.command.v2.command.PauseCallCommand;
import com.advance.patterns.behavioral.command.v2.command.ReassignCommand;
import com.advance.patterns.behavioral.command.v2.command.ResumeCallCommand;
import com.advance.patterns.behavioral.command.v2.invoker.CommandInvoker;
import com.advance.patterns.behavioral.command.v2.receiver.CaseActionService;
import org.junit.jupiter.api.Test;

/**
 * 经典 GoF 命令模式：命令对象封装操作、参数和接收者。
 *
 * @author advance
 * @since 2026-10-29 00:20:42
 */
public class CommandTest {
    /**
     * 期望：提交后案件状态不变；队列执行后按顺序暂停、重新分案并恢复外呼。
     */
    @Test
    public void shouldQueueCommandsAndExecuteLater() {
        CaseActionService receiver = new CaseActionService();
        CommandInvoker invoker = new CommandInvoker();
        System.out.println("期望：提交只将命令排队，不立即改变案件状态");
        invoker.submit(new PauseCallCommand("CMD-001", receiver, 10001L, "投诉核查"));
        invoker.submit(new ReassignCommand("CMD-002", receiver, 10002L, "USER_C"));
        invoker.submit(new ResumeCallCommand("CMD-003", receiver, 10001L));

        System.out.println("执行前案件状态：");
        System.out.println("执行前：" + receiver.stateOf(10001L));
        System.out.println("执行前：" + receiver.stateOf(10002L));
        invoker.executeAll();
        System.out.println("执行后：" + receiver.stateOf(10001L));
        System.out.println("执行后：" + receiver.stateOf(10002L));
    }
}
