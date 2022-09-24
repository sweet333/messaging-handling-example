package ru.sweetroyale.messaging.base.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import lombok.NonNull;
import ru.sweetroyale.messaging.base.broker.IBroker;
import ru.sweetroyale.messaging.base.message.IMessage;
import ru.sweetroyale.messaging.base.message.IMessageHandler;
import ru.sweetroyale.messaging.base.service.MessagingService;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessagingServiceImpl implements MessagingService {

    private final Map<String, Map<String, IMessageHandler<? extends IMessage>>> messageHandlerMap = new ConcurrentHashMap<>();
    private final IBroker broker;

    public MessagingServiceImpl(IBroker broker) {
        this.broker = broker;
    }

    @Override
    public void publish(@NonNull IMessage message, String... channels) {
        try {
            broker.publish(message.toJsonObject().toString(), channels);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addHandler(@NonNull IMessageHandler<? extends IMessage> handle, String... channels) {
        for (String channel : channels) {
            createMapIfNotExists(channel);

            Map<String, IMessageHandler<? extends IMessage>> handlerMap = messageHandlerMap.get(channel);
            handlerMap.put(handle.getMessageClass().getSimpleName(), handle);

            messageHandlerMap.put(handle.getMessageClass().getName(), handlerMap);
        }
    }

    @Override
    public void addListener(String... channels) {
        try {
            broker.addListener((channel, jsonMessage) -> {
                String messageKey = JsonParser.parseString(jsonMessage).getAsJsonObject().get("messageName").getAsString();

                IMessageHandler<? extends IMessage> handler = messageHandlerMap.get(channel).get(messageKey);

                handler.handle(new Gson().fromJson(jsonMessage, (Type) handler.getMessageClass()));
            }, channels);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createMapIfNotExists(String channel) {
        if (!messageHandlerMap.containsKey(channel)) {
            messageHandlerMap.put(channel, new HashMap<>());
        }
    }

}
