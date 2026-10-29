/**
 * 工厂与 IOC 容器专题的v3-pattern实践版本。
 *
 * <p>业务场景：依据《01-1｜设计模式专项》阶段 2 大纲，会话 `01a0ba95-6029-74e0-a6e2-c68184b4d5db`， 大纲回合 `01a0ba95-6670-7ed0-8d27-9b2279bc770d`。 按用户的新要求，将课程设计要点改编为催收金融业务；下述类名、数据和细节是 本仓库工程化补充，不宣称是课程原始案例。 所有案件编号、配置和流程均为虚构。只模拟人工复核，不发送短信、不外呼， 不查询真实客户资料、不决定信用待遇或采取法律行动。教学参数不代表真实监管规则。 采用 Java 8、普通类与直接方法调用。每个版本独立持有源码和测试， 不共享业务类，不依赖其他版本。Javadoc 使用作者 advance 和上海时区创建时间。 测试仅由 JUnit 发</p>
 *
 * <p>本版本实现要点：AgencyClientFactoryBean + ObjectFactory&amp;lt;ReviewSession&amp;gt;；区分产品工厂、延迟获取及 prototype 资源责任。工厂与 IOC 示例关注对象创建、依赖注入及容器管理边界。</p>
 *
 * <p>具体调用路径请参见本版本目录下的流程图与时序图文档。</p>
 *
 * @author advance
 * @since 2026-10-29 15:47:52
 */
package com.advance.patterns.creational.factoryioc.v3;
