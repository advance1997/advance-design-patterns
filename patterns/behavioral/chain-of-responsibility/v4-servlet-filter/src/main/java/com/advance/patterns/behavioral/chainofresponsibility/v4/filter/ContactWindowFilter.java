package com.advance.patterns.behavioral.chainofresponsibility.v4.filter;

import com.advance.patterns.behavioral.chainofresponsibility.v4.domain.OutboundRequest;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * 使用标准 Servlet Filter 检查教学时间窗口 [9,18)，以是否调用 chain.doFilter 控制后续处理。
 *
 * @author advance
 * @since 2026-10-28 16:42:38
 */
public class ContactWindowFilter implements Filter {

    /**
     * 读取测试注入的可信上下文，拒绝时直接写响应并返回。
     * 放行时只调用一次 chain.doFilter，随后按调用栈顺序返回。
     *
     * @param request 带有 outboundRequest 属性的模拟请求
     * @param response HTTP 响应
     * @param chain Servlet 框架提供的后续调用链
     * @throws IOException 响应写入或后续处理发生 I/O 错误时抛出
     * @throws ServletException 缺少教学上下文或后续 Servlet 执行失败时抛出
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("[责任链/v4] ContactWindowFilter.doFilter ---> 进入节点，检查教学时间窗口 [9,18)");
        Object attribute = request.getAttribute("outboundRequest");
        if (!(attribute instanceof OutboundRequest)) {
            throw new ServletException("缺少可信的 outboundRequest 教学上下文");
        }
        OutboundRequest outbound = (OutboundRequest) attribute;
        if (outbound.getHour() < 9 || outbound.getHour() >= 18) {
            System.out.println("[责任链/v4] ContactWindowFilter.doFilter ---> 拒绝，不调用 FilterChain.doFilter");
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.setContentType("text/plain;charset=UTF-8");
            httpResponse.getWriter().write("拒绝：不在教学时间窗口");
            return;
        }
        System.out.println("[责任链/v4] ContactWindowFilter.doFilter ---> 放行，调用 FilterChain.doFilter");
        chain.doFilter(request, response);
        System.out.println("[责任链/v4] ContactWindowFilter.doFilter ---> 后续处理返回，退出当前节点");
    }
}
