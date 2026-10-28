package com.advance.patterns.structural.decorator.v1.service;

import com.advance.patterns.structural.decorator.v1.domain.MaterialBundle;

/**
 * 集中完成基础清单与可选增强；新增一种增强就需要修改此类的分支。
 *
 * @author advance
 * @since 2026-10-28 15:30:17
 */
public class MaterialExportService {

    /**
     * 直接按固定顺序追加复核说明和教学水印。
     *
     * @param bundle 已归集的虚构材料
     * @param review 是否追加复核说明
     * @param watermark 是否追加教学水印
     * @return 清单文本，不生成真实文件
     */
    public String export(MaterialBundle bundle, boolean review, boolean watermark) {
        System.out.println("[装饰器/v1] MaterialExportService.export ---> 生成基础材料清单");
        StringBuilder text = new StringBuilder("案件：" + bundle.getCaseId() + "\n材料清单：");
        for (String name : bundle.getMaterialNames()) {
            text.append("\n- ").append(name);
        }
        if (review) {
            System.out.println("[装饰器/v1] MaterialExportService.export ---> 追加人工复核说明");
            text.append("\n复核说明：材料仅供人工复核，不代表审核通过。");
        }
        if (watermark) {
            System.out.println("[装饰器/v1] MaterialExportService.export ---> 追加教学水印");
            text.append("\n水印：内部教学材料，禁止外传。");
        }
        return text.toString();
    }
}
