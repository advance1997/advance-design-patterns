package com.advance.patterns.structural.decorator.v2.service;

/**
 * 独立追加教学水印；此时只是文本辅助类，还不能作为导出器相互包装。
 *
 * @author advance
 * @since 2026-10-28 15:30:17
 */
public class WatermarkAppender {

    /**
     * 在已有文本尾部追加教学水印。
     *
     * @param text 已有的材料清单文本
     * @return 增强后的新文本
     */
    public String append(String text) {
        System.out.println("[装饰器/v2] WatermarkAppender.append ---> 追加教学水印");
        return text + "\n水印：内部教学材料，禁止外传。";
    }
}
