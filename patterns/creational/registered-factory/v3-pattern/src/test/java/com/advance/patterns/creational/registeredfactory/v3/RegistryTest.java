package com.advance.patterns.creational.registeredfactory.v3;

import com.advance.patterns.creational.registeredfactory.v3.config.CollectionConfig;
import com.advance.patterns.creational.registeredfactory.v3.domain.CaseFile;
import com.advance.patterns.creational.registeredfactory.v3.domain.CaseStage;
import com.advance.patterns.creational.registeredfactory.v3.factory.CollectionHandlerFactory;
import com.advance.patterns.creational.registeredfactory.v3.handler.CollectionHandler;
import com.advance.patterns.creational.registeredfactory.v3.service.CollectionService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 注册边界演示；实际刷新 Spring 容器观察自动发现与启动失败。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public class RegistryTest {
    /**
     * testAutoDiscovery：直接调用观察业务边界。
     * <p>期望结果：额外注册 LEGAL_REVIEW Bean 后自动进入工厂；结果 LEGAL_PENDING，不改工厂或服务。</p>
     */
    @Test
    public void testAutoDiscovery() {
        System.out.println("\ntestAutoDiscovery ---> 期望：额外注册 LEGAL_REVIEW Bean 后自动进入工厂；结果 LEGAL_PENDING，不改工厂或服务");
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(CollectionConfig.class);
            context.registerBean("extraHandler", ExtraHandler.class, CaseStage.LEGAL_REVIEW);
            context.refresh();
            CollectionService service = context.getBean(CollectionService.class);
            System.out.println("新增处理器结果：" + service.process(new CaseFile("DEMO-003", CaseStage.LEGAL_REVIEW)));
        }
    }

    /**
     * testDuplicateFailsRefresh：直接调用观察业务边界。
     * <p>期望结果：额外注册 EARLY Bean 时 context.refresh 失败；BeanCreationException 包含阶段重复注册原因。</p>
     */
    @Test
    public void testDuplicateFailsRefresh() {
        System.out.println("\ntestDuplicateFailsRefresh ---> 期望：额外注册 EARLY Bean 时 context.refresh 失败；"
                + "BeanCreationException 包含阶段重复注册原因");
        try {
            try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
                context.register(CollectionConfig.class);
                context.registerBean("duplicateHandler", ExtraHandler.class, CaseStage.EARLY);
                context.refresh();
            }
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNullList：直接调用观察业务边界。
     * <p>期望结果：null 列表抛出 IllegalArgumentException。</p>
     */
    @Test
    public void testNullList() {
        System.out.println("\ntestNullList ---> 期望：null 列表抛出 IllegalArgumentException");
        try {
            new CollectionHandlerFactory(null);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testEmptyList：直接调用观察业务边界。
     * <p>期望结果：空列表抛出 IllegalArgumentException。</p>
     */
    @Test
    public void testEmptyList() {
        System.out.println("\ntestEmptyList ---> 期望：空列表抛出 IllegalArgumentException");
        try {
            new CollectionHandlerFactory(new ArrayList<CollectionHandler>());
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNullElement：直接调用观察业务边界。
     * <p>期望结果：列表包含 null 抛出 IllegalArgumentException。</p>
     */
    @Test
    public void testNullElement() {
        System.out.println("\ntestNullElement ---> 期望：列表包含 null 抛出 IllegalArgumentException");
        try {
            List<CollectionHandler> handlers = new ArrayList<CollectionHandler>();
            handlers.add(null);
            new CollectionHandlerFactory(handlers);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNullStage：直接调用观察业务边界。
     * <p>期望结果：处理器返回 null 阶段，IllegalArgumentException。</p>
     */
    @Test
    public void testNullStage() {
        System.out.println("\ntestNullStage ---> 期望：处理器返回 null 阶段，IllegalArgumentException");
        try {
            List<CollectionHandler> handlers = new ArrayList<CollectionHandler>();
            handlers.add(new ExtraHandler(null));
            new CollectionHandlerFactory(handlers);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testListIsolation：直接调用观察业务边界。
     * <p>期望结果：注册后清空传入列表不影响查找；LEGAL_PENDING。</p>
     */
    @Test
    public void testListIsolation() {
        System.out.println("\ntestListIsolation ---> 期望：注册后清空传入列表不影响查找；LEGAL_PENDING");
        List<CollectionHandler> handlers = new ArrayList<CollectionHandler>();
        handlers.add(new ExtraHandler(CaseStage.LEGAL_REVIEW));
        CollectionHandlerFactory factory = new CollectionHandlerFactory(handlers);
        handlers.clear();
        System.out.println("实际结果：" + factory.getHandler(CaseStage.LEGAL_REVIEW)
                .handle(new CaseFile("DEMO-003", CaseStage.LEGAL_REVIEW)));
    }

    /**
     * testNullLookup：直接调用观察业务边界。
     * <p>期望结果：查找 null 阶段抛出 IllegalArgumentException。</p>
     */
    @Test
    public void testNullLookup() {
        System.out.println("\ntestNullLookup ---> 期望：查找 null 阶段抛出 IllegalArgumentException");
        try {
            List<CollectionHandler> handlers = new ArrayList<CollectionHandler>();
            handlers.add(new ExtraHandler(CaseStage.LEGAL_REVIEW));
            new CollectionHandlerFactory(handlers).getHandler(null);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * 具名扩展处理器，演示新增 Bean，而不是修改工厂分支。
     *
     * @author advance
     * @since 2026-10-27 20:33:03
     */
    public static final class ExtraHandler implements CollectionHandler {
        /** 声明的阶段，测试允许为空以演示错误注册。 */
        private final CaseStage stage;

        /**
         * 保存本次演示的阶段。
         * @param stage 声明阶段
         */
        public ExtraHandler(CaseStage stage) {
            this.stage = stage;
        }

        /**
         * 获取声明阶段。
         * @return 阶段，测试中可能为空
         */
        @Override
        public CaseStage getStage() {
            return stage;
        }

        /**
         * 只生成模拟法务待审核标识，不采取法律行动。
         * @param caseFile 虚构案件
         * @return LEGAL_PENDING
         * @throws IllegalArgumentException 案件为空或阶段不匹配
         */
        @Override
        public String handle(CaseFile caseFile) {
            if (caseFile == null || caseFile.getStage() != stage) {
                throw new IllegalArgumentException("案件阶段不匹配");
            }
            System.out.println("[registered-factory/v3] ExtraHandler.handle ---> 进入模拟法务人工待审核");
            return "LEGAL_PENDING";
        }
    }
}
