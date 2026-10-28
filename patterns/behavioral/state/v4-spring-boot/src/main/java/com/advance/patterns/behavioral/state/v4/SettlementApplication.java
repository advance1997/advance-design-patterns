package com.advance.patterns.behavioral.state.v4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 教学入口，自动装配 MVC、H2 数据源、事务管理及 MyBatis 映射接口。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
@SpringBootApplication
public class SettlementApplication {

    /**
     * 启动仅使用虚构数据的本地教学应用。
     *
     * @param args Spring Boot 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(SettlementApplication.class, args);
    }
}
