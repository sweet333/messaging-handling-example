package ru.sweetroyale.messaging.base.message;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public abstract class BaseMessage implements IMessage {

    public JsonObject toJsonObject() {
        JsonObject jsonObject = new Gson().toJsonTree(this).getAsJsonObject();

        jsonObject.addProperty("messageName", getClass().getSimpleName());

        return jsonObject;
    }

}
