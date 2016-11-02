package com.chyun.config.manager.client.core;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * 类的实现描述
 */
public class ConfigUtil {
    private static final String DEFAULT_DECODE = "utf-8";
    private static final String DEFAULT_NAMESPACE = "/config-center";
    private static Map<String, String> proptiesContainer = Maps.newHashMap();

    private static RemoteConfigReader rcr = new RemoteConfigReader();

    public static String getProperty(String schema, String prop) {
        if (proptiesContainer.keySet().contains(prop)) {
            return proptiesContainer.get(prop);
        }
        String path = generateKeyPath(schema, prop);
        try {
            byte[] data = rcr.read(path);
            String value = new String(data, Charset.forName(DEFAULT_DECODE));
            proptiesContainer.put(prop, value);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void modifyProperty(String path, byte[] value) {
        if (StringUtils.isNotBlank(path)) {
            String property = StringUtils.substringAfterLast(path, "/");
            proptiesContainer.put(property, new String(value, Charset.forName(DEFAULT_DECODE)));
        }
    }

    private static String generateServicePath(String schema) {
        return DEFAULT_NAMESPACE.concat("/").concat(schema);
    }
    private static String generateKeyPath(String schema, String key) {
        return generateServicePath(schema).concat("/").concat(key);
    }
}
