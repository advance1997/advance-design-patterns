/**
 * 访问者模式专题的 v1-naive 基线版本。
 *
 * <p>场景是虚构催收诉讼材料目录，包含 PDF、录音和子目录。材料节点直接承担统计与校验行为，
 * 目录负责递归委托给子节点。本版本展示对象结构与操作耦合后，增加一种操作需要修改元素接口和各节点类。</p>
 *
 * @author advance
 * @since 2026-10-30 00:13:24
 */
package com.advance.patterns.behavioral.visitor.v1;
