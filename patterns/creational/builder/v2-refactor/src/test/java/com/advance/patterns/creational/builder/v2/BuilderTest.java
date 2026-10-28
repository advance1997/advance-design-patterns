package com.advance.patterns.creational.builder.v2;

import com.advance.patterns.creational.builder.v2.domain.CollectionTask;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * 长构造器、setter 和 Builder 的可读性与合法性边界演示。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public class BuilderTest {
    /**
     * testValidTask：直接调用观察业务边界。
     * <p>期望结果：名称复核任务；逾期 1-30 天；窗口 9-18；批次 100；案件 DEMO-001。</p>
     */
    @Test
    public void testValidTask() {
        System.out.println("\ntestValidTask ---> 期望：名称复核任务；逾期 1-30 天；窗口 9-18；批次 100；案件 DEMO-001");
        List<String> ids = new ArrayList<String>();
        ids.add("DEMO-001");
        CollectionTask task = new CollectionTask();
        task.setTaskName("复核任务");
        task.setMinOverdueDays(1);
        task.setMaxOverdueDays(30);
        task.setStartHour(9);
        task.setEndHour(18);
        task.setBatchSize(100);
        task.setCaseIds(ids);
        System.out.println("实际配置：" + task.preview());
    }

    /**
     * testBlankName：直接调用观察业务边界。
     * <p>期望结果：空名称抛出 IllegalArgumentException。</p>
     */
    @Test
    public void testBlankName() {
        System.out.println("\ntestBlankName ---> 期望：空名称抛出 IllegalArgumentException");
        try {
            List<String> ids = new ArrayList<String>();
            ids.add("DEMO-001");
            CollectionTask task = new CollectionTask();
            task.setTaskName(" ");
            task.setMinOverdueDays(1);
            task.setMaxOverdueDays(30);
            task.setStartHour(9);
            task.setEndHour(18);
            task.setBatchSize(100);
            task.setCaseIds(ids);
            task.preview();
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testReversedDays：直接调用观察业务边界。
     * <p>期望结果：逾期范围反转抛出 IllegalArgumentException。</p>
     */
    @Test
    public void testReversedDays() {
        System.out.println("\ntestReversedDays ---> 期望：逾期范围反转抛出 IllegalArgumentException");
        try {
            List<String> ids = new ArrayList<String>();
            ids.add("DEMO-001");
            CollectionTask task = new CollectionTask();
            task.setTaskName("复核任务");
            task.setMinOverdueDays(31);
            task.setMaxOverdueDays(30);
            task.setStartHour(9);
            task.setEndHour(18);
            task.setBatchSize(100);
            task.setCaseIds(ids);
            task.preview();
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNegativeDays：直接调用观察业务边界。
     * <p>期望结果：逾期天数为负抛出 IllegalArgumentException。</p>
     */
    @Test
    public void testNegativeDays() {
        System.out.println("\ntestNegativeDays ---> 期望：逾期天数为负抛出 IllegalArgumentException");
        try {
            List<String> ids = new ArrayList<String>();
            ids.add("DEMO-001");
            CollectionTask task = new CollectionTask();
            task.setTaskName("复核任务");
            task.setMinOverdueDays(-1);
            task.setMaxOverdueDays(30);
            task.setStartHour(9);
            task.setEndHour(18);
            task.setBatchSize(100);
            task.setCaseIds(ids);
            task.preview();
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testInvalidWindow：直接调用观察业务边界。
     * <p>期望结果：开始与结束相同抛出 IllegalArgumentException。</p>
     */
    @Test
    public void testInvalidWindow() {
        System.out.println("\ntestInvalidWindow ---> 期望：开始与结束相同抛出 IllegalArgumentException");
        try {
            List<String> ids = new ArrayList<String>();
            ids.add("DEMO-001");
            CollectionTask task = new CollectionTask();
            task.setTaskName("复核任务");
            task.setMinOverdueDays(1);
            task.setMaxOverdueDays(30);
            task.setStartHour(18);
            task.setEndHour(18);
            task.setBatchSize(100);
            task.setCaseIds(ids);
            task.preview();
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testLateWindow：直接调用观察业务边界。
     * <p>期望结果：结束超过 24抛出 IllegalArgumentException。</p>
     */
    @Test
    public void testLateWindow() {
        System.out.println("\ntestLateWindow ---> 期望：结束超过 24抛出 IllegalArgumentException");
        try {
            List<String> ids = new ArrayList<String>();
            ids.add("DEMO-001");
            CollectionTask task = new CollectionTask();
            task.setTaskName("复核任务");
            task.setMinOverdueDays(1);
            task.setMaxOverdueDays(30);
            task.setStartHour(9);
            task.setEndHour(25);
            task.setBatchSize(100);
            task.setCaseIds(ids);
            task.preview();
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testInvalidBatch：直接调用观察业务边界。
     * <p>期望结果：批次为 0抛出 IllegalArgumentException。</p>
     */
    @Test
    public void testInvalidBatch() {
        System.out.println("\ntestInvalidBatch ---> 期望：批次为 0抛出 IllegalArgumentException");
        try {
            List<String> ids = new ArrayList<String>();
            ids.add("DEMO-001");
            CollectionTask task = new CollectionTask();
            task.setTaskName("复核任务");
            task.setMinOverdueDays(1);
            task.setMaxOverdueDays(30);
            task.setStartHour(9);
            task.setEndHour(18);
            task.setBatchSize(0);
            task.setCaseIds(ids);
            task.preview();
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testEmptyCases：直接调用观察业务边界。
     * <p>期望结果：案件列表为空抛出 IllegalArgumentException。</p>
     */
    @Test
    public void testEmptyCases() {
        System.out.println("\ntestEmptyCases ---> 期望：案件列表为空抛出 IllegalArgumentException");
        try {
            List<String> ids = new ArrayList<String>();
            ids.add("DEMO-001");
            CollectionTask task = new CollectionTask();
            task.setTaskName("复核任务");
            task.setMinOverdueDays(1);
            task.setMaxOverdueDays(30);
            task.setStartHour(9);
            task.setEndHour(18);
            task.setBatchSize(100);
            task.setCaseIds(new ArrayList<String>());
            task.preview();
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testNullCases：直接调用观察业务边界。
     * <p>期望结果：案件列表为 null抛出 IllegalArgumentException。</p>
     */
    @Test
    public void testNullCases() {
        System.out.println("\ntestNullCases ---> 期望：案件列表为 null抛出 IllegalArgumentException");
        try {
            List<String> ids = new ArrayList<String>();
            ids.add("DEMO-001");
            CollectionTask task = new CollectionTask();
            task.setTaskName("复核任务");
            task.setMinOverdueDays(1);
            task.setMaxOverdueDays(30);
            task.setStartHour(9);
            task.setEndHour(18);
            task.setBatchSize(100);
            task.setCaseIds(null);
            task.preview();
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testDuplicateIds：直接调用观察业务边界。
     * <p>期望结果：重复案件编号抛出 IllegalArgumentException。</p>
     */
    @Test
    public void testDuplicateIds() {
        System.out.println("\ntestDuplicateIds ---> 期望：重复案件编号抛出 IllegalArgumentException");
        try {
            List<String> ids = new ArrayList<String>();
            ids.add("DEMO-001");
            ids.add("DEMO-001");
            CollectionTask task = new CollectionTask();
            task.setTaskName("复核任务");
            task.setMinOverdueDays(1);
            task.setMaxOverdueDays(30);
            task.setStartHour(9);
            task.setEndHour(18);
            task.setBatchSize(100);
            task.setCaseIds(ids);
            task.preview();
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testInputIsolation：直接调用观察业务边界。
     * <p>期望结果：构造或设置后更改源列表，不改变 task 内部案件列表；仍只有 DEMO-001。</p>
     */
    @Test
    public void testInputIsolation() {
        System.out.println("\ntestInputIsolation ---> 期望：构造或设置后更改源列表，不改变 task 内部案件列表；仍只有 DEMO-001");
        List<String> ids = new ArrayList<String>();
        ids.add("DEMO-001");
        CollectionTask task = new CollectionTask();
        task.setTaskName("复核任务");
        task.setMinOverdueDays(1);
        task.setMaxOverdueDays(30);
        task.setStartHour(9);
        task.setEndHour(18);
        task.setBatchSize(100);
        task.setCaseIds(ids);
        ids.add("DEMO-002");
        System.out.println("任务案件：" + task.getCaseIds());
    }

    /**
     * testReadOnlyCases：直接调用观察业务边界。
     * <p>期望结果：修改 getCaseIds 返回值抛出 UnsupportedOperationException。</p>
     */
    @Test
    public void testReadOnlyCases() {
        System.out.println("\ntestReadOnlyCases ---> 期望：修改 getCaseIds 返回值抛出 UnsupportedOperationException");
        try {
            List<String> ids = new ArrayList<String>();
            ids.add("DEMO-001");
            CollectionTask task = new CollectionTask();
            task.setTaskName("复核任务");
            task.setMinOverdueDays(1);
            task.setMaxOverdueDays(30);
            task.setStartHour(9);
            task.setEndHour(18);
            task.setBatchSize(100);
            task.setCaseIds(ids);
            task.getCaseIds().add("DEMO-002");
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }

    /**
     * testIncompleteObject：直接调用观察业务边界。
     * <p>期望结果：新对象尚未设置参数，preview 抛出 IllegalArgumentException。</p>
     */
    @Test
    public void testIncompleteObject() {
        System.out.println("\ntestIncompleteObject ---> 期望：新对象尚未设置参数，preview 抛出 IllegalArgumentException");
        try {
            new CollectionTask().preview();
            System.out.println("实际结果：未抛出异常，请对照期望检查");
        } catch (RuntimeException exception) {
            System.out.println("实际异常类型：" + exception.getClass().getSimpleName());
            System.out.println("实际异常信息：" + exception.getMessage());
        }
    }
}
