package com.advance.patterns.creational.prototype.v3;

import com.advance.patterns.creational.prototype.v3.domain.ReviewStep;
import com.advance.patterns.creational.prototype.v3.domain.TaskTemplate;
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
     * <p>期望结果：副本标记已复核后原模板 false，副本 true。</p>
     */
    @Test
    public void testCopyState() {
        System.out.println("\ntestCopyState ---> 期望：副本标记已复核后原模板 false，副本 true");
        List<ReviewStep> steps = new ArrayList<ReviewStep>();
        steps.add(new ReviewStep("材料核对"));
        TaskTemplate original = new TaskTemplate("原模板", steps);
        TaskTemplate copied = original.copy("复制模板");
        copied.markReviewed(0);
        System.out.println("原模板：" + original.describe());
        System.out.println("副本：" + copied.describe());
    }

    /**
     * testListIdentity：直接调用观察业务边界。
     * <p>期望结果：列表引用相同为 false；元素引用相同为 false。</p>
     */
    @Test
    public void testListIdentity() {
        System.out.println("\ntestListIdentity ---> 期望：列表引用相同为 false；元素引用相同为 false");
        List<ReviewStep> steps = new ArrayList<ReviewStep>();
        steps.add(new ReviewStep("材料核对"));
        TaskTemplate original = new TaskTemplate("原模板", steps);
        TaskTemplate copied = original.copy("复制模板");
        List<ReviewStep> originalSteps = original.getSteps();
        List<ReviewStep> copiedSteps = copied.getSteps();
        System.out.println("列表引用相同：" + (originalSteps == copiedSteps));
        System.out.println("步骤引用相同：" + (originalSteps.get(0) == copiedSteps.get(0)));
    }

    /**
     * testInputMutation：直接调用观察业务边界。
     * <p>期望结果：修改输入步骤后原模板状态为 false，输入已被深拷贝。</p>
     */
    @Test
    public void testInputMutation() {
        System.out.println("\ntestInputMutation ---> 期望：修改输入步骤后原模板状态为 false，输入已被深拷贝");
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

    /**
     * testReadOnlyList：直接调用观察业务边界。
     * <p>期望结果：修改读取列表抛出 UnsupportedOperationException。</p>
     */
    @Test
    public void testReadOnlyList() {
        System.out.println("\ntestReadOnlyList ---> 期望：修改读取列表抛出 UnsupportedOperationException");
        try {
            List<ReviewStep> steps = new ArrayList<ReviewStep>();
            steps.add(new ReviewStep("材料核对"));
            TaskTemplate original = new TaskTemplate("原模板", steps);
            original.getSteps().add(new ReviewStep("补充复核"));
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testSnapshotMutation：直接调用观察业务边界。
     * <p>期望结果：修改读取快照中的步骤，不改变原模板；原模板仍为 false。</p>
     */
    @Test
    public void testSnapshotMutation() {
        System.out.println("\ntestSnapshotMutation ---> 期望：修改读取快照中的步骤，不改变原模板；原模板仍为 false");
        List<ReviewStep> steps = new ArrayList<ReviewStep>();
        steps.add(new ReviewStep("材料核对"));
        TaskTemplate original = new TaskTemplate("原模板", steps);
        original.getSteps().get(0).setReviewed(true);
        System.out.println("原模板：" + original.describe());
    }

    /**
     * testCopiesCurrentState：直接调用观察业务边界。
     * <p>期望结果：原模板先标记 true 后复制，副本保留 true；复制不是重置状态。</p>
     */
    @Test
    public void testCopiesCurrentState() {
        System.out.println("\ntestCopiesCurrentState ---> 期望：原模板先标记 true 后复制，副本保留 true；复制不是重置状态");
        List<ReviewStep> steps = new ArrayList<ReviewStep>();
        steps.add(new ReviewStep("材料核对"));
        TaskTemplate original = new TaskTemplate("原模板", steps);
        original.markReviewed(0);
        TaskTemplate copied = original.copy("复制模板");
        System.out.println("副本：" + copied.describe());
    }

    /**
     * testNullSource：直接调用观察业务边界。
     * <p>期望结果：复制构造器输入 null 抛出 IllegalArgumentException。</p>
     */
    @Test
    public void testNullSource() {
        System.out.println("\ntestNullSource ---> 期望：复制构造器输入 null 抛出 IllegalArgumentException");
        try {
            new TaskTemplate((TaskTemplate) null, "副本");
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testInvalidCopyName：直接调用观察业务边界。
     * <p>期望结果：副本空名称抛出 IllegalArgumentException。</p>
     */
    @Test
    public void testInvalidCopyName() {
        System.out.println("\ntestInvalidCopyName ---> 期望：副本空名称抛出 IllegalArgumentException");
        try {
            List<ReviewStep> steps = new ArrayList<ReviewStep>();
            steps.add(new ReviewStep("材料核对"));
            TaskTemplate original = new TaskTemplate("原模板", steps);
            original.copy(" ");
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }
}
