package com.advance.patterns.structural.proxy.v4.config;

import com.advance.patterns.structural.proxy.v4.database.DemoDatabase;
import com.advance.patterns.structural.proxy.v4.repository.CaseRepository;
import com.advance.patterns.structural.proxy.v4.service.AssignmentService;
import com.advance.patterns.structural.proxy.v4.service.CaseAssignmentService;
import com.advance.patterns.structural.proxy.v4.service.AuditService;
import com.advance.patterns.structural.proxy.v4.service.AssignmentAuditService;
import com.advance.patterns.structural.proxy.v4.tx.TracingTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 集中配置虚构业务依赖，确保仓储和事务管理器使用相同数据源。
 *
 * @author advance
 * @since 2026-10-28 10:15:29
 */
@Configuration(proxyBeanMethods = false)
public class BusinessConfig {
    /**
     * 每个上下文独有的教学库。
     * @return 已初始化教学库
     * @throws SQLException 建库失败
     */
    @Bean(destroyMethod = "close")
    public DemoDatabase demoDatabase() throws SQLException {
        return new DemoDatabase();
    }

    /**
     * 导出同一个数据源供事务管理器和仓储使用。
     * @param database 教学库
     * @return 数据源
     */
    @Bean
    public DataSource dataSource(DemoDatabase database) {
        return database.getDataSource();
    }

    /**
     * 创建事务管理器。
     * @param dataSource 与仓储相同的数据源
     * @return 事务管理器
     */
    @Bean
    public PlatformTransactionManager transactionManager(
            DataSource dataSource) {
        return new TracingTransactionManager(dataSource);
    }

    /**
     * 创建案件仓储。
     * @param dataSource 教学数据源
     * @return 案件仓储
     */
    @Bean
    public CaseRepository caseRepository(DataSource dataSource) {
        return new CaseRepository(dataSource);
    }

    /**
     * 创建独立的内层事务参与者。
     * @param repository 案件仓储
     * @return 记录服务
     */
    @Bean
    public AuditService auditService(CaseRepository repository) {
        return new AssignmentAuditService(repository);
    }

    /**
     * 只按接口注入内层服务，兼容 JDK 代理类型。
     * @param repository 案件仓储
     * @param auditService 内层记录服务代理
     * @return 分派服务
     */
    @Bean
    public AssignmentService assignmentService(CaseRepository repository, AuditService auditService) {
        return new CaseAssignmentService(repository, auditService);
    }
}
