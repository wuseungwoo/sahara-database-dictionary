package com.sahara.dictionary.bean.sqlserver;

import org.springframework.util.StringUtils;

public class SqlserverIndexInfo {

    private String index_name= "";

    private String index_desc= "";

    private String index_keys= "";

    public String getIndex_name() {
        if(!StringUtils.isEmpty(index_name)){
            index_name=index_name.replaceAll("__","_");
        }
        return index_name;
    }

    public void setIndex_name(String index_name) {
        this.index_name = index_name;
    }

    public String getIndex_desc() {
        return index_desc;
    }

    public void setIndex_desc(String index_desc) {
        this.index_desc = index_desc;
    }

    public String getIndex_keys() {
        return index_keys;
    }

    public void setIndex_keys(String index_keys) {
        this.index_keys = index_keys;
    }

    @Override
    public String toString() {
        return "SqlserverIndexInfo{" +
                "index_name='" + index_name + '\'' +
                ", index_desc='" + index_desc + '\'' +
                ", index_keys='" + index_keys + '\'' +
                '}';
    }
}