package com.advance.patterns.structural.adapter.v4.web;

import com.advance.patterns.structural.adapter.v4.domain.MaterialFile;
import com.advance.patterns.structural.adapter.v4.service.MaterialCollectionService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.HttpRequestHandler;

/**
 * 实现 HttpRequestHandler 的教学入口，由 Spring MVC 的 HttpRequestHandlerAdapter 调用。
 *
 * @author advance
 * @since 2026-10-28 16:08:56
 */
public class BetaMaterialHttpHandler implements HttpRequestHandler {

    /** 已由 Spring 注入平台适配器的归集服务。 */
    private final MaterialCollectionService service;

    /**
     * 注入当前入口使用的归集服务。
     *
     * @param service 合同材料归集服务
     */
    public BetaMaterialHttpHandler(MaterialCollectionService service) {
        this.service = service;
    }

    /**
     * 读取教学案件编号并直接写出统一结果，不解析任何平台专有响应。
     *
     * @param request 携带正整数 caseId 的模拟请求
     * @param response 输出 UTF-8 纯文本的响应
     * @throws IOException 响应写出失败时抛出
     */
    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        System.out.println("[适配器/v4] BetaMaterialHttpHandler.handleRequest ---> 接收 MVC 适配后的统一调度");
        long caseId = Long.parseLong(request.getParameter("caseId"));
        MaterialFile material = service.collectContract(caseId);
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write(material.toString());
    }
}
