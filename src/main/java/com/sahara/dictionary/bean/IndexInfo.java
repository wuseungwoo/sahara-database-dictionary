package com.sahara.dictionary.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 索引
 */
@NoArgsConstructor
@ToString
@Data
public class IndexInfo {
    /**
     * 索引是否为主键
     */
    private int isIndex=0;
    /**
     * 索引序号
     */
    private int order;
    /**
     * 索引名称
     */
    private String name= "";
    /**
     * 索引类型
     */
    private String type= "";
    /**
     * 索引包含keys
     */
    private String containKey= "";

    public IndexInfo(String name, String type, String drop) {
        this.name = name;
        this.type = type;
        this.containKey = drop;
    }
}
