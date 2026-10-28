package com.advance.patterns.structural.bridge.v3.channel;

import com.advance.patterns.structural.bridge.v3.bridge.OutputChannel;

/**
 * 内部运营输出实现维度。
 *
 * @author advance
 * @since 2026-10-28 22:05:00
 */
public class InternalChannelBridge implements OutputChannel { public String send(String content){System.out.println("[桥接/v3] InternalChannelBridge.send ---> 内部运营渠道输出");return "内部运营:"+content;} }
