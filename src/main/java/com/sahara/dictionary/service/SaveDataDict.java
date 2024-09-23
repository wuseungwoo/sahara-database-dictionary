package com.sahara.dictionary.service;

import com.sahara.dictionary.bean.TableInfo;
import com.sahara.dictionary.dto.TableColumnMeta;
import com.sahara.dictionary.dto.mapper.TableColumnMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SaveDataDict {
    static Logger logger = LoggerFactory.getLogger(BuildPDF.class);
    @Resource
    private TableColumnMapper tableColumnMapper;

    public void saveData(List<TableInfo> list) {
        try {
            List<TableColumnMeta> data = transform(list);
            //分批
            List<List<TableColumnMeta>> lists = groupListByQuantity(data, 1000);
            for (List<TableColumnMeta> tableColumnMetas : lists) {
                tableColumnMapper.batchInsert(tableColumnMetas);
            }
        } catch (Exception e) {
            logger.error("saveDataDict error", e);
        }
    }

    public List<TableColumnMeta> transform(List<TableInfo> list) {
        return list.stream().map(TableColumnMeta::new).collect(Collectors.toList());
    }

    public <T> List<List<T>> groupListByQuantity(List<T> list, int quantity) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        } else if (quantity <= 0) {
            throw new IllegalArgumentException("Wrong quantity.");
        } else {
            List<List<T>> wrapList = new ArrayList();

            for (int count = 0; count < list.size(); count += quantity) {
                wrapList.add(list.subList(count, Math.min(count + quantity, list.size())));
            }

            return wrapList;
        }
    }
}
