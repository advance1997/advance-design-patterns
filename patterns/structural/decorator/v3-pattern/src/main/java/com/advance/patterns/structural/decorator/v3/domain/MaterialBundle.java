package com.advance.patterns.structural.decorator.v3.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 已归集的虚构案件材料名称快照；仅用于清单展示，不读取真实材料或判断材料是否合规。
 *
 * @author advance
 * @since 2026-10-28 15:30:17
 */
public final class MaterialBundle {

    /** 虚构案件编号。 */
    private final String caseId;

    /** 独立保存的只读材料名称集合。 */
    private final List<String> materialNames;

    /**
     * 保存清单输入快照，避免调用方修改列表影响后续导出。
     *
     * @param caseId 虚构案件编号
     * @param materialNames 已归集的材料名称，不包含真实材料内容
     * @throws NullPointerException 案件编号或材料列表为空引用时抛出
     */
    public MaterialBundle(String caseId, List<String> materialNames) {
        this.caseId = Objects.requireNonNull(caseId, "caseId 不能为空");
        this.materialNames = Collections.unmodifiableList(
                new ArrayList<String>(Objects.requireNonNull(materialNames, "materialNames 不能为空")));
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
     * 获取只读的材料名称快照。
     *
     * @return 材料名称列表
     */
    public List<String> getMaterialNames() {
        return materialNames;
    }
}
