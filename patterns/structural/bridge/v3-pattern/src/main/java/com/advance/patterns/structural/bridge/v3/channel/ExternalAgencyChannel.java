package com.advance.patterns.structural.bridge.v3.channel;

/**
 * 外部机构材料输出渠道模拟。
 *
 * @author advance
 * @since 2026-10-28 22:05:00
 */
public class ExternalAgencyChannel {
    public String send(String content){System.out.println("[桥接/v3] ExternalAgencyChannel.send ---> 外部机构渠道输出");return "外部:" + content;}
}
