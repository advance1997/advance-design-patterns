package com.advance.patterns.comprehensive.material.v1;

/**
 * 描述需要从某个材料来源获取的一项材料。
 *
 * @author advance
 * @since 2026-10-30 12:00:00
 */
public class MaterialRequirement {
    /** 来源路由类型。 */
    private final String type;
    /** 外部系统中的模拟查询键。 */
    private final String sourceKey;
    /** 材料在归集目录中的显示名称。 */
    private final String fileName;
    /** 目录名称。 */
    private final String folderName;
    /** 未获取到时是否阻止完成打包。 */
    private final boolean required;

    /**
     * 创建材料要求。
     *
     * @param type 来源类型
     * @param sourceKey 来源查询键
     * @param fileName 文件显示名称
     * @param folderName 目标目录
     * @param required 是否必需
     */
    public MaterialRequirement(String type, String sourceKey, String fileName, String folderName, boolean required) {
        this.type = type;
        this.sourceKey = sourceKey;
        this.fileName = fileName;
        this.folderName = folderName;
        this.required = required;
    }

    /** @return 来源类型 */
    public String getType() { return type; }
    /** @return 来源查询键 */
    public String getSourceKey() { return sourceKey; }
    /** @return 文件显示名称 */
    public String getFileName() { return fileName; }
    /** @return 目标目录 */
    public String getFolderName() { return folderName; }
    /** @return 是否为必需材料 */
    public boolean isRequired() { return required; }
}
