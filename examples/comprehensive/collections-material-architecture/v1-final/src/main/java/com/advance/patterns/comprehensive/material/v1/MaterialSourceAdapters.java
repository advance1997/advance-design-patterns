package com.advance.patterns.comprehensive.material.v1;

/**
 * 将异构外部客户端适配到统一的 {@link MaterialSource} 协议。
 *
 * @author advance
 * @since 2026-10-30 12:00:00
 */
public final class MaterialSourceAdapters {
    private MaterialSourceAdapters() { }

    /** 合同系统适配器。 */
    public static class ContractAdapter implements MaterialSource {
        private final MockSourceClients.ContractClient client;
        public ContractAdapter(MockSourceClients.ContractClient client) { this.client = client; }
        @Override
        public FetchResult fetch(MaterialRequirement requirement) {
            MockSourceClients.RawFile file = client.query(requirement.getSourceKey());
            if (file == null && client.wasLastQueryTimedOut()) {
                return new FetchResult(FetchResult.Status.ERROR, null, requirement.getFolderName(), 0, "合同系统模拟超时");
            }
            return foundOrMissing(file, requirement);
        }
    }

    /** 账务系统适配器。 */
    public static class LedgerAdapter implements MaterialSource {
        private final MockSourceClients.LedgerClient client;
        public LedgerAdapter(MockSourceClients.LedgerClient client) { this.client = client; }
        @Override
        public FetchResult fetch(MaterialRequirement requirement) {
            return foundOrMissing(client.query(requirement.getSourceKey()), requirement);
        }
    }

    /** 录音系统适配器。 */
    public static class RecordingAdapter implements MaterialSource {
        private final MockSourceClients.RecordingClient client;
        public RecordingAdapter(MockSourceClients.RecordingClient client) { this.client = client; }
        @Override
        public FetchResult fetch(MaterialRequirement requirement) {
            return foundOrMissing(client.query(requirement.getSourceKey()), requirement);
        }
    }

    private static FetchResult foundOrMissing(MockSourceClients.RawFile file, MaterialRequirement requirement) {
        if (file == null) {
            return new FetchResult(FetchResult.Status.MISSING, null, requirement.getFolderName(), 0, "来源确认材料不存在");
        }
        return new FetchResult(FetchResult.Status.FOUND, file.getName(), requirement.getFolderName(), file.getSize(), "来源查询成功");
    }
}
