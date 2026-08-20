package org.example.service;

import org.example.model.FxRatesApiClientResponse;

public interface ApiClientService {

    // fetch the Foreign Exchange Rates data from the API
    public FxRatesApiClientResponse fetch(String currency);
}
