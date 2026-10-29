/**
 * 访问者模式的完整双重分派版本。
 *
 * <p>元素为材料目录、PDF 与录音；操作为统计及校验。元素通过 {@code accept(visitor)} 动态分派到具体元素，
 * 再以 {@code visitor.visit(this)} 令编译器依据 {@code this} 的静态类型选择重载，并由运行时访问者类型执行实现。</p>
 *
 * <p>目录仍是组合节点，递归访问子节点时使用 JDK 增强 for 所依赖的迭代器。统计和校验访问者均持有单次操作状态，
 * 每次任务应新建实例，不可直接作为并发共享的有状态单例。</p>
 *
 * @author advance
 * @since 2026-10-30 00:13:24
 */
package com.advance.patterns.behavioral.visitor.v3;
