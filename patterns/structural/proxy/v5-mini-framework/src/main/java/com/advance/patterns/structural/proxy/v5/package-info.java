/**
 * 代理模式专题的v5-mini-framework实践版本。
 *
 * <p>业务场景：已读取《01-1｜设计模式专项》阶段 3「代理模式」原文： 会话 `01a0ba95-6029-74e0-a6e2-c68184b4d5db`， 大纲回合 `01a0ba95-6670-7ed0-8d27-9b2279bc770d`。 课程链路为 Class → 反射调用 → 静态代理 → JDK 动态代理 → CGLIB → Byte Buddy 基本思想 → Spring AOP → RPC Stub。 本次按用户指定聚焦事务场景、Spring 事务代理和自制简化事务框架： V1 观察反射，V2 静态代理，V3 JDK 动态代理，V4 对照 Spring 的 JDK/CGLIB 事务， V5 实现教学框架。Byte Buddy 与 RPC Stub 在本文说明边界，未</p>
 *
 * <p>本版本实现要点：MiniTransactionAutoProxyCreator；自定义注解 + Spring 后处理器 + JDK 代理 + 自制事务管理器。代理在客户端与目标对象之间控制访问，并可承载事务等横切边界。</p>
 *
 * <p>具体调用路径请参见本版本目录下的流程图与时序图文档。</p>
 *
 * @author advance
 * @since 2026-10-29 15:47:52
 */
package com.advance.patterns.structural.proxy.v5;
