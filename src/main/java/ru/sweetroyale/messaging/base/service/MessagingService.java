package ru.sweetroyale.messaging.base.service;

import lombok.NonNull;
import ru.sweetroyale.messaging.base.message.IMessage;
import ru.sweetroyale.messaging.base.message.IMessageHandler;

public interface MessagingService {

    /**
     * Отправить сообщение в брокер
     *
     * @param message  сообщение
     * @param channels в какие каналы отправлять
     */
    void publish(@NonNull IMessage message, String... channels);

    /**
     * Установить обработчик для сообщения
     *
     * @param handle   обработчик
     * @param channels для каких каналов
     */
    void addHandler(@NonNull IMessageHandler<? extends IMessage> handle, String... channels);

    /**
     * Добавить слушатели канала
     *
     * @param channels каналы
     */
    void addListener(String... channels);

}
