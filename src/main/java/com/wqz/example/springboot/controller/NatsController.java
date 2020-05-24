package com.wqz.example.springboot.controller;

import com.wqz.example.springboot.config.NatsConfig;
import com.wqz.example.springboot.utils.AppUtil;
import io.nats.client.Dispatcher;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Api("nats Controller")
@RestController
@RequestMapping("/nats")
public class NatsController {

    @ApiOperation("发布消息")
    @GetMapping("/publish")
    public void publish(@RequestParam String message) throws IOException, InterruptedException {
        AppUtil.natsConnection.publish(NatsConfig.getDefaultTopic(), message.getBytes(StandardCharsets.UTF_8));
    }

    @ApiOperation("订阅默认topic的消息")
    @GetMapping("/subscribe")
    public void subscribe() throws IOException, InterruptedException {
        AppUtil.natsConnection.createDispatcher(msg -> {
            System.out.println("收到消息：" + new String(msg.getData(), StandardCharsets.UTF_8));
        }).subscribe(NatsConfig.getDefaultTopic());
    }
}
