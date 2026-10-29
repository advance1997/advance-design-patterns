/**
 * 组合模式专题的v2-refactor实践版本。
 *
 * <p>业务场景：诉讼案件材料按“身份材料”“履约材料”等目录分组，目录下既可放材料，也可继续放子目录。示例只使用内存对象，不连接数据库、不调用远程服务。 核心结构： MaterialNode ├── MaterialLeaf（材料） └── MaterialGroup（目录，包含多个 MaterialNode） 打印树结构并统计必需材料数量是本例唯一的业务观察点，用来展示统一接口和递归组合。测试各版本只有一个打印式用例，标注期望路径，不使用断言。 mvn -f patterns/structural/composite/pom.xml test 源码兼容 JDK 1.8；每个版本独立编译和运行。</p>
 *
 * <p>本版本实现要点：把目录递归职责放进目录类，但材料与目录仍是不同类型；递归收敛了，调用方仍然需要区分节点类型。组合以统一接口表示叶子与容器，使客户端一致处理树形结构。</p>
 *
 * <p>具体调用路径请参见本版本目录下的流程图与时序图文档。</p>
 *
 * @author advance
 * @since 2026-10-29 15:47:52
 */
package com.advance.patterns.structural.composite.v2.domain;
