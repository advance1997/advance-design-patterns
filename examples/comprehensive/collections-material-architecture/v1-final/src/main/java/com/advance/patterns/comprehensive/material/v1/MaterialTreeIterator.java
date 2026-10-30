package com.advance.patterns.comprehensive.material.v1;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

/** 以前序方式统一遍历目录树全部节点。 @author advance @since 2026-10-30 12:00:00 */
public class MaterialTreeIterator implements Iterator<MaterialNode> {
    private final Deque<Iterator<MaterialNode>> path = new ArrayDeque<Iterator<MaterialNode>>();
    private MaterialNode next;
    /** @param root 目录树根节点 */
    public MaterialTreeIterator(MaterialFolder root) { path.push(Collections.<MaterialNode>singletonList(root).iterator()); }
    @Override public boolean hasNext() {
        while (next == null && !path.isEmpty()) {
            Iterator<MaterialNode> current = path.peek();
            if (!current.hasNext()) { path.pop(); continue; }
            next = current.next();
            if (next instanceof MaterialFolder) { path.push(((MaterialFolder) next).getChildren().iterator()); }
        }
        return next != null;
    }
    @Override public MaterialNode next() {
        if (!hasNext()) { throw new NoSuchElementException("材料目录已遍历完成"); }
        MaterialNode current = next; next = null; return current;
    }
}
