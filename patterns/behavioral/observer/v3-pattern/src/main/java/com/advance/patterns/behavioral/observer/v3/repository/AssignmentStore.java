package com.advance.patterns.behavioral.observer.v3.repository;

import java.util.HashMap;
import java.util.Map;

/**
 * 仅用于教学的单线程内存分派记录，不连接数据库，不提供事务或持久化保证。
 *
 * @author advance
 * @since 2026-10-28 20:28:32
 */
public class AssignmentStore {

    /** 本实例保存的案件与经办人对应关系。 */
    private final Map<String, String> assignments = new HashMap<String, String>();

    /**
     * 更新虚构案件的内存分派结果。
     *
     * @param caseId 虚构案件编号
     * @param collectorId 模拟经办人编号
     */
    public void save(String caseId, String collectorId) {
        System.out.println("[观察者/v3] AssignmentStore.save ---> 保存模拟分派，caseId="
                + caseId + "，collectorId=" + collectorId);
        assignments.put(caseId, collectorId);
    }

    /**
     * 读取当前内存分派结果，用来观察通知失败是否改变已完成操作。
     *
     * @param caseId 虚构案件编号
     * @return 当前经办人编号；不存在时为 null
     */
    public String findCollector(String caseId) {
        return assignments.get(caseId);
    }
}
