package com.advance.patterns.behavioral.state.v4.web;

import com.advance.patterns.behavioral.state.v4.domain.SettlementRecord;
import com.advance.patterns.behavioral.state.v4.domain.TransitionResult;
import com.advance.patterns.behavioral.state.v4.service.SettlementService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前端只提交事件与旧版本；实际状态迁移委托事务服务，不能直接更新 status。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
@RestController
@RequestMapping("/settlements")
public class SettlementController {

    /** 由 Spring 代理的事务服务。 */
    private final SettlementService service;

    /**
     * 注入状态迁移服务。
     *
     * @param service 具备事务边界的服务
     */
    public SettlementController(SettlementService service) {
        this.service = service;
    }

    /**
     * 读取方案当前状态和乐观锁版本。
     *
     * @param id 虚构方案编号
     * @return 当前方案快照
     */
    @GetMapping("/{id}")
    public SettlementRecord find(@PathVariable("id") long id) {
        return service.find(id);
    }

    /**
     * 从 HTTP 入口发起一个业务事件。
     *
     * @param id 虚构方案编号
     * @param command 客户端事件及预期版本
     * @return 提交成功后的状态、版本与 Outbox 事件编号
     */
    @PostMapping("/{id}/events")
    public TransitionResult handle(@PathVariable("id") long id, @RequestBody TransitionCommand command) {
        System.out.println("[状态/v4] SettlementController.handle ---> 接收 event=" + command.getEvent()
                + "，expectedVersion=" + command.getExpectedVersion());
        return service.handle(id, command.getEvent(), command.getExpectedVersion());
    }
}
