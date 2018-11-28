package com.ti.dao;

import java.util.ArrayList;

public interface BaseDao<T> {
    ArrayList<T> finaAll(T t, String table);

    ArrayList<T> find(T t, String table);

    void add(T t, String table);

    void update(T t, String table,Integer id);

    void delete(T t, String table);
}
