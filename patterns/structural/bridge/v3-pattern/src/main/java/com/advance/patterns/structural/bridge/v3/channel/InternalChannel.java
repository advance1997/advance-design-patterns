package com.advance.patterns.structural.bridge.v3.channel;

/**
 * 内部运营材料输出渠道模拟。
 *
 * @author advance
 * @since 2026-10-28 22:05:00
 */
public class InternalChannel {
    public String send(String content){System.out.println("[桥接/v3] InternalChannel.send ---> 内部运营渠道输出");return "内部:" + content;}
}
