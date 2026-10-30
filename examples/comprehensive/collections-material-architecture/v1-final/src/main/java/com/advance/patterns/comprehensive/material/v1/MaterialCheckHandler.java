package com.advance.patterns.comprehensive.material.v1;

/** 可按顺序扩展的材料结果校验责任链。 @author advance @since 2026-10-30 12:00:00 */
public abstract class MaterialCheckHandler {
    private MaterialCheckHandler next;
    /** @param next 后续处理器 @return 后续处理器，便于配置链 */
    public MaterialCheckHandler setNext(MaterialCheckHandler next) { this.next = next; return next; }
    /** @param requirement 材料要求 @param result 来源结果 @return 整条链的处理结果 */
    public final CheckOutcome check(MaterialRequirement requirement, FetchResult result) {
        CheckOutcome outcome = doCheck(requirement, result);
        if (!outcome.isAccepted() || next == null) { return outcome; }
        return next.check(requirement, result);
    }
    /** @param requirement 材料要求 @param result 来源结果 @return 当前节点结果 */
    protected abstract CheckOutcome doCheck(MaterialRequirement requirement, FetchResult result);
    /** 责任链检查结论。 */
    public static class CheckOutcome {
        private final boolean accepted; private final String message;
        public CheckOutcome(boolean accepted, String message) { this.accepted = accepted; this.message = message; }
        /** @return 是否通过 */ public boolean isAccepted() { return accepted; }
        /** @return 处理说明 */ public String getMessage() { return message; }
    }
    /** 检查来源状态和必需材料缺失规则。 */
    public static class AvailabilityHandler extends MaterialCheckHandler {
        @Override protected CheckOutcome doCheck(MaterialRequirement requirement, FetchResult result) {
            System.out.println("[最终架构/Chain] AvailabilityHandler.doCheck ---> 检查来源结果");
            if (result.getStatus() == FetchResult.Status.ERROR) { return new CheckOutcome(false, "来源暂时不可用，安排重试"); }
            if (result.getStatus() == FetchResult.Status.MISSING && requirement.isRequired()) {
                return new CheckOutcome(false, "必需材料已确认缺失");
            }
            return new CheckOutcome(true, "材料存在或为可选缺失");
        }
    }
    /** 检查已获取材料的基础文件元数据。 */
    public static class FileMetadataHandler extends MaterialCheckHandler {
        @Override protected CheckOutcome doCheck(MaterialRequirement requirement, FetchResult result) {
            System.out.println("[最终架构/Chain] FileMetadataHandler.doCheck ---> 检查文件名称与大小");
            if (result.getStatus() == FetchResult.Status.FOUND
                    && (result.getSize() <= 0 || result.getFileName() == null || result.getFileName().trim().isEmpty())) {
                return new CheckOutcome(false, "文件元数据无效");
            }
            return new CheckOutcome(true, "文件元数据有效");
        }
    }
}
