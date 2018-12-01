package com.ti.utils;


import com.ti.ApplicationStarter;
import com.ti.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class JdbcUtil {

    private static final Logger logger = LoggerFactory.getLogger(JdbcUtil.class);

    private static String userName;
    private static String password;
    private static String url;
    private static String driver;


    static {
        Properties properties = new Properties();
        InputStream resourceAsStream = ApplicationStarter.class.getClass().getResourceAsStream("/database.properties");
        try {
            properties.load(resourceAsStream);
            userName = properties.getProperty("jdbc.mysql.username");
            password = properties.getProperty("jdbc.mysql.password");
            url = properties.getProperty("jdbc.mysql.url");
            driver = properties.getProperty("jdbc.mysql.driver");
        } catch (Exception e) {
            logger.error("加载数据库配置资源失败", e);
        }
    }

    /**
     * 获取连接
     *
     * @return
     */
    public static Connection getConnection() {
        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, userName, password);

            if (connection == null) {
                logger.error("获取数据库连接失败");
                throw new Exception("");
            }
            return connection;
        } catch (Exception e) {
            logger.error("获取数据库连接失败", e);
        }
        return null;
    }

    /**
     * 跟新操作
     *
     * @param sql
     */
    public static void update(String sql) {
        Connection conn = null;
        Statement statement = null;
        try {
            conn = getConnection();
            statement = conn.createStatement();
            statement.executeUpdate(sql);
        } catch (Exception e) {
            logger.error("执行更新sql语句失败", e);
        } finally {
            closeConnection(conn, statement);
        }
    }

    /**
     * 关闭连接
     *
     * @param conn
     * @param statement
     */
    public static void closeConnection(Connection conn, Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            logger.error("关闭数据库连接失败", e);
        }
    }

    /**
     * 查询操作
     *
     * @param sql
     * @param resultType
     * @return
     */
    public static ArrayList select(String sql, Class resultType) {
        String className = resultType.getName();
        className = className.substring(className.lastIndexOf(".") + 1, className.length()) + "Mapper";
        Connection conn = null;
        Statement statement = null;
        try {
            conn = getConnection();
            ArrayList<Object> list = new ArrayList<>();
            HashMap<String, Properties> props = BeanFactory.getProps();
            Properties mapper = props.get(className);

            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet == null) {
                return null;
            }
            while (resultSet.next()) {
                Object item = resultType.newInstance();
                for (Object column : mapper.keySet()) {
                    String field = (String) mapper.get(column);
                    if ("table".equals(column)) {
                        continue;
                    }
                    String value = resultSet.getString((String) column);
                    if (value == null) {
                        continue;
                    }
                    String methodName = "set" + StringUtil.toUpperFirstChar(field);
                    Class fieldType = item.getClass().getDeclaredField(field).getType();
                    if (fieldType == Integer.class) {
                        item.getClass().getMethod(methodName, fieldType).invoke(item, Integer.parseInt(value));
                        continue;
                    } else if (fieldType == Double.class) {
                        item.getClass().getMethod(methodName, fieldType).invoke(item, Double.parseDouble(value));
                        continue;
                    }
                    item.getClass().getMethod(methodName, fieldType).invoke(item, value);
                }
                list.add(item);
            }
            return list;
        } catch (Exception e) {
            logger.error("执行查询sql语句失败", e);
        } finally {
            closeConnection(conn, statement);
        }
        return null;
    }


    /**
     * 加载mapper配置
     *
     * @param resultType
     * @return
     */
    public static HashMap<String, String> loadMapper(Class resultType) {
        HashMap<String, String> map = new HashMap<>();
        String className = resultType.getName();

        className = className.substring(className.lastIndexOf(".") + 1);
        String mapperName = className + "Mapper";
        Properties prop = new Properties();
        //加载mapper
        InputStream mapperStream = ApplicationStarter.class.getClass().getResourceAsStream("/" + mapperName + ".properties");
        try {
            prop.load(mapperStream);
            for (Object key : prop.keySet()) {
                String field = prop.getProperty((String) key);
                map.put((String) key, field);
            }
        } catch (IOException e) {
            logger.error("加载mapper文件失败", e);
        }
        return map;
    }
}
