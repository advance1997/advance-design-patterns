package com.advance.patterns.behavioral.state.v4.web;

import com.advance.patterns.behavioral.state.v4.domain.InvalidTransitionException;
import com.advance.patterns.behavioral.state.v4.domain.PlanNotFoundException;
import com.advance.patterns.behavioral.state.v4.domain.StateConflictException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 将教学中的非法事件、过期版本与不存在方案映射成清晰响应，不返回数据库异常细节。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
@RestControllerAdvice
public class SettlementExceptionAdvice {

    /**
     * 当前状态不允许事件或版本冲突时拒绝迁移。
     *
     * @param exception 业务冲突
     * @return HTTP 409 与中文原因
     */
    @ExceptionHandler({InvalidTransitionException.class, StateConflictException.class})
    public ResponseEntity<Map<String, String>> conflict(RuntimeException exception) {
        return response(409, exception.getMessage());
    }

    /**
     * Outbox 唯一键冲突发生在事务中，整个状态迁移会回滚。
     *
     * @param exception 数据库唯一键异常
     * @return 不暴露 SQL 的 HTTP 409 响应
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Map<String, String>> duplicate(DuplicateKeyException exception) {
        return response(409, "状态事件记录冲突，本次状态迁移已回滚");
    }

    /**
     * 读取不存在的教学方案。
     *
     * @param exception 方案不存在异常
     * @return HTTP 404
     */
    @ExceptionHandler(PlanNotFoundException.class)
    public ResponseEntity<Map<String, String>> notFound(PlanNotFoundException exception) {
        return response(404, exception.getMessage());
    }

    /**
     * 拒绝不完整事件或非法版本参数。
     *
     * @param exception 参数错误
     * @return HTTP 400
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> invalid(IllegalArgumentException exception) {
        return response(400, exception.getMessage());
    }

    /**
     * 拒绝无法解析的 JSON 或不存在的事件枚举。
     *
     * @param exception 消息解析异常
     * @return 不暴露解析内部信息的 HTTP 400
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> unreadable(HttpMessageNotReadableException exception) {
        return response(400, "请求必须包含合法的 event 和 expectedVersion");
    }

    /**
     * 统一构造简单错误响应。
     *
     * @param status HTTP 状态
     * @param message 可公开的中文说明
     * @return JSON 错误响应
     */
    private ResponseEntity<Map<String, String>> response(int status, String message) {
        System.out.println("[状态/v4] SettlementExceptionAdvice.response ---> 拒绝请求，status="
                + status + "，原因=" + message);
        Map<String, String> body = new LinkedHashMap<String, String>();
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}
