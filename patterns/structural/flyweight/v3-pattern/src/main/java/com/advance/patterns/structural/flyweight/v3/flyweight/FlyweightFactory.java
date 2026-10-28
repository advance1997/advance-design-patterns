package com.advance.patterns.structural.flyweight.v3.flyweight;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 按规则键缓存共享享元；同一 code/version 返回同一个对象。
 *
 * @author advance
 * @since 2026-10-28 21:22:00
 */
public class FlyweightFactory {
    private final Map<String, MaterialRuleFlyweight> cache = new LinkedHashMap<String, MaterialRuleFlyweight>();
    public MaterialRuleFlyweight get(String code, boolean required) {
        String key = code + ":" + required;
        MaterialRuleFlyweight value = cache.get(key);
        if (value == null) {
            System.out.println("[享元/v3] FlyweightFactory.get ---> 缓存未命中，创建 key=" + key);
            value = new MaterialRuleFlyweight(code, required); cache.put(key, value);
        } else {
            System.out.println("[享元/v3] FlyweightFactory.get ---> 缓存命中，复用 key=" + key);
        }
        return value;
    }
    public int size() { return cache.size(); }
    public Map<String, MaterialRuleFlyweight> snapshot() { return Collections.unmodifiableMap(cache); }
}
