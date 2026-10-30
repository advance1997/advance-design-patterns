package com.advance.patterns.comprehensive.material.v1;

/** 组合 Iterator 与 Visitor 构建可追踪的材料包描述。 @author advance @since 2026-10-30 12:00:00 */
public class MaterialPackageBuilder {
    /** @param taskId 任务标识 @param root 冻结后的材料树 @return 文件包描述 */
    public MaterialPackage build(String taskId, MaterialFolder root) {
        System.out.println("[最终架构/Builder] MaterialPackageBuilder.build ---> 生成目录清单与统计");
        StatisticsVisitor statistics = new StatisticsVisitor(); ManifestVisitor manifest = new ManifestVisitor();
        MaterialTreeIterator iterator = new MaterialTreeIterator(root);
        while (iterator.hasNext()) {
            MaterialNode node = iterator.next(); node.accept(statistics); node.accept(manifest);
        }
        String summary = "目录=" + statistics.getFolderCount() + "，PDF=" + statistics.getPdfCount()
                + "，录音=" + statistics.getAudioCount() + "，总大小=" + statistics.getTotalBytes() + "字节";
        System.out.println("[最终架构/Iterator] MaterialTreeIterator.next ---> 遍历完成，共 " + manifest.getLines().size() + " 条清单");
        return new MaterialPackage(taskId, manifest.getLines(), summary);
    }
}
