package com.ti.utils;

import com.ti.BeanFactory;
import com.ti.enums.SqlCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Properties;

public class MapperUtil {
    private static final Logger logger = LoggerFactory.getLogger(MapperUtil.class);

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
            if ("table".equals(name)) {
                continue;
            }
            for (String column : map.keySet()) {
                if (map.get(column).equals(name)) {
                    String methodName = "get" + StringUtil.toUpperFirstChar(name);
                    try {
                        Object value = obj.getClass().getMethod(methodName).invoke(obj);
                        if (value != null) {
                            sb.append(column + ",");
                        }
                    } catch (Exception e) {
                        logger.error("设置列属性失败", e);
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
                logger.error("sql语句注入bean属性值失败");
            }
        }
        sb.replace(sb.lastIndexOf(","), sb.length(), "");
        sb.append(")");
        return sb.toString();
    }

    /**
     * 组装add or ,查询条件
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
            String name = field.getName();
            if ("table".equals(name)) {
                continue;
            }
            try {
                Class<?> type = field.getType();
                if (type == Integer.class) {

                    String methodName = "get" + StringUtil.toUpperFirstChar(name);
                    Integer value = (Integer) obj.getClass().getMethod(methodName).invoke(obj);
                    if (value == null) {
                        continue;
                    }
                    sb.append(" " + name + "=" + value + flag);
                    continue;
                }
                if (type == String.class) {
                    String methodName = "get" + StringUtil.toUpperFirstChar(name);
                    String value = (String) obj.getClass().getMethod(methodName).invoke(obj);
                    if (value == null) {
                        continue;
                    }
                    sb.append(" " + name + "=\"" + value + "\"" + flag);
                    continue;
                }
                if (type == Double.class) {
                    String methodName = "get" + StringUtil.toUpperFirstChar(name);
                    Double value = (Double) obj.getClass().getMethod(methodName).invoke(obj);
                    if (value == null) {
                        continue;
                    }
                    sb.append(" " + name + "=" + value + flag);
                }
            } catch (Exception e) {
                logger.error("添加sql条件失败", e);
            }
        }
        if (sb.toString().equals(oldSql)) {
            return null;
        }
        sb.replace(sb.lastIndexOf(flag), sb.length(), "");
        sb.append(" ");
        return sb.toString();
    }

    /**
     * 初始化表名
     *
     * @param c
     * @return
     */
    public static String initTable(Class c) {
        HashMap<String, Properties> props = BeanFactory.getProps();
        String name = c.getName();
        name = name.substring(name.lastIndexOf(".") + 1, name.length());
        Properties properties = props.get(name + "Mapper");
        return (String) properties.get("table");
    }
}
