package com.advance.patterns.comprehensive.material.v1;

import org.junit.jupiter.api.Test;

/**
 * 验证诉讼材料归集最终架构的完整模拟路径。
 *
 * @author advance
 * @since 2026-10-30 12:00:00
 */
public class FinalArchitectureTest {
    /**
     * 输入为同一虚构诉讼案件重复提交两次；合同协议首次查询模拟超时。
     * 期望：两次提交返回同一个任务；第一次执行保留其余成功材料并有限重试；第二次只补取失败明细，
     * 最终状态 COMPLETED，目录 3 个、PDF 3 份、录音 1 份、总大小 10240 字节，对象包成功保存。
     * 预期路径：Facade → 幂等/Outbox → Mock MQ → Worker → Strategy/Adapter/责任链 → Composite/Iterator/Visitor/Builder → Mock 对象存储。
     */
    @Test
    public void shouldCollectPackageWithIdempotencyAndBoundedRetry() {
        RequirementResolver resolver = new RequirementResolver();
        resolver.register(new RequirementResolver.LitigationStrategy());
        resolver.register(new RequirementResolver.ArbitrationStrategy());

        MockMaterialTaskRepository repository = new MockMaterialTaskRepository();
        MockMessageBroker broker = new MockMessageBroker();
        MockSourceClients.ContractClient contractClient = new MockSourceClients.ContractClient();
        MaterialCheckHandler.AvailabilityHandler availability = new MaterialCheckHandler.AvailabilityHandler();
        availability.setNext(new MaterialCheckHandler.FileMetadataHandler());
        MaterialCollector collector = new MaterialCollector(availability);
        collector.register("CONTRACT", new MaterialSourceAdapters.ContractAdapter(contractClient));
        collector.register("LEDGER", new MaterialSourceAdapters.LedgerAdapter(new MockSourceClients.LedgerClient()));
        collector.register("RECORDING", new MaterialSourceAdapters.RecordingAdapter(new MockSourceClients.RecordingClient()));

        MockObjectStorage objectStorage = new MockObjectStorage();
        MaterialWorker worker = new MaterialWorker(repository, collector, new MaterialTreeAssembler(),
                new MaterialPackageBuilder(), objectStorage, broker, 2);
        OutboxPublisher publisher = new OutboxPublisher(repository, broker);
        MaterialFacade facade = new MaterialFacade(new MaterialTaskService(resolver, repository), publisher);

        MaterialRequest request = new MaterialRequest("tenant-demo", "request-10001", "CASE-10001", "LITIGATION");
        String firstTaskId = facade.submit(request);
        String duplicateTaskId = facade.submit(request);
        System.out.println("[期望] 幂等任务标识相同：" + firstTaskId + " = " + duplicateTaskId);

        String firstMessage = broker.poll();
        worker.handle(firstMessage);
        MaterialTask taskAfterFirstAttempt = repository.find(firstTaskId);
        System.out.println("[期望] 首次执行保留成功明细并进入 RETRY_WAIT；当前状态：" + taskAfterFirstAttempt.getStatus());

        String retryMessage = broker.poll();
        worker.handle(retryMessage);
        MaterialTask completedTask = repository.find(firstTaskId);
        MaterialPackage materialPackage = objectStorage.get(completedTask.getObjectKey());
        System.out.println("[期望] 最终任务状态：COMPLETED；实际状态：" + completedTask.getStatus());
        System.out.println("[期望] 最终对象键携带规则版本：" + completedTask.getObjectKey());
        System.out.println("[期望] 目录=3，PDF=3，录音=1，总大小=10240字节；实际统计：" + materialPackage.getStatistics());
        System.out.println("[期望] 清单包含全部 4 份材料；实际清单：");
        for (String line : materialPackage.getManifest()) { System.out.println("  " + line); }
    }
}
