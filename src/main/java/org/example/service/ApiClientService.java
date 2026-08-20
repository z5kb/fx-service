package org.example.service;

import org.example.model.FxRatesApiClientResponse;

public interface ApiClientService {

    public FxRatesApiClientResponse fetch(String currency);
}
