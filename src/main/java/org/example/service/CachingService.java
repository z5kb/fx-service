package org.example.service;

import org.example.model.FxRatesApiClientResponse;

public interface CachingService {

    // store the Foreign Exchange Rates data
    void cacheFxRatesData(FxRatesApiClientResponse response, Long secondsUntilExpiration);

    // retrieve the Foreign Exchange Rates data
    FxRatesApiClientResponse getFxRatesData(String currency);
}
