package org.example.core.common.config;

import org.example.core.common.utils.CommonUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Author zhoufan
 * @create 2023/5/5
 */
public class PropertiesLoader {
    private static Properties properties;

    private static Map<String, String> propertiesMap = new HashMap<>();

    private static String DEFAULT_PROPERTIES_FILE = "/Users/pumpkin/learn/Irpc/prpc/prpc-core/src/main/resources/prpc.properties";

    public static void loadConfiguration() throws IOException {
        if(properties != null) {
            return;
        }
        properties = new Properties();
        FileInputStream in = null;
        in = new FileInputStream(DEFAULT_PROPERTIES_FILE);
        properties.load(in);
    }

    /**
     * 根据键获取值
     * @param key
     * @return
     */
    public static String getPropertiesStr(String key) {
        if(properties == null) {
            return null;
        }
        if(CommonUtils.isEmpty(key)) {
            return null;
        }
        if (!propertiesMap.containsKey(key)) {
            String value = properties.getProperty(key);
            propertiesMap.put(key, value);
        }
        return String.valueOf(propertiesMap.get(key));
    }

    public static Integer getPropertiesInteger(String key) {
        if(properties == null) {
            return null;
        }
        if(CommonUtils.isEmpty(key)) {
            return null;
        }
        if (!propertiesMap.containsKey(key)) {
            String value = properties.getProperty(key);
            propertiesMap.put(key, value);
        }
        return Integer.valueOf(propertiesMap.get(key));
    }
}
