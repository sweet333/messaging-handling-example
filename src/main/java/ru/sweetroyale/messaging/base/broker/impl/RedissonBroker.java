package ru.sweetroyale.messaging.base.broker.impl;

import lombok.NonNull;
import org.redisson.api.RedissonClient;
import ru.sweetroyale.messaging.base.broker.IBroker;
import ru.sweetroyale.messaging.base.broker.IListener;

public class RedissonBroker implements IBroker {

    private final RedissonClient redissonClient;

    public RedissonBroker(@NonNull RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void publish(String message, String... channels) {
        for (String channel : channels) {
            redissonClient.getTopic(channel).publish(message);
        }
    }

    @Override
    public void addListener(IListener listener, String... channels) {
        for (String channel : channels) {
            redissonClient.getTopic(channel.toLowerCase())
                    .addListenerAsync(String.class, (charSequence, message) -> listener.onMessage(channel, message));
        }
    }
}
