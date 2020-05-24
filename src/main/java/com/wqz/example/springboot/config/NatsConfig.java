package com.wqz.example.springboot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * nats配置信息
 */
@Component
@ConfigurationProperties(prefix = "nats")
public class NatsConfig {
    private static Boolean enable = false;
    private static String host = "localhost";
    private static Integer port = 4222;
    private static String defaultTopic = "myNatsTopic";

    public static Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        NatsConfig.enable = enable;
    }

    public static String getHost() {
        return host;
    }

    public void setHost(String host) {
        NatsConfig.host = host;
    }

    public static Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        NatsConfig.port = port;
    }

    public static String getDefaultTopic() {
        return defaultTopic;
    }

    public void setDefaultTopic(String defaultTopic) {
        NatsConfig.defaultTopic = defaultTopic;
    }
}
