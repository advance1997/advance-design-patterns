package com.advance.patterns.comprehensive.material.v1;

/** 分离对目录树节点执行的业务操作。 @author advance @since 2026-10-30 12:00:00 */
public interface MaterialVisitor {
    /** @param folder 被访问目录 */ void visit(MaterialFolder folder);
    /** @param file 被访问文件 */ void visit(MaterialFile file);
}
