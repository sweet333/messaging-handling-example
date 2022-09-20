package ru.sweetroyale.messaging.base.message;

import java.lang.reflect.ParameterizedType;

public abstract class BaseMessageHandler<T extends IMessage> implements IMessageHandler<T> {

    @Override
    @SuppressWarnings("unchecked")
    public Class<T> getMessageClass() {
        try {
            return (Class<T>) ((ParameterizedType) getClass()
                    .getGenericSuperclass())
                    .getActualTypeArguments()[0];
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
