package ru.sweetroyale.messaging.base.utility;

import lombok.*;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class Redis {

    private String host, password;
    private int port;

    public RedissonClient getRedisClient() {
        Config config = new Config();
        config.setCodec(new JsonJacksonCodec());
        config.setThreads(8);
        config.setNettyThreads(8);
        config.useSingleServer()
                .setPassword(password)
                .setAddress("redis://" + host + ":" + port);

        return Redisson.create(config);
    }
}
