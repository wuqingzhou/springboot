package com.wqz.example.springboot.utils;

import com.wqz.example.springboot.config.NatsConfig;
import io.nats.client.Connection;
import io.nats.client.Nats;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * 应用工具类
 */
@Component
public class AppUtil {
    public static Connection natsConnection = null;    // nats连接

    /**
     * 如果开启了nats功能，则创建nats连接
     */
    @PostConstruct
    public void initNatsConnection() throws IOException, InterruptedException {
        if (NatsConfig.getEnable()) {
            String url = "nats://" + NatsConfig.getHost() + ":" + NatsConfig.getPort();
            natsConnection = Nats.connect(url);
        }
    }
}
