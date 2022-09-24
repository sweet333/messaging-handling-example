# messaging-handling-example

### `description:` Example of message handling using redis or rabbitmq

### `manual:`

1. Create message

```java

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestMessage extends BaseMessage {

    private String username;
    private int balance;

}
```

2. Create handler

```java
public class TestMessageHandler extends BaseMessageHandler<TestMessage> {

    @Override
    public void handle(TestMessage message) {
        System.out.println(message.getUsername());
        System.out.println(message.getBalance());
    }

}
```

3. Create client

```java
public class Client {

    public static void main(String[] args) {
        Config config = new Config();
        config.setCodec(new JsonJacksonCodec());
        config.setThreads(8);
        config.setNettyThreads(8);
        config.useSingleServer()
                .setPassword("sweetroyale")
                .setAddress("redis://localhost:6379");

        RedissonClient redissonClient = Redisson.create(config);

        IBroker broker = new RedissonBroker(redissonClient);

        MessagingService messagingService = new MessagingServiceImpl(broker);
        messagingService.addListener("proxy-1");
        messagingService.addHandler(new TestMessageHandle(), "proxy-1");
    }
}
```

4. Create sender

```java
public class Sender {

    public static void main(String[] args) {
        Config config = new Config();
        config.setCodec(new JsonJacksonCodec());
        config.setThreads(8);
        config.setNettyThreads(8);
        config.useSingleServer()
                .setPassword("sweetroyale")
                .setAddress("redis://localhost:6379");

        RedissonClient redissonClient = Redisson.create(config);

        IBroker broker = new RedissonBroker(redissonClient);
        MessagingService messagingService = new MessagingServiceImpl(broker);
        messagingService.publish(new TestMessage("sweetroyale", 100), "proxy-1");
    }
}
```

or Client+Sender

```java
public class ClientSender {

    public static void main(String[] args) {
        Config config = new Config();
        config.setCodec(new JsonJacksonCodec());
        config.setThreads(8);
        config.setNettyThreads(8);
        config.useSingleServer()
                .setPassword("sweetroyale")
                .setAddress("redis://localhost:6379");

        RedissonClient redissonClient = Redisson.create(config);

        IBroker broker = new RedissonBroker(redissonClient);
        MessagingService messagingService = new MessagingServiceImpl(broker);
        messagingService.publish(new TestMessage("sweetroyale", 100), "proxy-1");

        messagingService.addListener("proxy-1");
        messagingService.addHandler(new TestMessageHandle(), "proxy-1");
    }
}
```
