package ru.sweetroyale.messaging.base.broker;

public interface IBroker {

    /**
     * Опубликовать сообщение
     *
     * @param message  сообщение
     * @param channels каналы
     */
    void publish(String message, String... channels);

    /**
     * Добавить слушатель
     *
     * @param listener слушатель
     * @param channels каналы
     */
    void addListener(IListener listener, String... channels);

}
