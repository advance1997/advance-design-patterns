package com.advance.patterns.creational.registeredfactory.v1;

import com.advance.patterns.creational.registeredfactory.v1.config.CollectionConfig;
import com.advance.patterns.creational.registeredfactory.v1.domain.CaseFile;
import com.advance.patterns.creational.registeredfactory.v1.domain.CaseStage;
import com.advance.patterns.creational.registeredfactory.v1.service.CollectionService;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 使用真实 Spring 容器的打印式实践，测试结束关闭上下文。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public class CollectionTest {
    /**
     * testStages：直接调用观察业务边界。
     * <p>期望结果：EARLY 返回 EARLY_REVIEW，LATE 返回 LATE_REVIEW；均为容器管理的处理器。</p>
     */
    @Test
    public void testStages() {
        System.out.println("\ntestStages ---> 期望：EARLY 返回 EARLY_REVIEW，LATE 返回 LATE_REVIEW；均为容器管理的处理器");
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                CollectionConfig.class)) {
            CollectionService service = context.getBean(CollectionService.class);
            System.out.println("早期结果：" + service.process(new CaseFile("DEMO-001", CaseStage.EARLY)));
            System.out.println("后期结果：" + service.process(new CaseFile("DEMO-002", CaseStage.LATE)));
        }
    }

    /**
     * testUnknownStage：直接调用观察业务边界。
     * <p>期望结果：LEGAL_REVIEW 未注册，IllegalArgumentException；不执行其他阶段处理器。</p>
     */
    @Test
    public void testUnknownStage() {
        System.out.println("\ntestUnknownStage ---> 期望：LEGAL_REVIEW 未注册，IllegalArgumentException；不执行其他阶段处理器");
        try {
            try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                    CollectionConfig.class)) {
                CollectionService service = context.getBean(CollectionService.class);
                service.process(new CaseFile("DEMO-003", CaseStage.LEGAL_REVIEW));
            }
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNullCase：直接调用观察业务边界。
     * <p>期望结果：IllegalArgumentException：caseFile 不能为空。</p>
     */
    @Test
    public void testNullCase() {
        System.out.println("\ntestNullCase ---> 期望：IllegalArgumentException：caseFile 不能为空");
        try {
            try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                    CollectionConfig.class)) {
                CollectionService service = context.getBean(CollectionService.class);
                service.process(null);
            }
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testInvalidCase：直接调用观察业务边界。
     * <p>期望结果：空编号抛出 IllegalArgumentException，不创建有效案件。</p>
     */
    @Test
    public void testInvalidCase() {
        System.out.println("\ntestInvalidCase ---> 期望：空编号抛出 IllegalArgumentException，不创建有效案件");
        try {
            new CaseFile(" ", CaseStage.EARLY);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }
}
