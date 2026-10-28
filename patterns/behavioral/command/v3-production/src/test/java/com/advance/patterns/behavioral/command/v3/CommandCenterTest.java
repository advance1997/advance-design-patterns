package com.advance.patterns.behavioral.command.v3;

import com.advance.patterns.behavioral.command.v3.dispatcher.CommandDispatcher;
import com.advance.patterns.behavioral.command.v3.handler.CommandHandler;
import com.advance.patterns.behavioral.command.v3.handler.PauseCallHandler;
import com.advance.patterns.behavioral.command.v3.handler.ReassignHandler;
import com.advance.patterns.behavioral.command.v3.receiver.CaseActionService;
import com.advance.patterns.behavioral.command.v3.record.CommandRecord;
import com.advance.patterns.behavioral.command.v3.repository.InMemoryCommandRepository;
import com.advance.patterns.behavioral.command.v3.service.CommandSubmitService;
import com.advance.patterns.behavioral.command.v3.worker.CommandWorker;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

/**
 * 内存模拟催收运营命令中心的提交、异步执行和状态记录。
 *
 * @author advance
 * @since 2026-10-29 00:20:42
 */
public class CommandCenterTest {
    /**
     * 期望：提交时三条操作均为 PENDING；Worker 执行后两条 SUCCESS、一条 FAILED，案件状态只体现成功动作。
     */
    @Test
    public void shouldSubmitAndExecuteCommandsWithStatusTracking() {
        CaseActionService caseActionService = new CaseActionService();
        InMemoryCommandRepository repository = new InMemoryCommandRepository();
        CommandSubmitService submitService = new CommandSubmitService(repository);
        CommandHandler pauseHandler = new PauseCallHandler(caseActionService);
        CommandHandler reassignHandler = new ReassignHandler(caseActionService);
        CommandDispatcher dispatcher = new CommandDispatcher(
                Arrays.asList(pauseHandler, reassignHandler));
        CommandWorker worker = new CommandWorker(repository, dispatcher);

        System.out.println("期望：提交后业务尚未执行，命令状态均为 PENDING");
        submitService.submitPauseCall("CMD-101", 10001L, "投诉核查");
        submitService.submitReassign("CMD-102", 10002L, "USER_C");
        submitService.submitReassign("CMD-103", 10002L, " ");

        System.out.println("提交后的命令记录：");
        printRecord(repository.findById("CMD-101"));
        printRecord(repository.findById("CMD-102"));
        printRecord(repository.findById("CMD-103"));
        System.out.println("执行前案件：" + caseActionService.stateOf(10001L));

        worker.runOnce();

        System.out.println("执行后的命令记录（期望 SUCCESS、SUCCESS、FAILED）：");
        printRecord(repository.findById("CMD-101"));
        printRecord(repository.findById("CMD-102"));
        printRecord(repository.findById("CMD-103"));
        System.out.println("执行后案件：" + caseActionService.stateOf(10001L));
        System.out.println("执行后案件：" + caseActionService.stateOf(10002L));
    }

    private void printRecord(CommandRecord record) {
        System.out.println("命令记录：" + record);
    }
}
