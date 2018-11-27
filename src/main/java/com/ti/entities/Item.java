package com.ti.entities;

import java.lang.reflect.Field;

public class Item {
    private Integer id;
    private String name;
    private Double price;
    private Integer count;

    public Item() {
    }

    public Item(Integer id, String name, Double price, Integer count) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public static void main(String[] args) {
        Item item = new Item(1, "book", 122d, 34);
        Field[] fields = item.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            try {
                Class<?> type = field.getType();
                if (type == Integer.class) {
                    Integer intnum = (Integer) field.get(item);
                    System.out.println(intnum);
                }
                if (type == String.class) {
                    String str = (String) field.get(item);
                    System.out.println(str);
                }
                if (type == Double.class) {
                    Double aDouble = (Double) field.get(item);
                    System.out.println(aDouble);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", count=" + count +
                '}';
    }
}
