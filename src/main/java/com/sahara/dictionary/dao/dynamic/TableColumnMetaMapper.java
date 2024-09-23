package com.sahara.dictionary.dao.dynamic;

import com.sahara.dictionary.dto.TableColumnMeta;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.delete.DeleteDSL;
import org.mybatis.dynamic.sql.delete.MyBatis3DeleteModelAdapter;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategy;
import org.mybatis.dynamic.sql.select.MyBatis3SelectModelAdapter;
import org.mybatis.dynamic.sql.select.QueryExpressionDSL;
import org.mybatis.dynamic.sql.select.SelectDSL;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.MyBatis3UpdateModelAdapter;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;

import java.util.List;

import static com.sahara.dictionary.dao.dynamic.TableColumnMetaDynamicSqlSupport.columnCreateBy;
import static com.sahara.dictionary.dao.dynamic.TableColumnMetaDynamicSqlSupport.columnCreateTime;
import static com.sahara.dictionary.dao.dynamic.TableColumnMetaDynamicSqlSupport.columnDecimalNums;
import static com.sahara.dictionary.dao.dynamic.TableColumnMetaDynamicSqlSupport.columnDefaultValue;
import static com.sahara.dictionary.dao.dynamic.TableColumnMetaDynamicSqlSupport.columnDescription;
import static com.sahara.dictionary.dao.dynamic.TableColumnMetaDynamicSqlSupport.columnIsIndex;
import static com.sahara.dictionary.dao.dynamic.TableColumnMetaDynamicSqlSupport.columnIsNull;
import static com.sahara.dictionary.dao.dynamic.TableColumnMetaDynamicSqlSupport.columnLength;
import static com.sahara.dictionary.dao.dynamic.TableColumnMetaDynamicSqlSupport.columnName;
import static com.sahara.dictionary.dao.dynamic.TableColumnMetaDynamicSqlSupport.columnOrder;
import static com.sahara.dictionary.dao.dynamic.TableColumnMetaDynamicSqlSupport.columnRemark;
import static com.sahara.dictionary.dao.dynamic.TableColumnMetaDynamicSqlSupport.columnType;
import static com.sahara.dictionary.dao.dynamic.TableColumnMetaDynamicSqlSupport.columnUpdateBy;
import static com.sahara.dictionary.dao.dynamic.TableColumnMetaDynamicSqlSupport.columnUpdateTime;
import static com.sahara.dictionary.dao.dynamic.TableColumnMetaDynamicSqlSupport.dbName;
import static com.sahara.dictionary.dao.dynamic.TableColumnMetaDynamicSqlSupport.description;
import static com.sahara.dictionary.dao.dynamic.TableColumnMetaDynamicSqlSupport.id;
import static com.sahara.dictionary.dao.dynamic.TableColumnMetaDynamicSqlSupport.moduleName;
import static com.sahara.dictionary.dao.dynamic.TableColumnMetaDynamicSqlSupport.orderType;
import static com.sahara.dictionary.dao.dynamic.TableColumnMetaDynamicSqlSupport.storageEngine;
import static com.sahara.dictionary.dao.dynamic.TableColumnMetaDynamicSqlSupport.systemName;
import static com.sahara.dictionary.dao.dynamic.TableColumnMetaDynamicSqlSupport.systemVersion;
import static com.sahara.dictionary.dao.dynamic.TableColumnMetaDynamicSqlSupport.tableColumnMeta;
import static com.sahara.dictionary.dao.dynamic.TableColumnMetaDynamicSqlSupport.tableName;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

@Mapper
public interface TableColumnMetaMapper {

    /**
     * source table: table_column_meta
     */
    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    long count(SelectStatementProvider selectStatement);

    /**
     * source table: table_column_meta
     */
    @DeleteProvider(type = SqlProviderAdapter.class, method = "delete")
    int delete(DeleteStatementProvider deleteStatement);

