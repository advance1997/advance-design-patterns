package com.advance.patterns.comprehensive.material.v1;

/**
 * 归一化不同外部材料系统的查询协议。
 *
 * @author advance
 * @since 2026-10-30 12:00:00
 */
public interface MaterialSource {
    /**
     * 按材料要求读取文件。
     *
     * @param requirement 材料要求
     * @return FOUND、MISSING 或 ERROR，不以异常混淆业务缺失和临时故障
     */
    FetchResult fetch(MaterialRequirement requirement);
}
