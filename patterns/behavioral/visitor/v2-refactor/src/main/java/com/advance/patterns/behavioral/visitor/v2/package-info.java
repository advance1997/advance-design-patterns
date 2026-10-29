/**
 * 访问者模式专题的 v2-refactor 过渡版本。
 *
 * <p>业务场景与 v1 相同，仍处理虚构诉讼材料目录中的 PDF 和录音。元素只保存数据，统计和校验分别由
 * {@code MaterialStatisticsService}、{@code MaterialValidationService} 承担；两个服务都通过
 * {@code instanceof} 分辨目录、PDF、录音。操作与数据虽已分离，但新增节点类型仍需修改每个服务。</p>
 *
 * @author advance
 * @since 2026-10-30 00:13:24
 */
package com.advance.patterns.behavioral.visitor.v2;
