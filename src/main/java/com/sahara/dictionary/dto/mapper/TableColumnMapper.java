package com.sahara.dictionary.dto.mapper;

import com.sahara.dictionary.dto.TableColumnMeta;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TableColumnMapper {
    void batchInsert(@Param("records") List<TableColumnMeta> records);
}
