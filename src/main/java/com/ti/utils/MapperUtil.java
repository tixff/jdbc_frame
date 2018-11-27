package com.ti.utils;

import com.ti.enums.SqlCondition;

import java.lang.reflect.Field;
import java.util.HashMap;

public class MapperUtil {

    /**
     * 组装插入语句
     *
     * @param sb
     * @param obj
     * @return
     */
    public static String setInsertValueContion(StringBuffer sb, Object obj) {
        //添加列
        HashMap<String, String> map = JdbcUtil.loadMapper(obj.getClass());
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String name = field.getName();
            for (String column : map.keySet()) {
                if (map.get(column).equals(name)) {
                    String methodName = "get" + StringUtil.toUpperFirstChar(name);
                    try {
                        Object value = obj.getClass().getMethod(methodName).invoke(obj);
                        if (value != null) {
                            sb.append(column + ",");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (sb.charAt(sb.length() - 1) != ',') {
            return null;
        }
        sb.replace(sb.lastIndexOf(","), sb.length(), "");
        sb.append(") values(");
        //添加值
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Class<?> type = field.getType();
            String name = field.getName();
            try {
                if (type == Integer.class) {
                    String methodName = "get" + StringUtil.toUpperFirstChar(name);
                    Integer value = (Integer) obj.getClass().getMethod(methodName).invoke(obj);
                    if (value == null) {
                        continue;
                    }
                    sb.append(value + " , ");
                    continue;
                }
                if (type == String.class) {
                    String methodName = "get" + StringUtil.toUpperFirstChar(name);
                    String value = (String) obj.getClass().getMethod(methodName).invoke(obj);
                    if (value == null) {
                        continue;
                    }
                    sb.append("\"" + value + "\"" + " , ");
                    continue;
                }
                if (type == Double.class) {
                    String methodName = "get" + StringUtil.toUpperFirstChar(name);
                    Double value = (Double) obj.getClass().getMethod(methodName).invoke(obj);
                    if (value == null) {
                        continue;
                    }
                    sb.append(value + " , ");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sb.replace(sb.lastIndexOf(","), sb.length(), "");
        sb.append(")");
        return sb.toString();
    }

    /**
     * 组装add or查询条件
     *
     * @param sb
     * @param obj
     * @return
     */
    public static String commonCondition(StringBuffer sb, Object obj, SqlCondition condition) {
        String oldSql = sb.toString();
        Field[] fields = obj.getClass().getDeclaredFields();
        String flag = "";
        if (condition == SqlCondition.ADD) {
            flag = SqlCondition.ADD.getValue();
        }

        if (condition == SqlCondition.OR) {
            flag = SqlCondition.OR.getValue();
        }

        if (condition == SqlCondition.VALUES) {
            flag = SqlCondition.VALUES.getValue();
        }

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            try {
                Class<?> type = field.getType();
                if (type == Integer.class) {
                    String name = field.getName();
                    String methodName = "get" + StringUtil.toUpperFirstChar(name);
                    Integer value = (Integer) obj.getClass().getMethod(methodName).invoke(obj);
                    if (value == null) {
                        continue;
                    }
                    sb.append(" " + name + "=" + value + flag);
                    continue;
                }
                if (type == String.class) {
                    String name = field.getName();
                    String methodName = "get" + StringUtil.toUpperFirstChar(name);
                    String value = (String) obj.getClass().getMethod(methodName).invoke(obj);
                    if (value == null) {
                        continue;
                    }
                    sb.append(" " + name + "=\"" + value + "\"" + flag);
                    continue;
                }
                if (type == Double.class) {
                    String name = field.getName();
                    String methodName = "get" + StringUtil.toUpperFirstChar(name);
                    Double value = (Double) obj.getClass().getMethod(methodName).invoke(obj);
                    if (value == null) {
                        continue;
                    }
                    sb.append(" " + name + "=" + value + flag);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (sb.toString().equals(oldSql)) {
            return null;
        }
        sb.replace(sb.lastIndexOf(flag), sb.length(), "");
        sb.append(" ");
        return sb.toString();
    }

}
