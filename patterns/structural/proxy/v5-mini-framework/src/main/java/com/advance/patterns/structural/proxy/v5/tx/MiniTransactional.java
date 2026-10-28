package com.advance.patterns.structural.proxy.v5.tx;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 简化声明式事务注解：只支持实现类的 public 接口方法、同线程单数据源 REQUIRED。
 *
 * @author advance
 * @since 2026-10-28 10:15:29
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MiniTransactional {
    /**
     * 额外要求回滚的异常类型；默认仅 RuntimeException 与 Error 回滚。
     * @return 回滚异常类型数组
     */
    Class<? extends Throwable>[] rollbackFor() default {};
}
