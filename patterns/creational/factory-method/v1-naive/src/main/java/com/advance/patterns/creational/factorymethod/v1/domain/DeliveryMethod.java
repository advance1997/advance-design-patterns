package com.advance.patterns.creational.factorymethod.v1.domain;

/**
 * 配送方式枚举，区分公路运输与海运。
 *
 * @author advance
 * @since 2026-10-26 23:05:19
 */
public enum DeliveryMethod {
    /** 公路运输，由卡车承运。 */
    ROAD,
    /** 海路运输，由船舶承运。 */
    SEA
}
