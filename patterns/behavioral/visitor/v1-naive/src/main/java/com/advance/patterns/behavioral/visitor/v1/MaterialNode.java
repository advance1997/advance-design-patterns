package com.advance.patterns.behavioral.visitor.v1;

import java.util.List;

/**
 * 诉讼材料元素基线接口，直接声明统计和校验等业务操作。
 *
 * <p>节点类型与针对节点的业务操作放在同一抽象中。增加操作时，目录、PDF、录音等节点都需要同步扩展，
 * 用于观察访问者模式出现前的数据结构与业务行为耦合问题。</p>
 *
 * @author advance
 * @since 2026-10-30 00:13:24
 */
public interface MaterialNode {
    /**
     * 将当前节点及其子节点纳入材料统计。
     *
     * @param statistics 本次统计使用的可变结果对象
     */
    void collectStatistics(MaterialStatistics statistics);

    /**
     * 校验当前节点及其子节点，并将发现的问题追加到指定集合。
     *
     * @param issues 本次校验发现的问题集合
     */
    void validate(List<String> issues);
}
