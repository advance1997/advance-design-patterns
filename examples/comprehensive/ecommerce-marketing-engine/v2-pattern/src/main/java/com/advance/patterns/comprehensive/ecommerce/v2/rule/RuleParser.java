package com.advance.patterns.comprehensive.ecommerce.v2.rule;

import com.advance.patterns.comprehensive.ecommerce.v2.expression.AndExpression;
import com.advance.patterns.comprehensive.ecommerce.v2.expression.ConditionExpression;
import com.advance.patterns.comprehensive.ecommerce.v2.expression.Expression;
import com.advance.patterns.comprehensive.ecommerce.v2.expression.NotExpression;
import com.advance.patterns.comprehensive.ecommerce.v2.expression.OrExpression;

/**
 * 将优惠规则 DSL 解析为 AST；解析职责与 AST 执行职责分开。
 *
 * <p>语法优先级为 NOT、AND、OR；支持括号、vip、firstOrder、amount500、electronics。</p>
 *
 * @author advance
 * @since 2026-10-29 13:45:00
 */
public class RuleParser {
    /** 待解析的规则源文本。 */
    private final String source;
    /** 当前读取位置。 */
    private int position;

    /**
     * 创建一次规则解析器。
     *
     * @param source 优惠条件 DSL
     * @throws IllegalArgumentException 规则文本为空时抛出
     */
    public RuleParser(String source) {
        if (source == null || source.trim().isEmpty()) {
            throw new IllegalArgumentException("规则表达式不能为空");
        }
        this.source = source;
    }

    /**
     * 解析完整表达式，并拒绝未消费的尾部字符。
     *
     * @return 表达式 AST 根节点
     */
    public Expression parse() {
        Expression expression = parseOr();
        skipWhitespace();
        if (position != source.length()) {
            throw syntaxError("存在无法识别的尾部内容");
        }
        System.out.println("[优惠引擎/v2] RuleParser.parse ---> 规则解析完成：" + expression.describe());
        return expression;
    }

    /**
     * 按最低运算优先级解析一个或多个 OR 项。
     *
     * @return OR 表达式或其唯一子表达式
     */
    private Expression parseOr() {
        Expression expression = parseAnd();
        while (match("||")) {
            expression = new OrExpression(expression, parseAnd());
        }
        return expression;
    }

    /**
     * 解析一个或多个 AND 项。
     *
     * @return AND 表达式或其唯一子表达式
     */
    private Expression parseAnd() {
        Expression expression = parseUnary();
        while (match("&&")) {
            expression = new AndExpression(expression, parseUnary());
        }
        return expression;
    }

    /**
     * 解析一元 NOT、括号表达式或原子条件。
     *
     * @return 当前优先级表达式节点
     */
    private Expression parseUnary() {
        if (match("!")) {
            return new NotExpression(parseUnary());
        }
        if (match("(")) {
            Expression expression = parseOr();
            if (!match(")")) {
                throw syntaxError("缺少右括号");
            }
            return expression;
        }
        return parseCondition();
    }

    /**
     * 读取并验证一个原子条件标识符。
     *
     * @return 条件表达式叶子节点
     */
    private Expression parseCondition() {
        skipWhitespace();
        int start = position;
        while (position < source.length()) {
            char current = source.charAt(position);
            if (!Character.isLetterOrDigit(current) && current != '_') {
                break;
            }
            position++;
        }
        if (start == position) {
            throw syntaxError("此处需要条件名称");
        }
        String condition = source.substring(start, position);
        try {
            return new ConditionExpression(condition);
        } catch (IllegalArgumentException exception) {
            throw syntaxError("不支持的规则条件：" + condition);
        }
    }

    /**
     * 忽略空白并尝试读取一个语法符号。
     *
     * @param token 待匹配的语法符号
     * @return 匹配并消费符号时返回 true
     */
    private boolean match(String token) {
        skipWhitespace();
        if (source.startsWith(token, position)) {
            position += token.length();
            return true;
        }
        return false;
    }

    /**
     * 将解析位置移动到下一个非空白字符。
     */
    private void skipWhitespace() {
        while (position < source.length()
                && Character.isWhitespace(source.charAt(position))) {
            position++;
        }
    }

    /**
     * 构造带位置和原文的 DSL 语法异常。
     *
     * @param message 中文错误原因
     * @return 可抛出的非法参数异常
     */
    private IllegalArgumentException syntaxError(String message) {
        return new IllegalArgumentException("规则语法错误（位置 " + position + "）："
                + message + "；表达式：" + source);
    }
}