    /**
     * source table: table_column_meta
     */
    @InsertProvider(type = SqlProviderAdapter.class, method = "insert")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "record.id", before = false, resultType = Integer.class)
    int insert(InsertStatementProvider<TableColumnMeta> insertStatement);

    /**
     * source table: table_column_meta
     */
    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @ResultMap("TableColumnMetaResult")
    TableColumnMeta selectOne(SelectStatementProvider selectStatement);

    /**
     * source table: table_column_meta
     */
    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @Results(id = "TableColumnMetaResult", value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "system_name", property = "systemName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "module_name", property = "moduleName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "db_name", property = "dbName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "table_name", property = "tableName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "order_type", property = "orderType", jdbcType = JdbcType.VARCHAR),
            @Result(column = "storage_engine", property = "storageEngine", jdbcType = JdbcType.VARCHAR),
            @Result(column = "column_name", property = "columnName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "column_type", property = "columnType", jdbcType = JdbcType.VARCHAR),
            @Result(column = "column_length", property = "columnLength", jdbcType = JdbcType.BIGINT),
            @Result(column = "column_decimal_nums", property = "columnDecimalNums", jdbcType = JdbcType.INTEGER),
            @Result(column = "column_is_index", property = "columnIsIndex", jdbcType = JdbcType.INTEGER),
            @Result(column = "column_order", property = "columnOrder", jdbcType = JdbcType.INTEGER),
            @Result(column = "column_is_null", property = "columnIsNull", jdbcType = JdbcType.VARCHAR),
            @Result(column = "column_default_value", property = "columnDefaultValue", jdbcType = JdbcType.VARCHAR),
            @Result(column = "column_create_by", property = "columnCreateBy", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "column_create_time", property = "columnCreateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "column_update_by", property = "columnUpdateBy", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "column_update_time", property = "columnUpdateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "system_version", property = "systemVersion", jdbcType = JdbcType.VARCHAR),
            @Result(column = "description", property = "description", jdbcType = JdbcType.LONGVARCHAR),
            @Result(column = "column_description", property = "columnDescription", jdbcType = JdbcType.LONGVARCHAR),
            @Result(column = "column_remark", property = "columnRemark", jdbcType = JdbcType.LONGVARCHAR)
    })
    List<TableColumnMeta> selectMany(SelectStatementProvider selectStatement);

    /**
     * source table: table_column_meta
     */
    @UpdateProvider(type = SqlProviderAdapter.class, method = "update")
    int update(UpdateStatementProvider updateStatement);

    /**
     * source table: table_column_meta
     */
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<Long>> countByExample() {
        return SelectDSL.selectWithMapper(this::count, SqlBuilder.count())
                .from(tableColumnMeta);
    }

    /**
     * source table: table_column_meta
     */
    default DeleteDSL<MyBatis3DeleteModelAdapter<Integer>> deleteByExample() {
        return DeleteDSL.deleteFromWithMapper(this::delete, tableColumnMeta);
    }

    /**
     * source table: table_column_meta
     */
    default int deleteByPrimaryKey(Integer id_) {
        return DeleteDSL.deleteFromWithMapper(this::delete, tableColumnMeta)
                .where(id, isEqualTo(id_))
                .build()
                .execute();
    }

    /**
     * source table: table_column_meta
     */
    default int insert(TableColumnMeta record) {
        return insert(SqlBuilder.insert(record)
                .into(tableColumnMeta)
                .map(systemName).toProperty("systemName")
                .map(moduleName).toProperty("moduleName")
                .map(dbName).toProperty("dbName")
                .map(tableName).toProperty("tableName")
                .map(orderType).toProperty("orderType")
                .map(storageEngine).toProperty("storageEngine")
                .map(columnName).toProperty("columnName")
                .map(columnType).toProperty("columnType")
                .map(columnLength).toProperty("columnLength")
                .map(columnDecimalNums).toProperty("columnDecimalNums")
                .map(columnIsIndex).toProperty("columnIsIndex")
                .map(columnOrder).toProperty("columnOrder")
                .map(columnIsNull).toProperty("columnIsNull")
                .map(columnDefaultValue).toProperty("columnDefaultValue")
                .map(columnCreateBy).toProperty("columnCreateBy")
                .map(columnCreateTime).toProperty("columnCreateTime")
                .map(columnUpdateBy).toProperty("columnUpdateBy")
                .map(columnUpdateTime).toProperty("columnUpdateTime")
                .map(systemVersion).toProperty("systemVersion")
                .map(description).toProperty("description")
                .map(columnDescription).toProperty("columnDescription")
                .map(columnRemark).toProperty("columnRemark")
                .build()
                .render(RenderingStrategy.MYBATIS3));
    }

    /**
     * source table: table_column_meta
     */
    default int insertSelective(TableColumnMeta record) {
        return insert(SqlBuilder.insert(record)
                .into(tableColumnMeta)
                .map(systemName).toPropertyWhenPresent("systemName", record::getSystemName)
                .map(moduleName).toPropertyWhenPresent("moduleName", record::getModuleName)
                .map(dbName).toPropertyWhenPresent("dbName", record::getDbName)
                .map(tableName).toPropertyWhenPresent("tableName", record::getTableName)
                .map(orderType).toPropertyWhenPresent("orderType", record::getOrderType)
                .map(storageEngine).toPropertyWhenPresent("storageEngine", record::getStorageEngine)
                .map(columnName).toPropertyWhenPresent("columnName", record::getColumnName)
                .map(columnType).toPropertyWhenPresent("columnType", record::getColumnType)
                .map(columnLength).toPropertyWhenPresent("columnLength", record::getColumnLength)
                .map(columnDecimalNums).toPropertyWhenPresent("columnDecimalNums", record::getColumnDecimalNums)
                .map(columnIsIndex).toPropertyWhenPresent("columnIsIndex", record::getColumnIsIndex)
                .map(columnOrder).toPropertyWhenPresent("columnOrder", record::getColumnOrder)
                .map(columnIsNull).toPropertyWhenPresent("columnIsNull", record::getColumnIsNull)
                .map(columnDefaultValue).toPropertyWhenPresent("columnDefaultValue", record::getColumnDefaultValue)
                .map(columnCreateBy).toPropertyWhenPresent("columnCreateBy", record::getColumnCreateBy)
                .map(columnCreateTime).toPropertyWhenPresent("columnCreateTime", record::getColumnCreateTime)
                .map(columnUpdateBy).toPropertyWhenPresent("columnUpdateBy", record::getColumnUpdateBy)
                .map(columnUpdateTime).toPropertyWhenPresent("columnUpdateTime", record::getColumnUpdateTime)
                .map(systemVersion).toPropertyWhenPresent("systemVersion", record::getSystemVersion)
                .map(description).toPropertyWhenPresent("description", record::getDescription)
                .map(columnDescription).toPropertyWhenPresent("columnDescription", record::getColumnDescription)
                .map(columnRemark).toPropertyWhenPresent("columnRemark", record::getColumnRemark)
                .build()
                .render(RenderingStrategy.MYBATIS3));
    }

    /**
     * source table: table_column_meta
     */
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<List<TableColumnMeta>>> selectByExample() {
        return SelectDSL.selectWithMapper(this::selectMany, id, systemName, moduleName, dbName, tableName, orderType, storageEngine, columnName, columnType, columnLength, columnDecimalNums, columnIsIndex, columnOrder, columnIsNull, columnDefaultValue, columnCreateBy, columnCreateTime, columnUpdateBy, columnUpdateTime, systemVersion, description, columnDescription, columnRemark)
                .from(tableColumnMeta);
    }

    /**
     * source table: table_column_meta
     */
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<List<TableColumnMeta>>> selectDistinctByExample() {
        return SelectDSL.selectDistinctWithMapper(this::selectMany, id, systemName, moduleName, dbName, tableName, orderType, storageEngine, columnName, columnType, columnLength, columnDecimalNums, columnIsIndex, columnOrder, columnIsNull, columnDefaultValue, columnCreateBy, columnCreateTime, columnUpdateBy, columnUpdateTime, systemVersion, description, columnDescription, columnRemark)
                .from(tableColumnMeta);
    }

    /**
     * source table: table_column_meta
     */
    default TableColumnMeta selectByPrimaryKey(Integer id_) {
        return SelectDSL.selectWithMapper(this::selectOne, id, systemName, moduleName, dbName, tableName, orderType, storageEngine, columnName, columnType, columnLength, columnDecimalNums, columnIsIndex, columnOrder, columnIsNull, columnDefaultValue, columnCreateBy, columnCreateTime, columnUpdateBy, columnUpdateTime, systemVersion, description, columnDescription, columnRemark)
                .from(tableColumnMeta)
                .where(id, isEqualTo(id_))
                .build()
                .execute();
    }

    /**
     * source table: table_column_meta
     */
    default UpdateDSL<MyBatis3UpdateModelAdapter<Integer>> updateByExample(TableColumnMeta record) {
        return UpdateDSL.updateWithMapper(this::update, tableColumnMeta)
                .set(systemName).equalTo(record::getSystemName)
                .set(moduleName).equalTo(record::getModuleName)
                .set(dbName).equalTo(record::getDbName)
                .set(tableName).equalTo(record::getTableName)
                .set(orderType).equalTo(record::getOrderType)
                .set(storageEngine).equalTo(record::getStorageEngine)
                .set(columnName).equalTo(record::getColumnName)
                .set(columnType).equalTo(record::getColumnType)
                .set(columnLength).equalTo(record::getColumnLength)
                .set(columnDecimalNums).equalTo(record::getColumnDecimalNums)
                .set(columnIsIndex).equalTo(record::getColumnIsIndex)
                .set(columnOrder).equalTo(record::getColumnOrder)
                .set(columnIsNull).equalTo(record::getColumnIsNull)
                .set(columnDefaultValue).equalTo(record::getColumnDefaultValue)
                .set(columnCreateBy).equalTo(record::getColumnCreateBy)
                .set(columnCreateTime).equalTo(record::getColumnCreateTime)
                .set(columnUpdateBy).equalTo(record::getColumnUpdateBy)
                .set(columnUpdateTime).equalTo(record::getColumnUpdateTime)
                .set(systemVersion).equalTo(record::getSystemVersion)
                .set(description).equalTo(record::getDescription)
                .set(columnDescription).equalTo(record::getColumnDescription)
                .set(columnRemark).equalTo(record::getColumnRemark);
    }

    /**
     * source table: table_column_meta
     */
    default UpdateDSL<MyBatis3UpdateModelAdapter<Integer>> updateByExampleSelective(TableColumnMeta record) {
        return UpdateDSL.updateWithMapper(this::update, tableColumnMeta)
                .set(systemName).equalToWhenPresent(record::getSystemName)
                .set(moduleName).equalToWhenPresent(record::getModuleName)
                .set(dbName).equalToWhenPresent(record::getDbName)
                .set(tableName).equalToWhenPresent(record::getTableName)
                .set(orderType).equalToWhenPresent(record::getOrderType)
                .set(storageEngine).equalToWhenPresent(record::getStorageEngine)
                .set(columnName).equalToWhenPresent(record::getColumnName)
                .set(columnType).equalToWhenPresent(record::getColumnType)
                .set(columnLength).equalToWhenPresent(record::getColumnLength)
                .set(columnDecimalNums).equalToWhenPresent(record::getColumnDecimalNums)
                .set(columnIsIndex).equalToWhenPresent(record::getColumnIsIndex)
                .set(columnOrder).equalToWhenPresent(record::getColumnOrder)
                .set(columnIsNull).equalToWhenPresent(record::getColumnIsNull)
                .set(columnDefaultValue).equalToWhenPresent(record::getColumnDefaultValue)
                .set(columnCreateBy).equalToWhenPresent(record::getColumnCreateBy)
                .set(columnCreateTime).equalToWhenPresent(record::getColumnCreateTime)
                .set(columnUpdateBy).equalToWhenPresent(record::getColumnUpdateBy)
                .set(columnUpdateTime).equalToWhenPresent(record::getColumnUpdateTime)
                .set(systemVersion).equalToWhenPresent(record::getSystemVersion)
                .set(description).equalToWhenPresent(record::getDescription)
                .set(columnDescription).equalToWhenPresent(record::getColumnDescription)
                .set(columnRemark).equalToWhenPresent(record::getColumnRemark);
    }

    /**
     * source table: table_column_meta
     */
    default int updateByPrimaryKey(TableColumnMeta record) {
        return UpdateDSL.updateWithMapper(this::update, tableColumnMeta)
                .set(systemName).equalTo(record::getSystemName)
                .set(moduleName).equalTo(record::getModuleName)
                .set(dbName).equalTo(record::getDbName)
                .set(tableName).equalTo(record::getTableName)
                .set(orderType).equalTo(record::getOrderType)
                .set(storageEngine).equalTo(record::getStorageEngine)
                .set(columnName).equalTo(record::getColumnName)
                .set(columnType).equalTo(record::getColumnType)
                .set(columnLength).equalTo(record::getColumnLength)
                .set(columnDecimalNums).equalTo(record::getColumnDecimalNums)
                .set(columnIsIndex).equalTo(record::getColumnIsIndex)
                .set(columnOrder).equalTo(record::getColumnOrder)
                .set(columnIsNull).equalTo(record::getColumnIsNull)
                .set(columnDefaultValue).equalTo(record::getColumnDefaultValue)
                .set(columnCreateBy).equalTo(record::getColumnCreateBy)
                .set(columnCreateTime).equalTo(record::getColumnCreateTime)
                .set(columnUpdateBy).equalTo(record::getColumnUpdateBy)
                .set(columnUpdateTime).equalTo(record::getColumnUpdateTime)
                .set(systemVersion).equalTo(record::getSystemVersion)
                .set(description).equalTo(record::getDescription)
                .set(columnDescription).equalTo(record::getColumnDescription)
                .set(columnRemark).equalTo(record::getColumnRemark)
                .where(id, isEqualTo(record::getId))
                .build()
                .execute();
    }

    /**
     * source table: table_column_meta
     */
    default int updateByPrimaryKeySelective(TableColumnMeta record) {
        return UpdateDSL.updateWithMapper(this::update, tableColumnMeta)
                .set(systemName).equalToWhenPresent(record::getSystemName)
                .set(moduleName).equalToWhenPresent(record::getModuleName)
                .set(dbName).equalToWhenPresent(record::getDbName)
                .set(tableName).equalToWhenPresent(record::getTableName)
                .set(orderType).equalToWhenPresent(record::getOrderType)
                .set(storageEngine).equalToWhenPresent(record::getStorageEngine)
                .set(columnName).equalToWhenPresent(record::getColumnName)
                .set(columnType).equalToWhenPresent(record::getColumnType)
                .set(columnLength).equalToWhenPresent(record::getColumnLength)
                .set(columnDecimalNums).equalToWhenPresent(record::getColumnDecimalNums)
                .set(columnIsIndex).equalToWhenPresent(record::getColumnIsIndex)
                .set(columnOrder).equalToWhenPresent(record::getColumnOrder)
                .set(columnIsNull).equalToWhenPresent(record::getColumnIsNull)
                .set(columnDefaultValue).equalToWhenPresent(record::getColumnDefaultValue)
                .set(columnCreateBy).equalToWhenPresent(record::getColumnCreateBy)
                .set(columnCreateTime).equalToWhenPresent(record::getColumnCreateTime)
                .set(columnUpdateBy).equalToWhenPresent(record::getColumnUpdateBy)
                .set(columnUpdateTime).equalToWhenPresent(record::getColumnUpdateTime)
                .set(systemVersion).equalToWhenPresent(record::getSystemVersion)
                .set(description).equalToWhenPresent(record::getDescription)
                .set(columnDescription).equalToWhenPresent(record::getColumnDescription)
                .set(columnRemark).equalToWhenPresent(record::getColumnRemark)
                .where(id, isEqualTo(record::getId))
                .build()
                .execute();
    }
}