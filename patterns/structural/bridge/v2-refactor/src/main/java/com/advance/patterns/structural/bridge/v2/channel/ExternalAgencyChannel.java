package com.advance.patterns.structural.bridge.v2.channel;

/**
 * 外部机构材料输出渠道模拟。
 *
 * @author advance
 * @since 2026-10-28 22:05:00
 */
public class ExternalAgencyChannel {
    /**
     * 模拟将材料包交付给外部合作机构。
     *
     * @param content 材料包文本
     * @return 外部渠道处理结果
     */
    public String send(String content) {
        System.out.println("[桥接/v2] ExternalAgencyChannel.send ---> 模拟外部合作机构输出");
        return "外部机构：" + content;
    }
}
