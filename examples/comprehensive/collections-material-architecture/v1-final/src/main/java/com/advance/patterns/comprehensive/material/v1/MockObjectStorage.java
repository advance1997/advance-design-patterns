package com.advance.patterns.comprehensive.material.v1;

import java.util.LinkedHashMap;
import java.util.Map;

/** 以内存映射模拟对象存储和确定性对象键写入。 @author advance @since 2026-10-30 12:00:00 */
public class MockObjectStorage {
    private final Map<String, MaterialPackage> objects = new LinkedHashMap<String, MaterialPackage>();
    /** @param key 确定性对象键 @param materialPackage 教学文件包描述 */
    public void put(String key, MaterialPackage materialPackage) {
        System.out.println("[最终架构/Mock Object Storage] put ---> 上传临时产物 " + key); objects.put(key, materialPackage);
    }
    /** @param key 对象键 @return 已保存文件包，不存在返回 null */ public MaterialPackage get(String key) { return objects.get(key); }
}
