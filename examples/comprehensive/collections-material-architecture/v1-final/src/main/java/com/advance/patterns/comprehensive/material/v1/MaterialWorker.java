package com.advance.patterns.comprehensive.material.v1;

/** 消费任务消息，执行材料收集、有限重试和文件包归档。 @author advance @since 2026-10-30 12:00:00 */
public class MaterialWorker {
    private final MockMaterialTaskRepository repository;
    private final MaterialCollector collector;
    private final MaterialTreeAssembler treeAssembler;
    private final MaterialPackageBuilder packageBuilder;
    private final MockObjectStorage objectStorage;
    private final MockMessageBroker broker;
    private final int maxRetries;
    /** @param repository 任务存储 @param collector 来源与校验组件 @param treeAssembler 目录树组装器
     * @param packageBuilder 文件包描述构建器 @param objectStorage 对象存储 @param broker 消息代理 @param maxRetries 最大重试次数 */
    public MaterialWorker(MockMaterialTaskRepository repository, MaterialCollector collector,
                          MaterialTreeAssembler treeAssembler, MaterialPackageBuilder packageBuilder,
                          MockObjectStorage objectStorage, MockMessageBroker broker, int maxRetries) {
        this.repository = repository; this.collector = collector; this.treeAssembler = treeAssembler;
        this.packageBuilder = packageBuilder; this.objectStorage = objectStorage; this.broker = broker;
        this.maxRetries = maxRetries;
    }
    /**
     * 消费一条任务消息；重复消息通过任务状态和版本认领过滤。
     * @param taskId 归集任务标识
     */
    public void handle(String taskId) {
        MaterialTask task = repository.find(taskId);
        if (task == null) { System.out.println("[最终架构/Worker] handle ---> 未找到任务，忽略消息"); return; }
        int expectedVersion = task.getVersion();
        System.out.println("[最终架构/Worker] handle ---> 尝试认领任务 " + taskId);
        if (!repository.claim(taskId, expectedVersion)) { return; }
        boolean transientError = false;
        boolean invalidResult = false;
        for (MaterialTaskItem item : task.getItems()) {
            if (item.getStatus() == MaterialTaskItem.Status.SUCCESS || item.getStatus() == MaterialTaskItem.Status.MISSING
                    || item.getStatus() == MaterialTaskItem.Status.FAILED) { continue; }
            MaterialCheckHandler.CheckOutcome outcome = collector.collect(item);
            if (item.getResult().getStatus() == FetchResult.Status.ERROR) { transientError = true; }
            if (!outcome.isAccepted() && item.getResult().getStatus() == FetchResult.Status.FOUND) { invalidResult = true; }
        }
        if (invalidResult) {
            task.setStatus(MaterialTask.Status.FAILED);
            System.out.println("[最终架构/Worker] handle ---> 文件校验失败，任务终止"); return;
        }
        if (transientError) {
            if (task.getRetryCount() < maxRetries) {
                task.incrementRetryCount(); task.setStatus(MaterialTask.Status.RETRY_WAIT);
                System.out.println("[最终架构/Worker] handle ---> 保存部分成功明细，安排第 " + task.getRetryCount() + " 次有限重试");
                broker.retry(taskId);
            } else {
                task.setStatus(MaterialTask.Status.FAILED);
                System.out.println("[最终架构/Worker] handle ---> 超过重试上限，任务失败");
            }
            return;
        }
        for (MaterialTaskItem item : task.getItems()) {
            if (item.getRequirement().isRequired() && item.getStatus() == MaterialTaskItem.Status.MISSING) {
                task.setStatus(MaterialTask.Status.INCOMPLETE);
                System.out.println("[最终架构/Worker] handle ---> 必需材料已确认缺失，任务标记 INCOMPLETE"); return;
            }
        }
        task.setStatus(MaterialTask.Status.PACKAGING);
        MaterialFolder root = treeAssembler.assemble(task);
        MaterialPackage materialPackage = packageBuilder.build(taskId, root);
        String objectKey = "material-package/" + taskId + "/" + task.getRuleVersion();
        objectStorage.put(objectKey, materialPackage);
        task.setObjectKey(objectKey);
        task.setStatus(MaterialTask.Status.COMPLETED);
        System.out.println("[最终架构/Worker] handle ---> 结果条件提交完成，状态 COMPLETED");
    }
}
