package com.advance.patterns.behavioral.visitor.v3;

/**
 * 录音叶子元素，只保存材料数据并通过 accept 将操作分派给访问者。
 *
 * @author advance
 * @since 2026-10-30 00:13:24
 */
public class AudioRecording implements MaterialNode {
    /** 文件名称。 */
    private final String name;
    /** 文件大小，单位为字节。 */
    private final long size;
    /** 示例中模拟的可访问状态。 */
    private final boolean accessible;

    /**
     * 创建虚构录音材料。
     *
     * @param name 文件名称
     * @param size 文件大小，单位为字节
     * @param accessible 文件是否可访问
     */
    public AudioRecording(String name, long size, boolean accessible) {
        this.name = name;
        this.size = size;
        this.accessible = accessible;
    }

    /**
     * 获取录音文件名称。
     *
     * @return 文件名称
     */
    public String getName() { return name; }

    /**
     * 获取录音文件大小。
     *
     * @return 文件大小，单位为字节
     */
    public long getSize() { return size; }

    /**
     * 判断录音是否可访问。
     *
     * @return 可访问时返回 true
     */
    public boolean isAccessible() { return accessible; }

    /**
     * 将当前节点静态类型作为重载参数传给访问者。
     *
     * @param visitor 本次操作的访问者
     */
    @Override
    public void accept(MaterialVisitor visitor) {
        System.out.println("[访问者/v3] AudioRecording.accept ---> 调用 visit(AudioRecording)：" + name);
        visitor.visit(this);
    }
}
