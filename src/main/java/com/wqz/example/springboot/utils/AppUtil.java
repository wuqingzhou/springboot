package com.wqz.example.springboot.utils;

import com.wqz.example.springboot.config.NatsConfig;
import io.nats.client.Connection;
import io.nats.client.Nats;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 应用工具类
 */
@Component
public class AppUtil {
    public static Connection natsConnection = null;    // nats连接

    /**
     * 获取默认的nats连接
     */
    public static Connection getNatsConnection() throws IOException, InterruptedException {
        if (!NatsConfig.getEnable()) {
            return null;
        }

        if (null == natsConnection) {
            String url = "nats://" + NatsConfig.getHost() + ":" + NatsConfig.getPort();
            natsConnection = Nats.connect(url);
        }
        return natsConnection;
    }
}
