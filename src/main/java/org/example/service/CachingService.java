package org.example.service;

import org.example.model.FxRatesApiClientResponse;

public interface CachingService {

    void cacheFxRatesData(FxRatesApiClientResponse response);

    FxRatesApiClientResponse getFxRatesData(String currency);
}
