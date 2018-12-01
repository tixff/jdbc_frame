package com.ti;

import com.ti.annotation.BeanScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Pattern;

public class BeanFactory {
    private static Logger logger = LoggerFactory.getLogger(BeanFactory.class);
    private static HashMap<String, Object> beans = new HashMap<>();
    private static HashMap<String, Properties> props = new HashMap<>();
    private static Pattern pattern = Pattern.compile("^.+Mapper.properties");


    public static Object get(String className) {
        return beans.get(className);
    }

    public static HashMap<String, Properties> getProps() {
        return props;
    }

    /**
     * 注入工程所有被扫描的类
     *
     * @param classNames
     * @param path
     * @param packageName
     */
    public static void setAllClassName(ArrayList<String> classNames, String path, String packageName) {
        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                String name = f.getName();
                String newpath = path + "/" + name;
                String newPakageName;
                if (f.isDirectory()) {
                    if (packageName != null) {
                        newPakageName = packageName + "." + name;
                    } else {
                        newPakageName = name;
                    }
                    setAllClassName(classNames, newpath, newPakageName);
                } else {
                    setAllClassName(classNames, newpath, packageName);
                }
            }
        } else {
            String name = file.getName();
            //判断是否是类文件
            if ("class".equals(name.substring(name.lastIndexOf(".") + 1, name.length()))) {
                name = packageName + "." + name.substring(0, name.lastIndexOf("."));
                classNames.add(name);
            }
        }
    }

    /**
     * 扫描注解添加bean到容器
     */
    public static void addBeans() {
        URL url = BeanFactory.class.getClassLoader().getResource("");
        String path = url.getPath();
        path = path.substring(0, path.length() - 1);
        ArrayList<String> classNames = new ArrayList<>();
        setAllClassName(classNames, path, null);
        if (classNames == null) {
            return;
        }
        for (String name : classNames) {
            Class<?> aClass = null;
            try {
                aClass = Class.forName(name);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String key = name.substring(name.lastIndexOf(".") + 1, name.length());
            BeanScan annotation = aClass.getDeclaredAnnotation(BeanScan.class);
            if (annotation != null) {
                Object o = null;
                try {
                    o = aClass.newInstance();
                } catch (Exception e) {
                    logger.error("初始化bean失败", e);
                }
                beans.put(key, o);
            }
        }
    }

    /**
     * 加载mapper文件
     */
    public static void loadMapper(String path) {
        URL url = BeanFactory.class.getClassLoader().getResource(path);
        if (url != null) {
            String protocol = url.getProtocol();
            if ("file".equals(protocol)) {
                String p = url.getPath();
                File file = new File(p);
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    String fileName = f.getName();
                    if (pattern.matcher(fileName).find()) {
                        String key = fileName.substring(0, fileName.indexOf("."));
                        if (path != "") {
                            fileName = path + "/" + fileName;
                        }
                        InputStream resourceAsStream = BeanFactory.class.getClassLoader().getResourceAsStream(fileName);
                        Properties prop = new Properties();
                        try {
                            prop.load(resourceAsStream);
                        } catch (IOException e) {
                            logger.error("加载mapper文件失败",e);

                        }
                        props.put(key, prop);
                    }
                }
            }
        }
    }
}
