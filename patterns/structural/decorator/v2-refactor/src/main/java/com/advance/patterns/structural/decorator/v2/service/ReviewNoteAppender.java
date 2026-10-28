package com.advance.patterns.structural.decorator.v2.service;

/**
 * 独立追加人工复核说明；此时只是文本辅助类，还不能作为导出器相互包装。
 *
 * @author advance
 * @since 2026-10-28 15:30:17
 */
public class ReviewNoteAppender {

    /**
     * 在已有文本尾部追加人工复核说明。
     *
     * @param text 已有的材料清单文本
     * @return 增强后的新文本
     */
    public String append(String text) {
        System.out.println("[装饰器/v2] ReviewNoteAppender.append ---> 追加人工复核说明");
        return text + "\n复核说明：材料仅供人工复核，不代表审核通过。";
    }
}
