package com.advance.patterns.comprehensive.material.v1;

import java.util.ArrayList;
import java.util.List;

/** 表示教学实现生成的文件包描述。 @author advance @since 2026-10-30 12:00:00 */
public class MaterialPackage {
    private final String taskId; private final List<String> manifest; private final String statistics;
    /** @param taskId 任务标识 @param manifest 清单行 @param statistics 统计摘要 */
    public MaterialPackage(String taskId, List<String> manifest, String statistics) {
        this.taskId = taskId; this.manifest = new ArrayList<String>(manifest); this.statistics = statistics;
    }
    /** @return 任务标识 */ public String getTaskId() { return taskId; }
    /** @return 清单副本 */ public List<String> getManifest() { return new ArrayList<String>(manifest); }
    /** @return 统计摘要 */ public String getStatistics() { return statistics; }
}
