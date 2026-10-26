package com.advance.patterns.behavioral.templatemethod.v2.service;

import com.advance.patterns.behavioral.templatemethod.v2.domain.PaymentCommand;
import java.util.Objects;

/**
 * 支付公共步骤助手，仅复用步骤实现，不控制整体执行顺序。
 *
 * @author advance
 * @since 2026-10-26 23:05:19
 */
final class PaymentSteps {

    /**
     * 创建 PaymentSteps 实例并初始化依赖或业务数据。
     *
     */
    PaymentSteps() {
    }

    /**
     * 校验业务输入；参数不合法时终止后续处理。
     *
     * @param command 待校验或执行的支付命令
     */
    void validate(PaymentCommand command) { Objects.requireNonNull(command, "支付命令 command 不能为空").validate(); }
    /**
     * 记录支付前置步骤。
     */
    void before() { System.out.println("[template-method/v2] PaymentSteps ---> 开始执行前置步骤");  }
    /**
     * 记录支付流程成功步骤。
     */
    void success() { System.out.println("[template-method/v2] PaymentSteps ---> 业务步骤执行成功");  }
    /**
     * 记录支付流程异常步骤。
     */
    void failure() { System.out.println("[template-method/v2] PaymentSteps ---> 业务步骤执行失败");  }
    /**
     * 记录支付流程结束步骤。
     */
    void complete() { System.out.println("[template-method/v2] PaymentSteps ---> 流程执行结束");  }
}
