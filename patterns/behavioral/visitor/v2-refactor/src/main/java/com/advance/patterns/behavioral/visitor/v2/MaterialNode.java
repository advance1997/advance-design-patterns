package com.advance.patterns.behavioral.visitor.v2;

/**
 * 催收诉讼材料节点的标记接口，仅表示节点身份，不承载统计或校验业务。
 *
 * <p>v2 将操作移到独立 Service，但服务需要通过 {@code instanceof} 判断具体节点类型。</p>
 *
 * @author advance
 * @since 2026-10-30 00:13:24
 */
public interface MaterialNode {
}
