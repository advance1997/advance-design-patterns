package com.advance.patterns.behavioral.chainofresponsibility.v3.chain;

import com.advance.patterns.behavioral.chainofresponsibility.v3.domain.OutboundRequest;

/**
 * 负责案件操作权限的规则节点，通过才传递，拒绝即停止。
 *
 * @author advance
 * @since 2026-10-28 16:42:38
 */
public class PermissionHandler implements RuleHandler {

    /**
     * 执行当前检查，并打印传递前与返回后的路径。
     *
     * @param request 当前虚构申请
     * @param chain 当前请求专用责任链
     * @return 当前拒绝结果或后续节点返回的结果
     */
    @Override
    public String handle(OutboundRequest request, RuleChain chain) {
        System.out.println("[责任链/v3] PermissionHandler.handle ---> 进入节点，检查案件操作权限");
        if (!request.isPermitted()) {
            System.out.println("[责任链/v3] PermissionHandler.handle ---> 拒绝，不调用 RuleChain.proceed");
            return "拒绝：无案件操作权限";
        }
        System.out.println("[责任链/v3] PermissionHandler.handle ---> 放行，调用 RuleChain.proceed");
        String result = chain.proceed(request);
        System.out.println("[责任链/v3] PermissionHandler.handle ---> 后续处理返回，退出当前节点");
        return result;
    }
}
