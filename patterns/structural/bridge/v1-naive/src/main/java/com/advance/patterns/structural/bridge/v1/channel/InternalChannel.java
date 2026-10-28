package com.advance.patterns.structural.bridge.v1.channel;

/**
 * 内部运营材料输出渠道模拟。
 *
 * @author advance
 * @since 2026-10-28 22:05:00
 */
public class InternalChannel {
    /**
     * 模拟将材料包交付给内部运营。
     *
     * @param content 材料包文本
     * @return 内部渠道处理结果
     */
    public String send(String content) {
        System.out.println("[桥接/v1] InternalChannel.send ---> 模拟内部运营输出");
        return "内部运营：" + content;
    }
}
