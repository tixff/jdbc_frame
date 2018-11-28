package com.ti.entities;

import com.ti.utils.MapperUtil;

public class BaseBean<T> {
    private String table;

    public String getTable() {
        return table;
    }

    public BaseBean() {
        table = MapperUtil.initTable(this.getClass());
    }

}
