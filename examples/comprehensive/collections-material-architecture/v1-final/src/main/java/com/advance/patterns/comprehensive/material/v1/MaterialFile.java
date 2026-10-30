package com.advance.patterns.comprehensive.material.v1;

/** 已归集文件及其来源信息。 @author advance @since 2026-10-30 12:00:00 */
public class MaterialFile implements MaterialNode {
    private final String name; private final String folderName; private final long size; private final String sourceType;
    /** @param name 文件名 @param folderName 目录名 @param size 文件大小（字节） @param sourceType 来源系统类型 */
    public MaterialFile(String name, String folderName, long size, String sourceType) {
        this.name = name; this.folderName = folderName; this.size = size; this.sourceType = sourceType;
    }
    /** @return 文件名 */ public String getName() { return name; }
    /** @return 目录名 */ public String getFolderName() { return folderName; }
    /** @return 文件大小（字节） */ public long getSize() { return size; }
    /** @return 来源系统类型 */ public String getSourceType() { return sourceType; }
    @Override public void accept(MaterialVisitor visitor) { visitor.visit(this); }
}
