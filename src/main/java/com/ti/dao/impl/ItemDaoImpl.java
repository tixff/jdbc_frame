package com.ti.dao.impl;

import com.ti.annotation.BeanScan;
import com.ti.dao.ItemDao;
import com.ti.entities.Item;

import java.util.ArrayList;

@BeanScan
public class ItemDaoImpl extends BaseDaoImpl<Item> implements ItemDao {

    public ArrayList<Item> findAllItem() {
        return finaAll(new Item(), new Item().getTable());
    }

    public ArrayList<Item> findItem(Item item) {
        return find(item, item.getTable());
    }

    public void updateItem(Item item) {
        update(item, item.getTable(), item.getId());
    }

    public void deleteItem(Item item) {
        delete(item, item.getTable());
    }

    public void addItem(Item item) {
        add(item, item.getTable());
    }
}
