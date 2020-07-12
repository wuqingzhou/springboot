package com.wqz.example.springboot.controller;

import com.wqz.example.springboot.utils.AppUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/nats")
public class NatsController {

    @ApiOperation("往topic发布消息")
    @GetMapping("/publish")
    public void publish(@RequestParam String topic, @RequestParam String message)
            throws IOException, InterruptedException {
        AppUtil.getNatsConnection().publish(topic, message.getBytes(StandardCharsets.UTF_8));
    }

    @ApiOperation("往topic发布消息（请求响应模式）")
    @GetMapping("/publishOfRequestReplyModel")
    public void publishOfRequestReplyModel(@RequestParam String topic,
                                           @RequestParam String repTopic,
                                           @RequestParam String message)
            throws IOException, InterruptedException {
        AppUtil.getNatsConnection().publish(topic, repTopic, message.getBytes(StandardCharsets.UTF_8));

        AppUtil.getNatsConnection().createDispatcher(msg -> {
            String repString = "报告！收到回复消息，消息内容为：" + new String(msg.getData(), StandardCharsets.UTF_8);
        }).subscribe(repTopic);
    }

    @ApiOperation("订阅topic的消息")
    @GetMapping("/subscribe")
    public void subscribe(@RequestParam String topic) throws IOException, InterruptedException {
        AppUtil.getNatsConnection().createDispatcher(msg -> {
            System.out.println("收到消息（普通订阅）：" + new String(msg.getData(), StandardCharsets.UTF_8));
        }).subscribe(topic);
    }

    @ApiOperation("订阅topic的消息（队列模式）")
    @GetMapping("/subscribeOfQueueModel")
    public void subscribeOfQueueModel(@RequestParam String topic, @RequestParam String queueName)
            throws IOException, InterruptedException {
        AppUtil.getNatsConnection().createDispatcher(msg -> {
            System.out.println("收到消息(队列订阅)：" + new String(msg.getData(), StandardCharsets.UTF_8));
        }).subscribe(topic, queueName);
    }

    @ApiOperation("订阅topic的消息（请求回复模式）")
    @GetMapping("/subscribeOfRequestReplyModel")
    public void subscribeOfRequestReplyModel(@RequestParam String topic)
            throws IOException, InterruptedException {
        AppUtil.getNatsConnection().createDispatcher(msg -> {
            System.out.println("收到消息(请求回复订阅)：" + new String(msg.getData(), StandardCharsets.UTF_8));
            String repString = "报告！订阅者已收到消息，消息内容为：" + new String(msg.getData(), StandardCharsets.UTF_8);
            try {
                AppUtil.getNatsConnection().publish(msg.getReplyTo(), repString.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).subscribe(topic);
    }
}
