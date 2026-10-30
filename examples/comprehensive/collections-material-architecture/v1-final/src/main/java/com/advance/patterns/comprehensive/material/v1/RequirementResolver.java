package com.advance.patterns.comprehensive.material.v1;

import java.util.ArrayList;
import java.util.List;

/**
 * 注册并路由各诉讼场景的材料要求策略。
 *
 * @author advance
 * @since 2026-10-30 12:00:00
 */
public class RequirementResolver {
    private final List<RequirementStrategy> strategies = new ArrayList<RequirementStrategy>();
    /**
     * 注册场景策略。
     *
     * @param strategy 场景材料策略
     */
    public void register(RequirementStrategy strategy) { strategies.add(strategy); }
    /**
     * 解析场景对应的材料清单。
     *
     * @param scene 场景编码
     * @param caseId 案件标识
     * @return 独立的材料要求集合
     * @throws IllegalArgumentException 未注册场景时抛出
     */
    public List<MaterialRequirement> resolve(String scene, String caseId) {
        for (RequirementStrategy strategy : strategies) {
            if (strategy.scene().equals(scene)) {
                System.out.println("[最终架构/Strategy] RequirementResolver.resolve ---> 使用场景策略 " + scene);
                return strategy.requirements(caseId);
            }
        }
        throw new IllegalArgumentException("未注册的材料归集场景：" + scene);
    }

    /** 教学用诉讼材料要求策略。 */
    public static class LitigationStrategy implements RequirementStrategy {
        @Override public String scene() { return "LITIGATION"; }
        @Override public List<MaterialRequirement> requirements(String caseId) {
            List<MaterialRequirement> result = new ArrayList<MaterialRequirement>();
            result.add(new MaterialRequirement("CONTRACT", "loan-contract", "借款合同.pdf", "一、债权证明材料", true));
            result.add(new MaterialRequirement("CONTRACT", "loan-agreement", "借款协议.pdf", "一、债权证明材料", true));
            result.add(new MaterialRequirement("LEDGER", "ledger", "债务流水.pdf", "一、债权证明材料", true));
            result.add(new MaterialRequirement("RECORDING", "call-recording", "外呼录音.mp3", "二、催收证明材料", false));
            return result;
        }
    }

    /** 教学用仲裁材料要求策略，展示场景材料清单的差异。 */
    public static class ArbitrationStrategy implements RequirementStrategy {
        @Override public String scene() { return "ARBITRATION"; }
        @Override public List<MaterialRequirement> requirements(String caseId) {
            List<MaterialRequirement> result = new ArrayList<MaterialRequirement>();
            result.add(new MaterialRequirement("CONTRACT", "loan-contract", "借款合同.pdf", "一、债权证明材料", true));
            result.add(new MaterialRequirement("LEDGER", "ledger", "债务流水.pdf", "一、债权证明材料", true));
            return result;
        }
    }
}
