package com.advance.patterns.behavioral.state.v4.outbox;

import com.advance.patterns.behavioral.state.v4.domain.OutboxRecord;
import com.advance.patterns.behavioral.state.v4.mapper.OutboxMapper;
import org.springframework.stereotype.Service;

/**
 * 单工作者、手动触发的教学投递器；先模拟发送后标记，不提供恰好一次或并发抢占能力。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
@Service
public class OutboxDispatcher {

    /** 已提交 Outbox 记录的查询入口。 */
    private final OutboxMapper mapper;

    /** 不具备真实发送能力的模拟发布者。 */
    private final MockEventPublisher publisher;

    /**
     * 注入待投递记录访问对象与模拟消息终点。
     *
     * @param mapper 事件映射接口
     * @param publisher 模拟消息发布者
     */
    public OutboxDispatcher(OutboxMapper mapper, MockEventPublisher publisher) {
        this.mapper = mapper;
        this.publisher = publisher;
    }

    /**
     * 读取已提交待投递事件，逐条模拟发送并标记。
     * 发送后若标记前发生故障，再次执行可能重复发送，生产消费者需另行保证幂等。
     *
     * @return 本次成功标记的事件数
     */
    public int dispatchPending() {
        int delivered = 0;
        for (OutboxRecord record : mapper.findPending()) {
            publisher.send(record);
            delivered += mapper.markSent(record.getEventId());
        }
        System.out.println("[状态/v4] OutboxDispatcher.dispatchPending ---> 本次标记 SENT 数量=" + delivered);
        return delivered;
    }
}
