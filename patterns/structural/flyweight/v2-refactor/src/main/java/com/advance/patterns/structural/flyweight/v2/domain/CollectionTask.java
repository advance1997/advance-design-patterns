package com.advance.patterns.structural.flyweight.v2.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 案件独立的外部状态；不放入共享规则对象，避免不同案件互相污染。
 *
 * @author advance
 * @since 2026-10-28 21:22:00
 */
public class CollectionTask {
    /** 虚构案件编号。 */
    private final String caseId;
    /** 案件需要提交的材料规则，由案件任务独立记录。 */
    private final List<String> required = new ArrayList<String>();
    /** 当前案件已归集的材料编码。 */
    private final List<String> collected = new ArrayList<String>();
    /**
     * 创建案件独立的材料归集任务。
     *
     * @param caseId 虚构案件编号
     */
    public CollectionTask(String caseId) {
        this.caseId = caseId;
    }

    /**
     * 获取案件编号。
     *
     * @return 案件编号
     */
    public String getCaseId() { return caseId; }

    /**
     * 记录当前案件需要提交的一项材料。
     *
     * @param code 材料编码
     */
    public void require(String code) { required.add(code); }

    /**
     * 获取当前案件的必需材料清单。
     *
     * @return 不可修改的必需材料编码列表
     */
    public List<String> getRequired() { return Collections.unmodifiableList(required); }

    /**
     * 记录案件已归集的一项材料。
     *
     * @param code 材料编码
     */
    public void collect(String code) {
        collected.add(code);
        System.out.println("[享元/v2] CollectionTask.collect ---> 案件=" + caseId + "，材料=" + code);
    }
    /**
     * 获取当前案件已归集材料。
     *
     * @return 不可修改的已归集材料编码列表
     */
    public List<String> getCollected() { return Collections.unmodifiableList(collected); }
}
