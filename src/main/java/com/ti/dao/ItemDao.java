package com.ti.dao;

import com.ti.entities.Item;

import java.util.ArrayList;

public interface ItemDao {

    ArrayList<Item> findAllItem();

    ArrayList<Item>  findIem(Item item);

    void updateItem(Item item);

    void deleteItem(Item item);

    void addItem(Item item);


}
