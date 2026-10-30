package com.advance.patterns.comprehensive.material.v1;

import java.util.ArrayList;
import java.util.List;

/** 输出材料目录清单，不改变 Composite 节点结构。 @author advance @since 2026-10-30 12:00:00 */
public class ManifestVisitor implements MaterialVisitor {
    private final List<String> lines = new ArrayList<String>();
    @Override public void visit(MaterialFolder folder) { lines.add("目录：" + folder.getName()); }
    @Override public void visit(MaterialFile file) {
        lines.add("文件：" + file.getFolderName() + "/" + file.getName() + "，大小=" + file.getSize()
                + "字节，来源=" + file.getSourceType());
    }
    /** @return 清单行副本 */ public List<String> getLines() { return new ArrayList<String>(lines); }
}
