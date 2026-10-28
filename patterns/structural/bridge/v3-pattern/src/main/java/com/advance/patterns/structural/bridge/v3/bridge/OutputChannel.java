package com.advance.patterns.structural.bridge.v3.bridge;

/**
 * 实现维度接口；材料包抽象不依赖具体输出渠道。
 *
 * @author advance
 * @since 2026-10-28 22:05:00
 */
public interface OutputChannel {
    /**
     * 接收材料包文本并执行模拟输出。
     *
     * @param content 材料包文本
     * @return 渠道处理后的说明文本
     */
    String send(String content);
}
