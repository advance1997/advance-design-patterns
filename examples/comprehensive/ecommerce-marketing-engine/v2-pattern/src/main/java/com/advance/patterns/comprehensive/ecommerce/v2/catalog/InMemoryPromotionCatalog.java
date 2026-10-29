package com.advance.patterns.comprehensive.ecommerce.v2.catalog;

import com.advance.patterns.comprehensive.ecommerce.v2.domain.DiscountType;
import com.advance.patterns.comprehensive.ecommerce.v2.domain.Promotion;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 用不可变内存数据模拟营销活动配置查询，不连接 MySQL 或远程服务。
 *
 * @author advance
 * @since 2026-10-29 13:45:00
 */
public class InMemoryPromotionCatalog {
    /**
     * 创建专题示例中的三张优惠券配置。
     */
    public InMemoryPromotionCatalog() {
        System.out.println("[优惠引擎/v2] InMemoryPromotionCatalog 构造方法 ---> 装载模拟营销活动数据");
    }

    /**
     * 返回内存模拟的三个互斥活动，供本次订单逐个评估。
     *
     * @return 不可修改的营销活动列表
     */
    public List<Promotion> findAll() {
        List<Promotion> promotions = new ArrayList<Promotion>();
        promotions.add(new Promotion("VIP_FIXED", "VIP立减券", "vip && amount500",
                DiscountType.FIXED, new BigDecimal("20"), BigDecimal.ZERO));
        promotions.add(new Promotion("VIP_PERCENT", "VIP折扣券", "vip && amount500",
                DiscountType.PERCENTAGE, new BigDecimal("0.10"), new BigDecimal("50")));
        promotions.add(new Promotion("NEW_USER", "新客折扣券",
                "firstOrder && (amount500 || electronics)", DiscountType.PERCENTAGE,
                new BigDecimal("0.10"), new BigDecimal("30")));
        System.out.println("[优惠引擎/v2] InMemoryPromotionCatalog.findAll ---> 返回三条内存活动配置");
        return Collections.unmodifiableList(promotions);
    }
}
