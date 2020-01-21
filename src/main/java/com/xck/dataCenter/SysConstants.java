package com.xck.dataCenter;

public class SysConstants {

    /*项目根路径*/
    public static final String PROJECT_BASE_PATH = System.getProperty("user.dir");

    /*可使用的核心数*/
    public static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    /*系统配置文件路径*/
    public static final String SYS_CONFIG_PATH = PROJECT_BASE_PATH + "/config/system.properties";
}
