package com.sahara.dictionary.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * 列信息
 */
@ToString
@NoArgsConstructor
@Data
public class ColumnInfo {
    /**
     * 列名
     */
    private String name= "";
    /**
     * 类型
     */
    private String type= "";
    /**
     * 长度
     */
    private Long length;
    /**
     * 小数位数
     */
    private Integer decimalNums;
    /**
     * 是否为主键
     */
    private int isIndex;
    /**
     * 序号
     */
    private int order;
    /**
     * 是否允许为空
     */
    private String isNull= "";
    /**
     * 默认值
     */
    private String defaultValue= "";
    /**
     * 列描述
     */
    private String description= "";

    /**
     * 备注
     */
    private String remark= "";
    /**
     * 创建人
     */
    private Date createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改人
     */
    private Date updateBy;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 版本号
     */
    private String SystemVersion= "";
}
