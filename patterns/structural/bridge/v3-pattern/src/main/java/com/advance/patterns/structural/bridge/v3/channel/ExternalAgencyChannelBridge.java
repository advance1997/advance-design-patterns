package com.advance.patterns.structural.bridge.v3.channel;

import com.advance.patterns.structural.bridge.v3.bridge.OutputChannel;

/**
 * 外部机构输出实现维度。
 *
 * @author advance
 * @since 2026-10-28 22:05:00
 */
public class ExternalAgencyChannelBridge implements OutputChannel {
    @Override
    public String send(String content) {
        System.out.println("[桥接/v3] ExternalAgencyChannelBridge.send ---> 模拟外部合作机构渠道输出");
        return "外部机构：" + content;
    }
}
