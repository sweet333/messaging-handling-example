package ru.sweetroyale.messaging.base.broker.impl;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import lombok.NonNull;
import ru.sweetroyale.messaging.base.broker.IBroker;
import ru.sweetroyale.messaging.base.broker.IListener;

import java.nio.charset.StandardCharsets;

public class RabbitBroker implements IBroker {

    private final Connection connection;

    public RabbitBroker(@NonNull Connection connection) {
        this.connection = connection;
    }

    @Override
    public void publish(String message, String... channels) {
        for (String channel : channels) {
            try (Channel connectionChannel = connection.createChannel()) {
                connectionChannel.exchangeDeclare(channel, "fanout");
                connectionChannel.basicPublish(channel, "", null, message.getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addListener(IListener listener, String... channels) {
        try (Channel connectionChannel = connection.createChannel()) {
            for (String channel : channels) {
                connectionChannel.exchangeDeclare(channel, "fanout");
                String queueName = connectionChannel.queueDeclare().getQueue();
                connectionChannel.queueBind(queueName, channel, "");

                DeliverCallback deliverCallback = (consumerTag, delivery) ->
                        listener.onMessage(channel, new String(delivery.getBody(), StandardCharsets.UTF_8));

                connectionChannel.basicConsume(queueName, true, deliverCallback, consumerTag -> {

                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
