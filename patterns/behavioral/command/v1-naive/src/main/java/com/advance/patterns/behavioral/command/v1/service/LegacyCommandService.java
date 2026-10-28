package com.advance.patterns.behavioral.command.v1.service;

/**
 * v1 通过操作类型分支同步调用不同业务动作。
 *
 * @author advance
 * @since 2026-10-29 00:20:42
 */
public class LegacyCommandService {
    private final CaseActionService caseActionService;

    /**
     * 创建同步操作服务。
     *
     * @param caseActionService 案件业务接收者
     */
    public LegacyCommandService(CaseActionService caseActionService) {
        this.caseActionService = caseActionService;
    }

    /**
     * 按类型同步执行业务动作。
     *
     * @param type 操作类型
     * @param caseId 案件编号
     * @param argument 操作参数
     */
    public void execute(String type, long caseId, String argument) {
        System.out.println("[命令/v1] LegacyCommandService.execute ---> 根据操作类型选择业务逻辑");
        if ("PAUSE_CALL".equals(type)) {
            caseActionService.pauseCall(caseId, argument);
        } else if ("REASSIGN".equals(type)) {
            caseActionService.reassign(caseId, argument);
        } else {
            throw new IllegalArgumentException("不支持的操作类型：" + type);
        }
    }
}
