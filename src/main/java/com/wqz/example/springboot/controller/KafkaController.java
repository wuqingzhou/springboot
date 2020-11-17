package com.wqz.example.springboot.controller;

import com.wqz.example.springboot.utils.AppUtil;
import io.swagger.annotations.ApiOperation;
import kafka.utils.ZkUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.security.JaasUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import scala.collection.JavaConversions;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("/kafka")
public class KafkaController {
    @ApiOperation(value = "查看topic列表", notes = "zookeeperStr参数示例：192.168.238.128:2181")
    @GetMapping("/getTopicList")
    public List<String> getTopicList(@RequestParam String zookeeperStr){
        ZkUtils zkUtils = ZkUtils.apply(zookeeperStr, 30000, 30000, JaasUtils.isZkSecurityEnabled());
        List<String> topicList = JavaConversions.seqAsJavaList(zkUtils.getAllTopics());
        return topicList;
    }

    /**
     * kafka生产者示例
     */
    public static void main1(String[] args) {
        String topicName = "mytopic1";
        String bootstrapServers = "192.168.238.128:9092";
        String keySerializer = "org.apache.kafka.common.serialization.StringSerializer";
        String valueSerializer = "org.apache.kafka.common.serialization.StringSerializer";
        Properties properties = new Properties();

        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("acks", "all");
        properties.put("retries", 0);
        properties.put("batch.size", 16384);
        properties.put("linger.ms", 1);
        properties.put("buffer.memory", 33554432);
        properties.put("key.serializer", keySerializer);
        properties.put("value.serializer", valueSerializer);

        Producer<String, String> producer = null;
        try {
            producer = new KafkaProducer<String, String>(properties);
            for (int i = 0; i < 1; i++) {
                String msg = "this is a msg from java api。" + new Date().getTime();
                producer.send(new ProducerRecord<String, String>(topicName, msg));
                System.out.println("消息发送成功。");
            }
        } catch (Exception e) {
            System.out.println("消息发送失败。");
        } finally {
            producer.close();
        }
    }

    /**
     * kafka消费者示例
     */
    public static void main2(String[] args) {
        String topicName = "mytopic1";
        String bootstrapServers = "192.168.238.128:9092";
        String groupId = "group-1";
        String keyDeserialize = "org.apache.kafka.common.serialization.StringDeserializer";
        String valueDdeserializer = "org.apache.kafka.common.serialization.StringDeserializer";
        Properties properties = new Properties();

        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("group.id", groupId);
        properties.put("enable.auto.commit", "true");
        properties.put("auto.commit.interval.ms", "1000");
        properties.put("auto.offset.reset", "earliest");
        properties.put("session.timeout.ms", "30000");
        properties.put("key.deserializer", keyDeserialize);
        properties.put("value.deserializer", valueDdeserializer);

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Arrays.asList(topicName));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, value = %s", record.offset(), record.value());
                System.out.println();
            }
        }
    }
}
