package com.advance.patterns.creational.prototype.v1;

import com.advance.patterns.creational.prototype.v1.domain.ReviewStep;
import com.advance.patterns.creational.prototype.v1.domain.TaskTemplate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * 人工复核模板复制实践：直接打印对象引用和可变步骤状态。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public class PrototypeTest {
    /**
     * testCopyState：直接调用观察业务边界。
     * <p>期望结果：副本标记已复核后原模板也变为 true，暴露共享元素风险。</p>
     */
    @Test
    public void testCopyState() {
        System.out.println("\ntestCopyState ---> 期望：副本标记已复核后原模板也变为 true，暴露共享元素风险");
        List<ReviewStep> steps = new ArrayList<ReviewStep>();
        steps.add(new ReviewStep("材料核对"));
        TaskTemplate original = new TaskTemplate("原模板", steps);
        TaskTemplate copied = new TaskTemplate("复制模板", original.getSteps());
        copied.markReviewed(0);
        System.out.println("原模板：" + original.describe());
        System.out.println("副本：" + copied.describe());
    }

    /**
     * testListIdentity：直接调用观察业务边界。
     * <p>期望结果：列表引用相同为 true；元素引用相同为 true。</p>
     */
    @Test
    public void testListIdentity() {
        System.out.println("\ntestListIdentity ---> 期望：列表引用相同为 true；元素引用相同为 true");
        List<ReviewStep> steps = new ArrayList<ReviewStep>();
        steps.add(new ReviewStep("材料核对"));
        TaskTemplate original = new TaskTemplate("原模板", steps);
        TaskTemplate copied = new TaskTemplate("复制模板", original.getSteps());
        List<ReviewStep> originalSteps = original.getSteps();
        List<ReviewStep> copiedSteps = copied.getSteps();
        System.out.println("列表引用相同：" + (originalSteps == copiedSteps));
        System.out.println("步骤引用相同：" + (originalSteps.get(0) == copiedSteps.get(0)));
    }

    /**
     * testInputMutation：直接调用观察业务边界。
     * <p>期望结果：修改输入步骤后原模板状态为 true，输入元素仍被共享。</p>
     */
    @Test
    public void testInputMutation() {
        System.out.println("\ntestInputMutation ---> 期望：修改输入步骤后原模板状态为 true，输入元素仍被共享");
        List<ReviewStep> steps = new ArrayList<ReviewStep>();
        steps.add(new ReviewStep("材料核对"));
        TaskTemplate original = new TaskTemplate("原模板", steps);
        steps.get(0).setReviewed(true);
        System.out.println("原模板：" + original.describe());
    }

    /**
     * testBlankName：直接调用观察业务边界。
     * <p>期望结果：空模板名称抛出 IllegalArgumentException。</p>
     */
    @Test
    public void testBlankName() {
        System.out.println("\ntestBlankName ---> 期望：空模板名称抛出 IllegalArgumentException");
        try {
            List<ReviewStep> steps = new ArrayList<ReviewStep>();
            steps.add(new ReviewStep("材料核对"));
            new TaskTemplate(" ", steps);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNullSteps：直接调用观察业务边界。
     * <p>期望结果：null 列表抛出 IllegalArgumentException。</p>
     */
    @Test
    public void testNullSteps() {
        System.out.println("\ntestNullSteps ---> 期望：null 列表抛出 IllegalArgumentException");
        try {
            new TaskTemplate("原模板", null);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testEmptySteps：直接调用观察业务边界。
     * <p>期望结果：空步骤列表抛出 IllegalArgumentException。</p>
     */
    @Test
    public void testEmptySteps() {
        System.out.println("\ntestEmptySteps ---> 期望：空步骤列表抛出 IllegalArgumentException");
        try {
            new TaskTemplate("原模板", new ArrayList<ReviewStep>());
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNullStep：直接调用观察业务边界。
     * <p>期望结果：列表含 null 抛出 IllegalArgumentException。</p>
     */
    @Test
    public void testNullStep() {
        System.out.println("\ntestNullStep ---> 期望：列表含 null 抛出 IllegalArgumentException");
        try {
            List<ReviewStep> steps = new ArrayList<ReviewStep>();
            steps.add(null);
            new TaskTemplate("原模板", steps);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testInvalidIndex：直接调用观察业务边界。
     * <p>期望结果：越界标记抛出 IllegalArgumentException。</p>
     */
    @Test
    public void testInvalidIndex() {
        System.out.println("\ntestInvalidIndex ---> 期望：越界标记抛出 IllegalArgumentException");
        try {
            List<ReviewStep> steps = new ArrayList<ReviewStep>();
            steps.add(new ReviewStep("材料核对"));
            TaskTemplate original = new TaskTemplate("原模板", steps);
            original.markReviewed(1);
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }
}
