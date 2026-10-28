package com.advance.patterns.behavioral.state.v4.web;

import com.advance.patterns.behavioral.state.v4.outbox.OutboxDispatcher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 仅供本地手动观察 Outbox 的教学入口，不应直接暴露为生产管理接口。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
@RestController
public class DemoOutboxController {

    /** 单工作者教学投递器。 */
    private final OutboxDispatcher dispatcher;

    /**
     * 注入模拟投递服务。
     *
     * @param dispatcher 只发送到内存打印终点的投递器
     */
    public DemoOutboxController(OutboxDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    /**
     * 手动投递已提交事件，返回标记数量。
     *
     * @return 本次标记为 SENT 的数量
     */
    @PostMapping("/demo/outbox/dispatch")
    public int dispatch() {
        return dispatcher.dispatchPending();
    }
}
