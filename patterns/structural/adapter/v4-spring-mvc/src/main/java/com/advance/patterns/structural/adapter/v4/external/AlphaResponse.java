package com.advance.patterns.structural.adapter.v4.external;

/**
 * 模拟平台 Alpha 的专有响应，业务层不应长期依赖这些字段。
 *
 * @author advance
 * @since 2026-10-28 16:08:56
 */
public final class AlphaResponse {

    /** 平台状态码：0000 成功，4040 未找到。 */
    private final String code;

    /** 平台虚构文件令牌；未找到时为空字符串。 */
    private final String fileToken;

    /**
     * 创建模拟平台响应。
     *
     * @param code 平台状态码：0000 成功，4040 未找到
     * @param fileToken 平台虚构文件令牌；未找到时为空字符串
     */
    public AlphaResponse(String code, String fileToken) {
        this.code = code;
        this.fileToken = fileToken;
    }

    /**
     * 获取平台状态码：0000 成功，4040 未找到。
     *
     * @return 平台状态码：0000 成功，4040 未找到
     */
    public String getCode() {
        return code;
    }

    /**
     * 获取平台虚构文件令牌；未找到时为空字符串。
     *
     * @return 平台虚构文件令牌；未找到时为空字符串
     */
    public String getFileToken() {
        return fileToken;
    }
}
