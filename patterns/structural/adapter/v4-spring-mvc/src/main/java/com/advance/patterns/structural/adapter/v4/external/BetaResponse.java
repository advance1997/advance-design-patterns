package com.advance.patterns.structural.adapter.v4.external;

/**
 * 模拟平台 Beta 的专有响应，命名和状态语义与 Alpha 不同。
 *
 * @author advance
 * @since 2026-10-28 16:08:56
 */
public final class BetaResponse {

    /** 平台是否存在合同材料。 */
    private final boolean exists;

    /** 平台虚构对象存储标识；未找到时为空字符串。 */
    private final String objectKey;

    /**
     * 创建模拟平台响应。
     *
     * @param exists 平台是否存在合同材料
     * @param objectKey 平台虚构对象存储标识；未找到时为空字符串
     */
    public BetaResponse(boolean exists, String objectKey) {
        this.exists = exists;
        this.objectKey = objectKey;
    }

    /**
     * 获取平台是否存在合同材料。
     *
     * @return 平台是否存在合同材料
     */
    public boolean isExists() {
        return exists;
    }

    /**
     * 获取平台虚构对象存储标识；未找到时为空字符串。
     *
     * @return 平台虚构对象存储标识；未找到时为空字符串
     */
    public String getObjectKey() {
        return objectKey;
    }
}
