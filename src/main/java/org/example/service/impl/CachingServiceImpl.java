package org.example.service.impl;

import com.google.gson.Gson;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.example.model.FxRatesApiClientResponse;
import org.example.service.CachingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CachingServiceImpl implements CachingService {

    private final Gson gson;

    @Autowired
    public CachingServiceImpl(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void cacheFxRatesData(FxRatesApiClientResponse response) {
        redis().setex(FX_API_DATA_REDIS_KEY, response.getTimeNextUpdateTimestamp(), gson.toJson(response));
        System.out.println("cached " + FX_API_DATA_REDIS_KEY);
    }

    @Override
    public FxRatesApiClientResponse getFxRatesData() {
        String dataRaw = redis().get(FX_API_DATA_REDIS_KEY);
        return gson.fromJson(dataRaw, FxRatesApiClientResponse.class);
    }

    // connect to the running Redis server and return a connection object
    private RedisCommands<String, String> redis() {
        RedisURI uri = RedisURI.Builder.redis(REDIS_HOST, REDIS_PORT).build();
        RedisClient client = RedisClient.create(uri);
        StatefulRedisConnection<String, String> connection = client.connect();
        return connection.sync();
    }

    private final String REDIS_HOST = "localhost";
    private final int REDIS_PORT = 6379;
    private final String FX_API_DATA_REDIS_KEY = "fx-api-data";
}
