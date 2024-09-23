package com.sahara.dictionary.dto;

import com.sahara.dictionary.bean.ColumnInfo;
import com.sahara.dictionary.bean.TableInfo;

import java.util.Date;

/**
 * This class corresponds to the database table table_column_meta <br>
 *
 * @mbg.generated do_not_delete_during_merge
 */
public class TableColumnMeta {
    /**
     * database comment: id序号,<br>source field: table_column_meta.id
     */
    private Integer id;

    /**
     * database comment: 系统名,<br>source field: table_column_meta.system_name
     */
    private String systemName;

    /**
     * database comment: 模块名,<br>source field: table_column_meta.module_name
     */
    private String moduleName;

    /**
     * database comment: 数据库名,<br>source field: table_column_meta.db_name
     */
    private String dbName;

    /**
     * database comment: 表名,<br>source field: table_column_meta.table_name
     */
    private String tableName;

    /**
     * database comment: 字符集,<br>source field: table_column_meta.order_type
     */
    private String orderType;

    /**
     * database comment: 存储引擎,<br>source field: table_column_meta.storage_engine
     */
    private String storageEngine;

    /**
     * database comment: 列名,<br>source field: table_column_meta.column_name
     */
    private String columnName;

    /**
     * database comment: 类型,<br>source field: table_column_meta.column_type
     */
    private String columnType;

    /**
     * database comment: 长度,<br>source field: table_column_meta.column_length
     */
    private Long columnLength;

    /**
     * database comment: 小数位数,<br>source field: table_column_meta.column_decimal_nums
     */
    private Integer columnDecimalNums;

    /**
     * database comment: 是否为主键,<br>source field: table_column_meta.column_is_index
     */
    private Integer columnIsIndex;

    /**
     * database comment: 序号,<br>source field: table_column_meta.column_order
     */
    private Integer columnOrder;

    /**
     * database comment: 是否允许为空,<br>source field: table_column_meta.column_is_null
     */
    private String columnIsNull;

    /**
     * database comment: 默认值,<br>source field: table_column_meta.column_default_value
     */
    private String columnDefaultValue;

    /**
     * database comment: 创建人,<br>source field: table_column_meta.column_create_by
     */
    private Date columnCreateBy;

    /**
     * database comment: 创建时间,<br>source field: table_column_meta.column_create_time
     */
    private Date columnCreateTime;

    /**
     * database comment: 修改人,<br>source field: table_column_meta.column_update_by
     */
    private Date columnUpdateBy;

    /**
     * database comment: 修改时间,<br>source field: table_column_meta.column_update_time
     */
    private Date columnUpdateTime;

    /**
     * database comment: 版本号,<br>source field: table_column_meta.system_version
     */
    private String systemVersion;

    /**
     * database comment: 表描述,<br>source field: table_column_meta.description
     */
    private String description;

    /**
     * database comment: 列描述,<br>source field: table_column_meta.column_description
     */
    private String columnDescription;

    /**
     * database comment: 备注,<br>source field: table_column_meta.column_remark
     */
    private String columnRemark;

    /**
     * transform TableInfo to TableColumnMeta
     *
     * @param tableInfo
     */
    public TableColumnMeta(TableInfo tableInfo) {
        this.systemName = tableInfo.getSystemName();
        this.moduleName = tableInfo.getModuleName();
        this.dbName = tableInfo.getDbName();
        this.tableName = tableInfo.getTableName();
        this.orderType = tableInfo.getOrderType();
        this.storageEngine = tableInfo.getStorageEngine();
        this.description = tableInfo.getDescription();
        for (ColumnInfo columnInfo : tableInfo.getColumnList()) {
            this.columnName = columnInfo.getName();
            this.columnType = columnInfo.getType();
            this.columnLength = columnInfo.getLength();
            this.columnDecimalNums = columnInfo.getDecimalNums();
            this.columnIsIndex = columnInfo.getIsIndex();
            this.columnOrder = columnInfo.getOrder();
            this.columnIsNull = columnInfo.getIsNull();
            this.columnDefaultValue = columnInfo.getDefaultValue();
            this.columnDescription = columnInfo.getDescription();
            this.columnRemark = columnInfo.getRemark();
            this.columnCreateBy = columnInfo.getCreateBy();
            this.columnCreateTime = columnInfo.getCreateTime();
            this.columnUpdateBy = columnInfo.getUpdateBy();
            this.columnUpdateTime = columnInfo.getUpdateTime();
            this.systemVersion = columnInfo.getSystemVersion();
        }
    }

