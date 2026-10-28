package com.advance.patterns.structural.adapter.v4.web;

import com.advance.patterns.structural.adapter.v4.domain.MaterialFile;
import com.advance.patterns.structural.adapter.v4.service.MaterialCollectionService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * 实现 Controller 的教学入口，由 Spring MVC 的 SimpleControllerHandlerAdapter 调用。
 *
 * @author advance
 * @since 2026-10-28 16:08:56
 */
public class AlphaMaterialController implements Controller {

    /** 已由 Spring 注入平台适配器的归集服务。 */
    private final MaterialCollectionService service;

    /**
     * 注入当前入口使用的归集服务。
     *
     * @param service 合同材料归集服务
     */
    public AlphaMaterialController(MaterialCollectionService service) {
        this.service = service;
    }

    /**
     * 读取教学案件编号并直接写出统一结果，不解析任何平台专有响应。
     *
     * @param request 携带正整数 caseId 的模拟请求
     * @param response 输出 UTF-8 纯文本的响应
     * @return null 表示响应已直接写入，不再渲染视图
     * @throws Exception 参数转换或响应写出失败时抛出
     */
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        System.out.println("[适配器/v4] AlphaMaterialController.handleRequest ---> 接收 MVC 适配后的统一调度");
        long caseId = Long.parseLong(request.getParameter("caseId"));
        MaterialFile material = service.collectContract(caseId);
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write(material.toString());
        return null;
    }
}
