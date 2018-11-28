package com.ti.dao.impl;

import com.ti.dao.BaseDao;
import com.ti.enums.SqlCondition;
import com.ti.utils.JdbcUtil;
import com.ti.utils.MapperUtil;

import java.util.ArrayList;

public class BaseDaoImpl<T> implements BaseDao<T> {

    @Override
    public ArrayList<T> finaAll(T t, String table) {
        String sql = "select *from " + table;
        ArrayList list = JdbcUtil.select(sql, t.getClass());
        return list;
    }

    @Override
    public ArrayList<T> find(T t, String table) {
        if (t == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        sb.append("select * from " + table + " where");
        String sql = MapperUtil.commonCondition(sb, t, SqlCondition.ADD);
        if (sql == null) {
            return null;
        }
        ArrayList list = JdbcUtil.select(sql, t.getClass());
        return list;
    }

    @Override
    public void add(T t, String table) {
        if (t == null) {
            return;
        }

        StringBuffer sb = new StringBuffer();
        sb.append("insert into " + table + "(");
        String sql = MapperUtil.setInsertValueContion(sb, t);
        if (sql == null) {
            return;
        }
        JdbcUtil.update(sql);
    }

    @Override
    public void update(T t, String table, Integer id) {
        if (t == null || id == null) {
            return;
        }
        StringBuffer sb = new StringBuffer();
        sb.append("update " + table + " set ");
        String sql = MapperUtil.commonCondition(sb, t, SqlCondition.VALUES);
        if (sql == null) {
            return;
        }
        sb.append("where id=" + id);
        sql = sb.toString();
        JdbcUtil.update(sql);
    }

    @Override
    public void delete(T t, String table) {
        if (t == null) {
            return;
        }

        StringBuffer sb = new StringBuffer();
        sb.append("delete from " + table + " where");
        String sql = MapperUtil.commonCondition(sb, t, SqlCondition.OR);
        if (sql == null) {
            return;
        }

        JdbcUtil.update(sql);
    }
}
