package com.advance.patterns.structural.flyweight.v3.domain;

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
    /** 当前案件已归集的材料编码。 */
    private final List<String> collected = new ArrayList<String>();
    public CollectionTask(String caseId) { this.caseId = caseId; }
    public String getCaseId() { return caseId; }
    public void collect(String code) {
        collected.add(code);
        System.out.println("[享元/v3] CollectionTask.collect ---> 案件=" + caseId + "，材料=" + code);
    }
    public List<String> getCollected() { return Collections.unmodifiableList(collected); }
}
