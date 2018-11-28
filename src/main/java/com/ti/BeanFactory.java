package com.ti;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Pattern;

public class BeanFactory {
    private static HashMap<String, Object> beans = new HashMap<>();
    private static HashMap<String, Properties> props = new HashMap<>();
    private static Pattern pattern = Pattern.compile("^.+Mapper.properties");


    public static void main(String[] args) {
        for (String fileName : props.keySet()) {
            System.out.println(fileName);
            Properties properties = props.get(fileName);
            for (Object o : properties.keySet()) {
                System.out.print(o);
                System.out.println(":"+properties.getProperty((String)o));
            }
        }
    }

    public static HashMap<String, Properties> getProps() {
        return props;
    }


    //加载mapper
    static {
        URL url = BeanFactory.class.getClassLoader().getResource("");
        if (url != null) {
            String protocol = url.getProtocol();
            if ("file".equals(protocol)) {
                String path = url.getPath();
                File file = new File(path);
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    String fileName = f.getName();
                    if (pattern.matcher(fileName).find()) {
                        String key = fileName.substring(0, fileName.indexOf("."));
                        InputStream resourceAsStream = BeanFactory.class.getClassLoader().getResourceAsStream(fileName);
                        Properties prop = new Properties();
                        try {
                            prop.load(resourceAsStream);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        props.put(key, prop);
                    }
                }
            }
        }
    }
}
