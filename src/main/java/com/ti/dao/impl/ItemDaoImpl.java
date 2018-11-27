package com.ti.dao.impl;

import com.ti.dao.ItemDao;
import com.ti.entities.Item;
import com.ti.enums.SqlCondition;
import com.ti.utils.JdbcUtil;
import com.ti.utils.MapperUtil;

import java.util.ArrayList;

public class ItemDaoImpl implements ItemDao {

    public ArrayList<Item> findAllItem() {
        String sql = "select *from item";
        ArrayList list = JdbcUtil.select(sql, Item.class);
        return list;
    }

    public ArrayList<Item> findIem(Item item) {
        if (item == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        sb.append("select * from item where");
        String sql = MapperUtil.commonCondition(sb, item, SqlCondition.ADD);
        if (sql == null) {
            return null;
        }
        ArrayList list = JdbcUtil.select(sql, Item.class);
        return list;
    }

    public void updateItem(Item item) {
        if (item == null || item.getId() == null) {
            return;
        }
        StringBuffer sb = new StringBuffer();
        sb.append("update item set ");
        String sql = MapperUtil.commonCondition(sb, item, SqlCondition.VALUES);
        if (sql == null) {
            return;
        }
        sb.append("where id=" + item.getId());
        sql = sb.toString();
        JdbcUtil.update(sql);
    }

    public void deleteItem(Item item) {
        if (item == null) {
            return;
        }

        StringBuffer sb = new StringBuffer();
        sb.append("delete from item where");
        String sql = MapperUtil.commonCondition(sb, item, SqlCondition.OR);
        if (sql == null) {
            return;
        }

        JdbcUtil.update(sql);
    }

    public void addItem(Item item) {
        if (item == null) {
            return;
        }

        StringBuffer sb = new StringBuffer();
        sb.append("insert into item(");
        String sql = MapperUtil.setInsertValueContion(sb, item);
        if (sql == null) {
            return;
        }
        JdbcUtil.update(sql);
    }
}
