package com.advance.patterns.comprehensive.material.v1;

/**
 * 表示一次材料归集请求。
 *
 * @author advance
 * @since 2026-10-30 12:00:00
 */
public class MaterialRequest {
    /** 租户标识。 */
    private final String tenantId;
    /** 业务侧提供的幂等请求键。 */
    private final String requestKey;
    /** 案件标识。 */
    private final String caseId;
    /** 归集场景，例如 LITIGATION 或 ARBITRATION。 */
    private final String scene;

    /**
     * 创建归集请求。
     *
     * @param tenantId 租户标识
     * @param requestKey 同一业务请求重试时保持不变的幂等键
     * @param caseId 虚构案件标识
     * @param scene 材料归集场景
     */
    public MaterialRequest(String tenantId, String requestKey, String caseId, String scene) {
        this.tenantId = tenantId;
        this.requestKey = requestKey;
        this.caseId = caseId;
        this.scene = scene;
    }

    /** @return 租户标识 */
    public String getTenantId() { return tenantId; }
    /** @return 幂等请求键 */
    public String getRequestKey() { return requestKey; }
    /** @return 案件标识 */
    public String getCaseId() { return caseId; }
    /** @return 归集场景 */
    public String getScene() { return scene; }
}
