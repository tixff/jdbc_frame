package com.ti.enums;

public enum SqlCondition {
    ADD(" and "),
    OR(" or "),
    VALUES(" , ");//insert values括号里面的值
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    SqlCondition(String value) {
        this.value = value;
    }


}
