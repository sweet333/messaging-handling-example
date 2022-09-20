package ru.sweetroyale.messaging.base.service;

import lombok.NonNull;
import ru.sweetroyale.messaging.base.message.IMessage;
import ru.sweetroyale.messaging.base.message.IMessageHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseMessagingService implements MessagingService {

    protected final Map<String, Map<String, IMessageHandler<? extends IMessage>>> messageHandlerMap = new ConcurrentHashMap<>();

    @Override
    public void addHandler(@NonNull IMessageHandler<? extends IMessage> handle, String... channels) {
        for (String channel : channels) {
            createMapIfNotExists(channel);

            Map<String, IMessageHandler<? extends IMessage>> handlerMap = messageHandlerMap.get(channel);
            handlerMap.put(handle.getMessageClass().getSimpleName(), handle);

            messageHandlerMap.put(handle.getMessageClass().getName(), handlerMap);
        }
    }

    protected void createMapIfNotExists(String channel) {
        if (!messageHandlerMap.containsKey(channel)) {
            messageHandlerMap.put(channel, new HashMap<>());
        }
    }
}
