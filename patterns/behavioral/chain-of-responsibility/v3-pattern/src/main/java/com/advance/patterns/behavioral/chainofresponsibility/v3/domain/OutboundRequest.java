package com.advance.patterns.behavioral.chainofresponsibility.v3.domain;

/**
 * 模拟外呼申请快照；权限与小时均由测试构造，不接受真实用户资料，不代表生产授权。
 *
 * @author advance
 * @since 2026-10-28 16:42:38
 */
public final class OutboundRequest {

    /** 虚构案件编号。 */
    private final String caseId;

    /** 模拟可信权限检查结果，不应由真实前端自行声明。 */
    private final boolean permitted;

    /** 固定教学时间，范围为 0 至 23，不读取系统时钟。 */
    private final int hour;

    /**
     * 创建可重复验证的教学请求。
     *
     * @param caseId 虚构案件编号
     * @param permitted 模拟的案件操作权限结果
     * @param hour 教学小时，范围为 0 至 23
     * @throws IllegalArgumentException 小时超出有效范围时抛出
     */
    public OutboundRequest(String caseId, boolean permitted, int hour) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("hour 必须处于 0 至 23");
        }
        this.caseId = caseId;
        this.permitted = permitted;
        this.hour = hour;
    }

    /**
     * 获取虚构案件编号。
     *
     * @return 案件编号
     */
    public String getCaseId() {
        return caseId;
    }

    /**
     * 获取模拟权限结果。
     *
     * @return 有模拟权限时为 true
     */
    public boolean isPermitted() {
        return permitted;
    }

    /**
     * 获取固定教学小时。
     *
     * @return 0 至 23 范围的小时
     */
    public int getHour() {
        return hour;
    }
}
