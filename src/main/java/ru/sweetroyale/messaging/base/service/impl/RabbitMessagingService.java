package ru.sweetroyale.messaging.base.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import lombok.NonNull;
import ru.sweetroyale.messaging.base.message.IMessage;
import ru.sweetroyale.messaging.base.message.IMessageHandler;
import ru.sweetroyale.messaging.base.service.BaseMessagingService;
import ru.sweetroyale.messaging.base.utility.Rabbit;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class RabbitMessagingService extends BaseMessagingService {

    private final Connection connection = Rabbit.builder()
            .host("localhost")
            .username("sweetroyale")
            .password("sweetroyale").build().createConnection();

    @Override
    public void publish(@NonNull IMessage message, String... channels) {
        for (String channel : channels) {
            try (Channel connectionChannel = connection.createChannel()) {
                connectionChannel.exchangeDeclare(channel, "fanout");
                connectionChannel.basicPublish(channel, "", null, message.toJsonObject().toString().getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addListener(@NonNull String... channels) {
        try {
            Channel connectionChannel = connection.createChannel();

            for (String channel : channels) {
                createMapIfNotExists(channel);

                connectionChannel.exchangeDeclare(channel, "fanout");
                String queueName = connectionChannel.queueDeclare().getQueue();
                connectionChannel.queueBind(queueName, channel, "");

                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String response = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    String messageKey = JsonParser.parseString(response).getAsJsonObject().get("messageName").getAsString();

                    IMessageHandler<? extends IMessage> handler = messageHandlerMap.get(channel).get(messageKey);

                    handler.handle(new Gson().fromJson(response, (Type) handler.getMessageClass()));
                };

                connectionChannel.basicConsume(queueName, true, deliverCallback, consumerTag -> {

                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
