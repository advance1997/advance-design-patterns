package com.advance.patterns.comprehensive.material.v1;

import java.util.HashMap;
import java.util.Map;

/**
 * 提供内存模拟的合同、账务和录音系统客户端。
 *
 * @author advance
 * @since 2026-10-30 12:00:00
 */
public final class MockSourceClients {
    private MockSourceClients() { }

    /** 不同模拟系统返回的原始文件结构。 */
    public static class RawFile {
        private final String name;
        private final long size;
        public RawFile(String name, long size) { this.name = name; this.size = size; }
        public String getName() { return name; }
        public long getSize() { return size; }
    }

    /** 模拟合同系统客户端；借款协议首次查询超时，后续可成功。 */
    public static class ContractClient {
        private int agreementAttempts;
        private boolean lastQueryTimedOut;
        public RawFile query(String key) {
            System.out.println("[最终架构/Adapter] ContractClient.query ---> 查询合同系统");
            lastQueryTimedOut = false;
            if ("loan-agreement".equals(key) && agreementAttempts++ == 0) {
                lastQueryTimedOut = true;
                return null;
            }
            return "loan-contract".equals(key) ? new RawFile("借款合同.pdf", 1024)
                    : new RawFile("借款协议.pdf", 2048);
        }
        /** @return 最近一次合同查询是否因模拟超时失败 */
        public boolean wasLastQueryTimedOut() { return lastQueryTimedOut; }
    }

    /** 模拟账务系统客户端。 */
    public static class LedgerClient {
        private final Map<String, RawFile> files = new HashMap<String, RawFile>();
        public LedgerClient() { files.put("ledger", new RawFile("债务流水.pdf", 3072)); }
        public RawFile query(String key) {
            System.out.println("[最终架构/Adapter] LedgerClient.query ---> 查询账务系统");
            return files.get(key);
        }
    }

    /** 模拟录音平台客户端。 */
    public static class RecordingClient {
        public RawFile query(String key) {
            System.out.println("[最终架构/Adapter] RecordingClient.query ---> 查询录音平台");
            return "call-recording".equals(key) ? new RawFile("外呼录音.mp3", 4096) : null;
        }
    }
}
