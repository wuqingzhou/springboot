package com.wqz.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * app-config 配置信息
 * 注：变量全部定义成静态。get方法是静态方法，set方法是普通方法
 */
@Component
@ConfigurationProperties(prefix = "app-config")
public class AppConfig {
    private static String name;
    private static String desc;
    private static String id;

    public static String getName() {
        return name;
    }

    public void setName(String name) {
        AppConfig.name = name;
    }

    public static String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        AppConfig.desc = desc;
    }

    public static String getId() {
        return id;
    }

    public void setId(String id) {
        AppConfig.id = id;
    }
}
