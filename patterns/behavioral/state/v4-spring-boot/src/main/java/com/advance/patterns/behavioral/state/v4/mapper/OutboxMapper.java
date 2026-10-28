package com.advance.patterns.behavioral.state.v4.mapper;

import com.advance.patterns.behavioral.state.v4.domain.OutboxRecord;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * MyBatis Outbox 接口；入库参与状态迁移事务，教学投递器随后独立更新投递标记。
 *
 * @author advance
 * @since 2026-10-28 20:58:50
 */
@Mapper
public interface OutboxMapper {

    /**
     * 保存状态迁移事件，方案编号与迁移版本组合唯一。
     *
     * @param record 与本次迁移对应的完整事件记录
     * @return 插入行数
     */
    @Insert("INSERT INTO settlement_outbox "
            + "(event_id, plan_id, plan_version, event, from_status, to_status, delivery_status) "
            + "VALUES (#{eventId}, #{planId}, #{planVersion}, #{event}, #{fromStatus}, #{toStatus}, 'NEW')")
    int insert(OutboxRecord record);

    /**
     * 查询未投递事件，仅供单个手动教学投递器使用。
     *
     * @return 按方案与版本排序的待投递事件
     */
    @Select("SELECT * FROM settlement_outbox WHERE delivery_status = 'NEW' ORDER BY plan_id, plan_version")
    List<OutboxRecord> findPending();

    /**
     * 在模拟发送返回后标记已投递，不宣称与外部发送原子提交。
     *
     * @param eventId 事件编号
     * @return 标记成功为 1
     */
    @Update("UPDATE settlement_outbox SET delivery_status = 'SENT' "
            + "WHERE event_id = #{eventId} AND delivery_status = 'NEW'")
    int markSent(@Param("eventId") String eventId);
}
