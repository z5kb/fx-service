package org.example.service.impl;

import ch.qos.logback.classic.encoder.JsonEncoder;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.example.model.FxRatesApiClientResponse;
import org.example.service.CachingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

@Service
public class CachingServiceImpl implements CachingService {

    private final Gson gson;

    @Autowired
    public CachingServiceImpl(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void cache(FxRatesApiClientResponse response) {
        RedisCommands<String, String> commands = setupRedis();

        commands.setex(FX_API_DATA_REDIS_KEY, response.getTimeNextUpdateTimestamp(), gson.toJson(response));
        System.out.println("cached " + FX_API_DATA_REDIS_KEY);
    }

    @Override
    public FxRatesApiClientResponse getFxRatesData() {
        String dataRaw = setupRedis().get(FX_API_DATA_REDIS_KEY);
        return gson.fromJson(dataRaw, FxRatesApiClientResponse.class);
    }

    private RedisCommands<String, String> setupRedis() {
        RedisURI uri = RedisURI.Builder.redis(REDIS_HOST, REDIS_PORT).build();
        RedisClient client = RedisClient.create(uri);
        StatefulRedisConnection<String, String> connection = client.connect();
        return connection.sync();
    }


    private final String REDIS_HOST = "localhost";
    private final int REDIS_PORT = 6379;
    private final String FX_API_DATA_REDIS_KEY = "fx-api-data";
}
