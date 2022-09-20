package ru.sweetroyale.messaging.base.message;

import com.google.gson.JsonObject;

public interface IMessage {

    /**
     * Преобразовать в JsonObject
     *
     * @return JsonObject
     */
    JsonObject toJsonObject();

}
