package com.advance.patterns.behavioral.command.v1;

import com.advance.patterns.behavioral.command.v1.service.CaseActionService;
import com.advance.patterns.behavioral.command.v1.service.LegacyCommandService;
import org.junit.jupiter.api.Test;

/**
 * v1 直接按操作类型同步执行催收运营动作。
 *
 * @author advance
 * @since 2026-10-29 00:20:42
 */
public class CommandTest {
    /**
     * 期望：暂停外呼和重新分案立即执行，调用方依赖操作类型与参数含义。
     */
    @Test
    public void shouldExecuteOperationSynchronouslyByType() {
        CaseActionService receiver = new CaseActionService();
        LegacyCommandService service = new LegacyCommandService(receiver);

        System.out.println("期望：PAUSE_CALL 与 REASSIGN 在请求调用中同步执行");
        service.execute("PAUSE_CALL", 10001L, "投诉核查");
        service.execute("REASSIGN", 10002L, "USER_C");
        System.out.println("案件一：" + receiver.stateOf(10001L));
        System.out.println("案件二：" + receiver.stateOf(10002L));
    }
}
