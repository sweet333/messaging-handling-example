package ru.sweetroyale.messaging.base.broker;

import lombok.NonNull;

public interface IListener {

    /**
     * Действие при сообщении
     *
     * @param channel канал
     * @param message сообщение
     */
    void onMessage(@NonNull String channel, String message);

}
