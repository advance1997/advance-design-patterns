package com.advance.patterns.behavioral.visitor.v1;

import java.util.List;

/**
 * 录音材料元素基线类，将录音统计与可访问性校验逻辑直接放在数据对象中。
 *
 * @author advance
 * @since 2026-10-30 00:13:24
 */
public class AudioRecording implements MaterialNode {
    /** 录音文件名称。 */
    private final String name;
    /** 录音文件大小，单位为字节。 */
    private final long size;
    /** 示例中模拟的文件可访问状态。 */
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
     * 获取文件名称。
     *
     * @return 文件名称
     */
    public String getName() { return name; }

    /**
     * 获取文件大小。
     *
     * @return 文件大小，单位为字节
     */
    public long getSize() { return size; }

    /**
     * 将当前录音计入统计结果。
     *
     * @param statistics 本次统计使用的可变结果对象
     */
    @Override
    public void collectStatistics(MaterialStatistics statistics) {
        System.out.println("[访问者/v1] AudioRecording.collectStatistics ---> 统计录音：" + name);
        statistics.addAudio(size);
    }

    /**
     * 检查当前录音是否可以访问。
     *
     * @param issues 本次校验发现的问题集合
     */
    @Override
    public void validate(List<String> issues) {
        System.out.println("[访问者/v1] AudioRecording.validate ---> 校验录音：" + name);
        if (!accessible) {
            issues.add("录音不可访问：" + name);
        }
    }
}
