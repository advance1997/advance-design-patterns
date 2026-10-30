package com.advance.patterns.comprehensive.material.v1;

import java.util.LinkedHashMap;
import java.util.Map;

/** 将成功材料明细组织为诉讼材料 Composite 目录树。 @author advance @since 2026-10-30 12:00:00 */
public class MaterialTreeAssembler {
    /** @param task 已完成收集和校验的任务 @return 案件目录根节点 */
    public MaterialFolder assemble(MaterialTask task) {
        System.out.println("[最终架构/Composite] MaterialTreeAssembler.assemble ---> 构建案件材料目录树");
        MaterialFolder root = new MaterialFolder("案件" + task.getRequest().getCaseId());
        Map<String, MaterialFolder> folders = new LinkedHashMap<String, MaterialFolder>();
        for (MaterialTaskItem item : task.getItems()) {
            if (item.getStatus() != MaterialTaskItem.Status.SUCCESS) { continue; }
            FetchResult result = item.getResult(); String folderName = result.getFolderName();
            MaterialFolder folder = folders.get(folderName);
            if (folder == null) { folder = new MaterialFolder(folderName); folders.put(folderName, folder); root.add(folder); }
            folder.add(new MaterialFile(result.getFileName(), folderName, result.getSize(), item.getRequirement().getType()));
        }
        return root;
    }
}
