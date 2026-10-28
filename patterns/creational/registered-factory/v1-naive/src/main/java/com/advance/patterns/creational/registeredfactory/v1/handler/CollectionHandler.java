package com.advance.patterns.creational.registeredfactory.v1.handler;

import com.advance.patterns.creational.registeredfactory.v1.domain.CaseFile;
import com.advance.patterns.creational.registeredfactory.v1.domain.CaseStage;

/**
 * 案件人工复核处理器契约；不创建自身、不查找其他处理器，不发起真实触达。
 *
 * @author advance
 * @since 2026-10-27 20:33:03
 */
public interface CollectionHandler {
    /**
     * 声明负责阶段，实例生命周期内保持不变。
     * @return 非空阶段
     */
    CaseStage getStage();

    /**
     * 生成模拟人工复核结果。
     * @param caseFile 与处理器阶段匹配的案件
     * @return 模拟复核标识
     * @throws IllegalArgumentException 案件为空或阶段不匹配
     */
    String handle(CaseFile caseFile);
}
