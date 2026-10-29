package com.advance.patterns.behavioral.visitor.v3;

/**
 * 可接受材料访问者的元素协议。
 *
 * <p>具体元素通过 {@link #accept(MaterialVisitor)} 完成第一次动态分派，再以自身静态类型调用对应的 visit 重载。</p>
 *
 * @author advance
 * @since 2026-10-30 00:13:24
 */
public interface MaterialNode {
    /**
     * 将当前元素交给访问者处理。
     *
     * @param visitor 本次操作的访问者
     */
    void accept(MaterialVisitor visitor);
}
