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

}
```

2. Create handler

```java
public class TestMessageHandler extends BaseMessageHandler<TestMessage> {

    @Override
    public void handle(TestMessage message) {
        System.out.println(message.getUsername());
    }

}
```

3. Create handler

```java
public class TestMessageHandler extends BaseMessageHandler<TestMessage> {

    @Override
    public void handle(TestMessage message) {
        System.out.println(message.getUsername());
    }

}
```

4. Create client

```java
public class Client {

    public static void main(String[] args) {
        MessagingService messagingService = new RabbitMessagingService(); //Rabbitmq or redis

        messagingService.addListener("lobby-1");
        messagingService.addHandler(new TestMessageHandler(), "lobby-1");
    }
}
```

5. Create sender

```java
public class Sender {

    public static void main(String[] args) {
        MessagingService messagingService = new RabbitMessagingService();

        messagingService.publish(new TestMessage("sweetroyale"), "lobby-1");
    }
}
```

or Client+Sender

```java
public class ClientSender {

    public static void main(String[] args) {
        MessagingService messagingService = new RabbitMessagingService();

        messagingService.addListener("lobby-1");
        messagingService.addHandler(new TestMessageHandler(), "lobby-1");

        messagingService.publish(new TestMessage("sweetroyale"), "proxy-1");
    }
}
```

I went to bed, good night.