    /**
     * database comment: id序号,<br>source field: table_column_meta.id
     */
    public Integer getId() {
        return id;
    }

    /**
     * database comment: id序号,<br>source field: table_column_meta.id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * database comment: 系统名,<br>source field: table_column_meta.system_name
     */
    public String getSystemName() {
        return systemName;
    }

    /**
     * database comment: 系统名,<br>source field: table_column_meta.system_name
     */
    public void setSystemName(String systemName) {
        this.systemName = systemName == null ? null : systemName.trim();
    }

    /**
     * database comment: 模块名,<br>source field: table_column_meta.module_name
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * database comment: 模块名,<br>source field: table_column_meta.module_name
     */
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName == null ? null : moduleName.trim();
    }

    /**
     * database comment: 数据库名,<br>source field: table_column_meta.db_name
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * database comment: 数据库名,<br>source field: table_column_meta.db_name
     */
    public void setDbName(String dbName) {
        this.dbName = dbName == null ? null : dbName.trim();
    }

    /**
     * database comment: 表名,<br>source field: table_column_meta.table_name
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * database comment: 表名,<br>source field: table_column_meta.table_name
     */
    public void setTableName(String tableName) {
        this.tableName = tableName == null ? null : tableName.trim();
    }

    /**
     * database comment: 字符集,<br>source field: table_column_meta.order_type
     */
    public String getOrderType() {
        return orderType;
    }

    /**
     * database comment: 字符集,<br>source field: table_column_meta.order_type
     */
    public void setOrderType(String orderType) {
        this.orderType = orderType == null ? null : orderType.trim();
    }

    /**
     * database comment: 存储引擎,<br>source field: table_column_meta.storage_engine
     */
    public String getStorageEngine() {
        return storageEngine;
    }

    /**
     * database comment: 存储引擎,<br>source field: table_column_meta.storage_engine
     */
    public void setStorageEngine(String storageEngine) {
        this.storageEngine = storageEngine == null ? null : storageEngine.trim();
    }

    /**
     * database comment: 列名,<br>source field: table_column_meta.column_name
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * database comment: 列名,<br>source field: table_column_meta.column_name
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName == null ? null : columnName.trim();
    }

    /**
     * database comment: 类型,<br>source field: table_column_meta.column_type
     */
    public String getColumnType() {
        return columnType;
    }

    /**
     * database comment: 类型,<br>source field: table_column_meta.column_type
     */
    public void setColumnType(String columnType) {
        this.columnType = columnType == null ? null : columnType.trim();
    }

    /**
     * database comment: 长度,<br>source field: table_column_meta.column_length
     */
    public Long getColumnLength() {
        return columnLength;
    }

    /**
     * database comment: 长度,<br>source field: table_column_meta.column_length
     */
    public void setColumnLength(Long columnLength) {
        this.columnLength = columnLength;
    }

    /**
     * database comment: 小数位数,<br>source field: table_column_meta.column_decimal_nums
     */
    public Integer getColumnDecimalNums() {
        return columnDecimalNums;
    }

    /**
     * database comment: 小数位数,<br>source field: table_column_meta.column_decimal_nums
     */
    public void setColumnDecimalNums(Integer columnDecimalNums) {
        this.columnDecimalNums = columnDecimalNums;
    }

    /**
     * database comment: 是否为主键,<br>source field: table_column_meta.column_is_index
     */
    public Integer getColumnIsIndex() {
        return columnIsIndex;
    }

    /**
     * database comment: 是否为主键,<br>source field: table_column_meta.column_is_index
     */
    public void setColumnIsIndex(Integer columnIsIndex) {
        this.columnIsIndex = columnIsIndex;
    }

    /**
     * database comment: 序号,<br>source field: table_column_meta.column_order
     */
    public Integer getColumnOrder() {
        return columnOrder;
    }

    /**
     * database comment: 序号,<br>source field: table_column_meta.column_order
     */
    public void setColumnOrder(Integer columnOrder) {
        this.columnOrder = columnOrder;
    }

    /**
     * database comment: 是否允许为空,<br>source field: table_column_meta.column_is_null
     */
    public String getColumnIsNull() {
        return columnIsNull;
    }

    /**
     * database comment: 是否允许为空,<br>source field: table_column_meta.column_is_null
     */
    public void setColumnIsNull(String columnIsNull) {
        this.columnIsNull = columnIsNull == null ? null : columnIsNull.trim();
    }

    /**
     * database comment: 默认值,<br>source field: table_column_meta.column_default_value
     */
    public String getColumnDefaultValue() {
        return columnDefaultValue;
    }

    /**
     * database comment: 默认值,<br>source field: table_column_meta.column_default_value
     */
    public void setColumnDefaultValue(String columnDefaultValue) {
        this.columnDefaultValue = columnDefaultValue == null ? null : columnDefaultValue.trim();
    }

    /**
     * database comment: 创建人,<br>source field: table_column_meta.column_create_by
     */
    public Date getColumnCreateBy() {
        return columnCreateBy;
    }

    /**
     * database comment: 创建人,<br>source field: table_column_meta.column_create_by
     */
    public void setColumnCreateBy(Date columnCreateBy) {
        this.columnCreateBy = columnCreateBy;
    }

    /**
     * database comment: 创建时间,<br>source field: table_column_meta.column_create_time
     */
    public Date getColumnCreateTime() {
        return columnCreateTime;
    }

    /**
     * database comment: 创建时间,<br>source field: table_column_meta.column_create_time
     */
    public void setColumnCreateTime(Date columnCreateTime) {
        this.columnCreateTime = columnCreateTime;
    }

    /**
     * database comment: 修改人,<br>source field: table_column_meta.column_update_by
     */
    public Date getColumnUpdateBy() {
        return columnUpdateBy;
    }

    /**
     * database comment: 修改人,<br>source field: table_column_meta.column_update_by
     */
    public void setColumnUpdateBy(Date columnUpdateBy) {
        this.columnUpdateBy = columnUpdateBy;
    }

    /**
     * database comment: 修改时间,<br>source field: table_column_meta.column_update_time
     */
    public Date getColumnUpdateTime() {
        return columnUpdateTime;
    }

    /**
     * database comment: 修改时间,<br>source field: table_column_meta.column_update_time
     */
    public void setColumnUpdateTime(Date columnUpdateTime) {
        this.columnUpdateTime = columnUpdateTime;
    }

    /**
     * database comment: 版本号,<br>source field: table_column_meta.system_version
     */
    public String getSystemVersion() {
        return systemVersion;
    }

    /**
     * database comment: 版本号,<br>source field: table_column_meta.system_version
     */
    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion == null ? null : systemVersion.trim();
    }

    /**
     * database comment: 表描述,<br>source field: table_column_meta.description
     */
    public String getDescription() {
        return description;
    }

    /**
     * database comment: 表描述,<br>source field: table_column_meta.description
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * database comment: 列描述,<br>source field: table_column_meta.column_description
     */
    public String getColumnDescription() {
        return columnDescription;
    }

    /**
     * database comment: 列描述,<br>source field: table_column_meta.column_description
     */
    public void setColumnDescription(String columnDescription) {
        this.columnDescription = columnDescription == null ? null : columnDescription.trim();
    }

    /**
     * database comment: 备注,<br>source field: table_column_meta.column_remark
     */
    public String getColumnRemark() {
        return columnRemark;
    }

    /**
     * database comment: 备注,<br>source field: table_column_meta.column_remark
     */
    public void setColumnRemark(String columnRemark) {
        this.columnRemark = columnRemark == null ? null : columnRemark.trim();
    }

}