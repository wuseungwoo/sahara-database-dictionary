package com.sahara.dictionary.dao.dynamic;

import java.sql.JDBCType;
import java.util.Date;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class TableColumnMetaDynamicSqlSupport {
    /**source table: table_column_meta */
    public static final TableColumnMeta tableColumnMeta = new TableColumnMeta();

    /**database comment: id序号,<br>source field: table_column_meta.id */
    public static final SqlColumn<Integer> id = tableColumnMeta.id;

    /**database comment: 系统名,<br>source field: table_column_meta.system_name */
    public static final SqlColumn<String> systemName = tableColumnMeta.systemName;

    /**database comment: 模块名,<br>source field: table_column_meta.module_name */
    public static final SqlColumn<String> moduleName = tableColumnMeta.moduleName;

    /**database comment: 数据库名,<br>source field: table_column_meta.db_name */
    public static final SqlColumn<String> dbName = tableColumnMeta.dbName;

    /**database comment: 表名,<br>source field: table_column_meta.table_name */
    public static final SqlColumn<String> tableName = tableColumnMeta.tableName;

    /**database comment: 字符集,<br>source field: table_column_meta.order_type */
    public static final SqlColumn<String> orderType = tableColumnMeta.orderType;

    /**database comment: 存储引擎,<br>source field: table_column_meta.storage_engine */
    public static final SqlColumn<String> storageEngine = tableColumnMeta.storageEngine;

    /**database comment: 列名,<br>source field: table_column_meta.column_name */
    public static final SqlColumn<String> columnName = tableColumnMeta.columnName;

    /**database comment: 类型,<br>source field: table_column_meta.column_type */
    public static final SqlColumn<String> columnType = tableColumnMeta.columnType;

    /**database comment: 长度,<br>source field: table_column_meta.column_length */
    public static final SqlColumn<Long> columnLength = tableColumnMeta.columnLength;

    /**database comment: 小数位数,<br>source field: table_column_meta.column_decimal_nums */
    public static final SqlColumn<Integer> columnDecimalNums = tableColumnMeta.columnDecimalNums;

    /**database comment: 是否为主键,<br>source field: table_column_meta.column_is_index */
    public static final SqlColumn<Integer> columnIsIndex = tableColumnMeta.columnIsIndex;

    /**database comment: 序号,<br>source field: table_column_meta.column_order */
    public static final SqlColumn<Integer> columnOrder = tableColumnMeta.columnOrder;

    /**database comment: 是否允许为空,<br>source field: table_column_meta.column_is_null */
    public static final SqlColumn<String> columnIsNull = tableColumnMeta.columnIsNull;

    /**database comment: 默认值,<br>source field: table_column_meta.column_default_value */
    public static final SqlColumn<String> columnDefaultValue = tableColumnMeta.columnDefaultValue;

    /**database comment: 创建人,<br>source field: table_column_meta.column_create_by */
    public static final SqlColumn<Date> columnCreateBy = tableColumnMeta.columnCreateBy;

    /**database comment: 创建时间,<br>source field: table_column_meta.column_create_time */
    public static final SqlColumn<Date> columnCreateTime = tableColumnMeta.columnCreateTime;

    /**database comment: 修改人,<br>source field: table_column_meta.column_update_by */
    public static final SqlColumn<Date> columnUpdateBy = tableColumnMeta.columnUpdateBy;

    /**database comment: 修改时间,<br>source field: table_column_meta.column_update_time */
    public static final SqlColumn<Date> columnUpdateTime = tableColumnMeta.columnUpdateTime;

    /**database comment: 版本号,<br>source field: table_column_meta.system_version */
    public static final SqlColumn<String> systemVersion = tableColumnMeta.systemVersion;

    /**database comment: 表描述,<br>source field: table_column_meta.description */
    public static final SqlColumn<String> description = tableColumnMeta.description;

    /**database comment: 列描述,<br>source field: table_column_meta.column_description */
    public static final SqlColumn<String> columnDescription = tableColumnMeta.columnDescription;

    /**database comment: 备注,<br>source field: table_column_meta.column_remark */
    public static final SqlColumn<String> columnRemark = tableColumnMeta.columnRemark;

    /**source table: table_column_meta */
    public static final class TableColumnMeta extends SqlTable {
        public final SqlColumn<Integer> id = column("id", JDBCType.INTEGER);

        public final SqlColumn<String> systemName = column("system_name", JDBCType.VARCHAR);

        public final SqlColumn<String> moduleName = column("module_name", JDBCType.VARCHAR);

        public final SqlColumn<String> dbName = column("db_name", JDBCType.VARCHAR);

        public final SqlColumn<String> tableName = column("table_name", JDBCType.VARCHAR);

        public final SqlColumn<String> orderType = column("order_type", JDBCType.VARCHAR);

        public final SqlColumn<String> storageEngine = column("storage_engine", JDBCType.VARCHAR);

        public final SqlColumn<String> columnName = column("column_name", JDBCType.VARCHAR);

        public final SqlColumn<String> columnType = column("column_type", JDBCType.VARCHAR);

        public final SqlColumn<Long> columnLength = column("column_length", JDBCType.BIGINT);

        public final SqlColumn<Integer> columnDecimalNums = column("column_decimal_nums", JDBCType.INTEGER);

        public final SqlColumn<Integer> columnIsIndex = column("column_is_index", JDBCType.INTEGER);

        public final SqlColumn<Integer> columnOrder = column("column_order", JDBCType.INTEGER);

        public final SqlColumn<String> columnIsNull = column("column_is_null", JDBCType.VARCHAR);

        public final SqlColumn<String> columnDefaultValue = column("column_default_value", JDBCType.VARCHAR);

        public final SqlColumn<Date> columnCreateBy = column("column_create_by", JDBCType.TIMESTAMP);

        public final SqlColumn<Date> columnCreateTime = column("column_create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Date> columnUpdateBy = column("column_update_by", JDBCType.TIMESTAMP);

        public final SqlColumn<Date> columnUpdateTime = column("column_update_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> systemVersion = column("system_version", JDBCType.VARCHAR);

        public final SqlColumn<String> description = column("description", JDBCType.LONGVARCHAR);

        public final SqlColumn<String> columnDescription = column("column_description", JDBCType.LONGVARCHAR);

        public final SqlColumn<String> columnRemark = column("column_remark", JDBCType.LONGVARCHAR);

        public TableColumnMeta() {
            super("table_column_meta");
        }
    }
}