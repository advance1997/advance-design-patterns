package com.advance.patterns.behavioral.state.v4.mapper;

import com.advance.patterns.behavioral.state.v4.domain.SettlementRecord;
import com.advance.patterns.behavioral.state.v4.domain.SettlementStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * MyBatis 方案持久化接口，使用状态与版本联合条件保护并发迁移。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
@Mapper
public interface SettlementMapper {

    /**
     * 读取当前方案快照。
     *
     * @param id 虚构方案编号
     * @return 数据库快照，不存在时为 null
     */
    @Select("SELECT id, status, version FROM settlement_plan WHERE id = #{id}")
    SettlementRecord find(@Param("id") long id);

    /**
     * 原子比较当前状态与版本，匹配时修改状态并递增版本。
     *
     * @param id 方案编号
     * @param before 读取到的原状态
     * @param after 状态处理器计算的新状态
     * @param version 预期旧版本
     * @return 成功为 1，旧版本或旧状态失效为 0
     */
    @Update("UPDATE settlement_plan SET status = #{after}, version = version + 1 "
            + "WHERE id = #{id} AND status = #{before} AND version = #{version}")
    int change(@Param("id") long id, @Param("before") SettlementStatus before,
            @Param("after") SettlementStatus after, @Param("version") long version);
}
