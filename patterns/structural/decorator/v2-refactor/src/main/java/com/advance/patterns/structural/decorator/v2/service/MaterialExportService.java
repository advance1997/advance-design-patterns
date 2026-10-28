package com.advance.patterns.structural.decorator.v2.service;

import com.advance.patterns.structural.decorator.v2.domain.MaterialBundle;

/**
 * 提取增强职责后的过渡服务；仍由服务判断开关和固定执行顺序。
 *
 * @author advance
 * @since 2026-10-28 15:30:17
 */
public class MaterialExportService {

    /** 专门负责复核说明的辅助对象。 */
    private final ReviewNoteAppender reviewNoteAppender = new ReviewNoteAppender();

    /** 专门负责教学水印的辅助对象。 */
    private final WatermarkAppender watermarkAppender = new WatermarkAppender();

    /**
     * 生成基础清单，再调用独立辅助类；尚未消除集中编排。
     *
     * @param bundle 已归集的虚构材料
     * @param review 是否追加复核说明
     * @param watermark 是否追加教学水印
     * @return 清单文本
     */
    public String export(MaterialBundle bundle, boolean review, boolean watermark) {
        System.out.println("[装饰器/v2] MaterialExportService.export ---> 生成基础材料清单");
        StringBuilder text = new StringBuilder("案件：" + bundle.getCaseId() + "\n材料清单：");
        for (String name : bundle.getMaterialNames()) {
            text.append("\n- ").append(name);
        }
        String result = text.toString();
        if (review) {
            result = reviewNoteAppender.append(result);
        }
        if (watermark) {
            result = watermarkAppender.append(result);
        }
        return result;
    }
}
