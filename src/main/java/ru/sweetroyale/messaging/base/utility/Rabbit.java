package ru.sweetroyale.messaging.base.utility;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.Builder;

@Builder
public class Rabbit {

    private String host, username, password;

    public Connection createConnection() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setUsername(username);
        factory.setPassword(password);

        try {
            return factory.newConnection();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
