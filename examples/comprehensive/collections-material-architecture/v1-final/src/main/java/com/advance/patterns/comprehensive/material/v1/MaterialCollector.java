package com.advance.patterns.comprehensive.material.v1;

import java.util.LinkedHashMap;
import java.util.Map;

/** 使用统一适配器收集材料并执行责任链校验。 @author advance @since 2026-10-30 12:00:00 */
public class MaterialCollector {
    private final Map<String, MaterialSource> sources = new LinkedHashMap<String, MaterialSource>();
    private final MaterialCheckHandler checkChain;
    /** @param checkChain 结果校验链入口 */ public MaterialCollector(MaterialCheckHandler checkChain) { this.checkChain = checkChain; }
    /** @param type 来源类型 @param source 统一材料来源接口 */ public void register(String type, MaterialSource source) { sources.put(type, source); }
    /**
     * 获取并记录一项材料。
     * @param item 任务材料明细 @return 校验结论
     * @throws IllegalArgumentException 来源类型未注册时抛出
     */
    public MaterialCheckHandler.CheckOutcome collect(MaterialTaskItem item) {
        MaterialRequirement requirement = item.getRequirement();
        MaterialSource source = sources.get(requirement.getType());
        if (source == null) { throw new IllegalArgumentException("未注册材料来源适配器：" + requirement.getType()); }
        System.out.println("[最终架构/Adapter] MaterialCollector.collect ---> 获取 " + requirement.getFileName());
        FetchResult result = source.fetch(requirement); item.record(result);
        return checkChain.check(requirement, result);
    }
}
