package ru.sweetroyale.messaging.base.message;

public interface IMessageHandler<T extends IMessage> {

    /**
     * Получить класс сообщения
     *
     * @return класс сообщения
     */
    Class<? extends IMessage> getMessageClass();

    void handle(T message);

}
