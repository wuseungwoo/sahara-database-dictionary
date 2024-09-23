package com.sahara.dictionary.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 表信息
 * 序号，系统名，模块名，表名，列名，数据类型，长度，小数位数，主键，允许空，默认值，列说明，备注，创建人，创建时间，修改人，修改时间，当前版本
 */
@NoArgsConstructor
@ToString
@Data
public class TableInfo {
    /**
     * id序号
     */
    private Integer id;
    /**
     * 系统名
     */
    private String systemName= "";
    /**
     * 模块名
     */
    private String moduleName= "";
    /**
     * 数据库名
     */
    private String dbName= "";
    /**
     * 表名
     */
    private String tableName= "";
    /**
     * 字符集
     */
    private String orderType= "";
    /**
     * 存储引擎
     */
    private String storageEngine= "";
    /**
     * 表描述
     */
    private String description= "";

    /**
     * 所有列名
     */
    private List<ColumnInfo> columnList;

    /**
     * 索引列如果有
     */
    private List<IndexInfo> indexInfoList;
}
