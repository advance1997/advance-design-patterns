package com.advance.patterns.structural.bridge.v3.bridge;

/**
 * 实现维度接口；材料包抽象不依赖具体输出渠道。
 *
 * @author advance
 * @since 2026-10-28 22:05:00
 */
public interface OutputChannel { String send(String content); }
