package org.example.service;

import com.google.gson.JsonObject;
import io.lettuce.core.api.sync.RedisCommands;
import org.example.model.FxRatesApiClientResponse;

public interface CachingService {

    void cache(FxRatesApiClientResponse response);

    FxRatesApiClientResponse getFxRatesData();
}
