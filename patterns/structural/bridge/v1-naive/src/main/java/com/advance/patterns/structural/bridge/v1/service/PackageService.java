package com.advance.patterns.structural.bridge.v1.service;

import com.advance.patterns.structural.bridge.v1.domain.MaterialPackage;
import com.advance.patterns.structural.bridge.v1.channel.InternalChannel;
import com.advance.patterns.structural.bridge.v1.channel.ExternalAgencyChannel;

/**
 * 把场景和输出渠道组合在同一个服务中，展示二维变化导致分支膨胀。
 *
 * @author advance
 * @since 2026-10-28 22:05:00
 */
public class PackageService {
    /**
     * 根据输出渠道分支生成并输出材料包。
     *
     * @param pkg 待输出的材料包数据
     * @param channel 渠道代码，仅支持 INTERNAL 或 EXTERNAL
     * @return 模拟渠道输出结果
     */
    public String export(MaterialPackage pkg, String channel) {
        System.out.println("[桥接/v1] PackageService.export ---> 同时判断材料场景和输出渠道");
        String content = pkg.getScene() + "材料包，案件=" + pkg.getCaseId();
        if ("INTERNAL".equals(channel)) {
            return new InternalChannel().send(content);
        }
        if ("EXTERNAL".equals(channel)) {
            return new ExternalAgencyChannel().send(content);
        }
        throw new IllegalArgumentException("未知输出渠道：" + channel);
    }
}
