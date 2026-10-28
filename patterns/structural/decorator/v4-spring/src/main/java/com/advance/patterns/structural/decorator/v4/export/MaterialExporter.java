package com.advance.patterns.structural.decorator.v4.export;

import com.advance.patterns.structural.decorator.v4.domain.MaterialBundle;

/**
 * 统一导出组件契约；基础实现和装饰器均可作为客户端的导出器。
 *
 * @author advance
 * @since 2026-10-28 15:30:17
 */
public interface MaterialExporter {

    /**
     * 根据材料快照生成清单；装饰器在委托结果上追加自己的内容。
     *
     * @param bundle 已归集的虚构材料快照
     * @return 清单文本
     */
    String export(MaterialBundle bundle);
}
