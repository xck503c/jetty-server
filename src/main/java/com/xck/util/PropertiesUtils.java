package com.xck.util;

import com.xck.dataCenter.SysConstants;
import org.apache.commons.lang.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {
    private static Properties sysPro = null;

    private static Properties getSysProperties(){
        if(sysPro == null){
            sysPro = getProperties(SysConstants.SYS_CONFIG_PATH);
        }
        return sysPro;
    }

    public static Properties getProperties(String configPath){
        Properties properties = new Properties();
        InputStream in = null;

        try {
            in = new FileInputStream(configPath);
            properties.load(in);
        } catch (FileNotFoundException e) {
            System.err.println("加载自定义配置文件system.properties异常");
        } catch (IOException e){
            e.printStackTrace();
        }

        return properties;
    }

    public static String getPropertyStr(String key, String defaultValue){
        Properties properties = getSysProperties();
        if(properties == null) return defaultValue;

        String valueStr = properties.getProperty(key);
        if(StringUtils.isBlank(valueStr)){
            return defaultValue;
        }
        return valueStr;
    }

    public static Integer getPropertyInt(String key, int defaultValue){
        Properties properties = getSysProperties();
        if(properties == null) return defaultValue;

        String valueStr = properties.getProperty(key);
        int value;
        try{
            value = Integer.parseInt(valueStr);
        }catch (NumberFormatException e){
            value = defaultValue;
        }
        return value;
    }

    public static Long getPropertyLong(String key, long defaultValue){
        Properties properties = getSysProperties();
        if(properties == null) return defaultValue;

        String valueStr = properties.getProperty(key);
        long value;
        try{
            value = Long.parseLong(valueStr);
        }catch (NumberFormatException e){
            value = defaultValue;
        }
        return value;
    }
}
