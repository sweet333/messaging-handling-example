package ru.sweetroyale.messaging.base.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import lombok.NonNull;
import org.redisson.api.RedissonClient;
import ru.sweetroyale.messaging.base.message.IMessage;
import ru.sweetroyale.messaging.base.message.IMessageHandler;
import ru.sweetroyale.messaging.base.service.BaseMessagingService;
import ru.sweetroyale.messaging.base.utility.Redis;

import java.lang.reflect.Type;

public class RedisMessagingService extends BaseMessagingService {

    private final RedissonClient redis = Redis.builder()
            .password("sweetroyale").port(6379)
            .host("localhost").build().getRedisClient();

    @Override
    public void publish(@NonNull IMessage message, String... channels) {
        for (String channel : channels) {
            redis.getTopic(channel).publish(message.toJsonObject().toString());
        }
    }

    @Override
    public void addListener(@NonNull String... channels) {
        try {
            for (String channel : channels) {
                createMapIfNotExists(channel);

                redis.getTopic(channel.toLowerCase()).addListenerAsync(String.class, (charSequence, message) -> {
                    try {
                        String messageKey = JsonParser.parseString(message).getAsJsonObject().get("messageName").getAsString();

                        IMessageHandler<? extends IMessage> handler = messageHandlerMap.get(channel).get(messageKey);

                        handler.handle(new Gson().fromJson(message, (Type) handler.getMessageClass()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
