package com.advance.patterns.behavioral.state.v4.outbox;

import com.advance.patterns.behavioral.state.v4.domain.OutboxRecord;
import org.springframework.stereotype.Component;

/**
 * 教学消息终点，仅打印和单线程计数，不发送到 MQ、不调用真实外部系统。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
@Component
public class MockEventPublisher {

    /** 当前应用实例的模拟发送次数，不是生产可靠投递计数。 */
    private int sentCount;

    /**
     * 打印完整迁移事实，供学习者观察事务后投递。
     *
     * @param record 待投递事件
     */
    public void send(OutboxRecord record) {
        sentCount++;
        System.out.println("[状态/v4] MockEventPublisher.send ---> 模拟投递 eventId=" + record.getEventId()
                + "，planId=" + record.getPlanId() + "，" + record.getFromStatus() + " → " + record.getToStatus());
    }

    /**
     * 获取本实例模拟发送次数。
     *
     * @return 模拟发送次数
     */
    public int getSentCount() {
        return sentCount;
    }
}
