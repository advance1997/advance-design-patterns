/**
 * 责任链模式专题的v2-refactor实践版本。
 *
 * <p>业务场景：虚构案件 DEMO-001 申请模拟外呼，依次经过两个检查： 1. Permission：是否有模拟案件操作权限。 2. ContactWindow：教学小时是否处于 `[9,18)`。 只有全部通过才到达 MockCallGateway，记录一次模拟申请。 不包含电话号码、不调用供应商、不发起真实外呼，也不操作资金。 9 至 18 点只是固定教学参数，不是监管要求，更不是完整的外呼合规方案。 权限结果与小时由测试构造，使行为可重复；生产权限必须由服务端可信系统判定， 不能相信前端传入的 permitted 标记。材料合规、真实触达权限等不在此模块范围内。</p>
 *
 * <p>本版本实现要点：提取 PermissionRule、ContactWindowRule；规则职责独立，但服务仍负责每个分支和下一步调用。责任链让请求依次经过处理节点，由节点决定继续传递或短路。</p>
 *
 * <p>具体调用路径请参见本版本目录下的流程图与时序图文档。</p>
 *
 * @author advance
 * @since 2026-10-29 15:47:52
 */
package com.advance.patterns.behavioral.chainofresponsibility.v2;
