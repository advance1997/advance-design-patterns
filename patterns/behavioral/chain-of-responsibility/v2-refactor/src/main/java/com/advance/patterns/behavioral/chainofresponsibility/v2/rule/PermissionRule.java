package com.advance.patterns.behavioral.chainofresponsibility.v2.rule;

import com.advance.patterns.behavioral.chainofresponsibility.v2.domain.OutboundRequest;

/**
 * 只负责案件操作权限的独立检查，不持有下一规则，也不控制链式传递。
 *
 * @author advance
 * @since 2026-10-28 16:42:38
 */
public class PermissionRule {

    /**
     * 检查案件操作权限，不调用后续规则。
     *
     * @param request 虚构外呼申请
     * @return 当前规则是否通过
     */
    public boolean allows(OutboundRequest request) {
        System.out.println("[责任链/v2] PermissionRule.allows ---> 检查案件操作权限");
        return request.isPermitted();
    }
}
