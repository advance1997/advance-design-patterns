package com.advance.patterns.comprehensive.material.v1;

/** 组合目录树中统一的文件或目录抽象。 @author advance @since 2026-10-30 12:00:00 */
public interface MaterialNode {
    /** 接受访问者处理当前节点。 @param visitor 目录树操作访问者 */ void accept(MaterialVisitor visitor);
}
