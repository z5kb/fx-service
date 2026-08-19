package org.example.service;

import com.google.gson.JsonObject;
import org.example.model.FxRatesApiClientResponse;

public interface CachingService {

    void cache(FxRatesApiClientResponse response);
}
