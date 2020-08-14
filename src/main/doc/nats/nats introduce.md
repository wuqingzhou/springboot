##### nats是什么
- nats一个开源、轻量级、高性能消息中间件。nats的开发哲学认为高质量的QoS应该在客户端构建，故只建立了request-reply，不提供持久化、事务处理、增强的交付模式、企业级队列。
##### nats支持哪些消息传递模型
- 发布订阅
- 队列订阅
- 请求回复
##### 发布订阅模型简介
- nats将发布订阅消息传递模型实现为一对多通信，发布者在subject上发布一条消息，那么所有监听了此subject的订阅者都会收到该消息。如下图：
- 生产者发布消息，java代码示例：
```
/* 生成者发布消息 */
// 建立连接
Connection nc = Nats.connect("nats://127.0.0.1:4222");
// 往主题名为："subject"的主题上发布一条消息，消息内容为："hello world"，消息的编码格式为：UTF-8
nc.publish("subject", "hello world".getBytes(StandardCharsets.UTF_8));
```
- 普通消费者订阅消息，java代码示例：
```
/* 普通消费者订阅消息 */
// 建立连接
Connection nc = Nats.connect("nats://127.0.0.1:4222");
// 订阅主题：subject
Subscription sub = nc.subscribe("subject");
// 拉取主题上的下一条消息
Message msg = sub.nextMessage(Duration.ofMillis(500));
// 将消息使用UTF-8转码，转成字符串
String response = new String(msg.getData(), StandardCharsets.UTF_8);
```
- 基于回调的消费者订阅消息，java代码示例：
```
/* 基于回调的消费者订阅消息 */
// 建立连接
Connection nc = Nats.connect("nats://127.0.0.1:4222");
// 创建一个调度器，调度器订阅某主题后，一旦监听到主题有新消息，则会马上执行
Dispatcher d = nc.createDispatcher(msg - >{
    // 将消息使用UTF-8转码，转成字符串
    String response = new String(msg.getData(), StandardCharsets.UTF_8);
    //do something
});
// 调度器订阅主题：subject
d.subscribe("subject");
```
##### 队列订阅模型简介
- NATS提供称为队列订阅的负载均衡功能。主要功能是将具有相同queue名字的subject进行负载均衡。使用队列订阅功能消息发布者不需要做任何改动，消息接受者需要具有相同的队列名。
要创建队列订阅，订户会注册队列名称。具有相同队列名称的所有订户构成队列组。这不需要配置。当发布已注册主题上的消息时，随机选择该组中的一个成员来接收该消息。尽管队列组具有多个订户，但每个消息仅由一个消息使用。
NATS的一个重要特性是队列组由应用程序及其队列订户定义，而不是在服务器配置上定义。如下图：

- 基于回调的生成者发布消息，java代码示例：
```
/* 生成者发布消息 */
// 建立连接
Connection nc = Nats.connect("nats://127.0.0.1:4222");
// 往主题名为："subject"的主题上发布一条消息，消息内容为："hello world"，消息的编码格式为：UTF-8
nc.publish("subject", "hello world".getBytes(StandardCharsets.UTF_8));
```
- 基于回调的消费者订阅消息，java代码示例：
```
/* 基于回调的消费者订阅消息 */
// 建立连接
Connection nc = Nats.connect("nats://127.0.0.1:4222");
// 创建一个调度器，调度器订阅某主题后，一旦监听到主题有新消息，则会马上执行
Dispatcher d = nc.createDispatcher(msg - >{
    // 将消息使用UTF-8转码，转成字符串
    String response = new String(msg.getData(), StandardCharsets.UTF_8);
    //do something
});
// 调度器订阅主题：subject
d.subscribe("subject","queName");
```
##### 请求回复模式订阅模式
- 在Request Reply过程中，发布请求发布带有响应主题的消息，期望对该subject做出响应操作。如下图：

- 请求回复模式下生产者发布消息，java代码示例：
```
/* 请求回复模式下生成者发布消息 */
// 建立连接
Connection nc = Nats.connect("nats://127.0.0.1:4222");
// 往主题名为："subject"的主题上发布一条消息，消息内容为："hello world"，消息的编码格式为：UTF-8。
// 同时指定回复主题为repSubject，期望消费者收到消息后，往repSubject主题里发送一条回复消息。
nc.publish("subject", "repSubject", "hello world".getBytes(StandardCharsets.UTF_8));

nc.createDispatcher(msg -> {
    String repString = "报告！收到回复消息，消息内容为：" + new String(msg.getData(), StandardCharsets.UTF_8);
}).subscribe(repSubject);
```
- 请求回复模式下消费者订阅消息，java代码示例：
```
/* 请求回复模式下消费者订阅消息 */
// 建立连接
Connection nc = Nats.connect("nats://127.0.0.1:4222");
// 创建一个调度器，调度器订阅某主题后，一旦监听到主题有新消息，则会马上执行
Dispatcher d = nc.createDispatcher(msg - >{
    // 将消息使用UTF-8转码，转成字符串
    String response = new String(msg.getData(), StandardCharsets.UTF_8);
    try {
        String repString = "报告！订阅者已收到消息，消息内容为：" + new String(msg.getData(), StandardCharsets.UTF_8);
        nc.publish(msg.getReplyTo(), repString.getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
        e.printStackTrace();
    }
    //do something
});
// 调度器订阅主题：subject
d.subscribe("subject");
```
##### 参考资料
- https://www.jianshu.com/p/2110d4f43d04
- https://blog.csdn.net/weixin_43809223/article/details/107073635?%3E?utm_medium=distribute.pc_relevant.none-task-blog-baidujs-1
