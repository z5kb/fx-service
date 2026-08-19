package org.example.service.impl;

import com.google.gson.JsonObject;
import org.example.model.FxRatesApiClientResponse;
import org.example.service.CachingService;
import org.springframework.stereotype.Service;

@Service
public class CachingServiceImpl implements CachingService {

    @Override
    public void cache(FxRatesApiClientResponse response) {
        System.out.println("caching " + response);
    }
}
