package com.advance.patterns.structural.decorator.v4.export;

import com.advance.patterns.structural.decorator.v4.domain.MaterialBundle;
import java.util.Objects;

/**
 * 装饰器基类：实现同一接口并持有同接口组件，使任意层装饰器能够相互组合。
 *
 * @author advance
 * @since 2026-10-28 15:30:17
 */
public abstract class MaterialExporterDecorator implements MaterialExporter {

    /** 被包装的组件，可以是基础导出器，也可以是另一层装饰器。 */
    private final MaterialExporter delegate;

    /**
     * 固定本层装饰器的委托目标。
     *
     * @param delegate 被包装的组件
     * @throws NullPointerException 委托目标为空时抛出
     */
    protected MaterialExporterDecorator(MaterialExporter delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate 不能为空");
    }

    /**
     * 调用内层组件；不改变参数，也不持有单次请求的数据。
     *
     * @param bundle 当前材料快照
     * @return 内层组件生成的文本
     */
    @Override
    public String export(MaterialBundle bundle) {
        return delegate.export(bundle);
    }
}
